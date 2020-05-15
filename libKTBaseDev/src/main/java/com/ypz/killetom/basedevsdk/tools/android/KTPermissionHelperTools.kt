package com.ypz.killetom.basedevsdk.tools.android

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class KTPermissionHelperTools {

    class Config internal constructor(val context: Activity) {

        private var permissions = ArrayList<String>()

        private lateinit var unallowedPermission: List<String>

        private var grantedAction: KTPermissionGrantedAction? = null

        private var failAction: KTPermissionFailAction? = null

        private var failTimes = 1

        private var retryTimes = 1

        private var retryCurrentTime = 0

        private val responseAction = object : KTPermissionResponseAction {
            override fun response(code: Int, permissions: Array<String>, results: IntArray) {

                if (code == REQUEST_PERMISSION_CODE) {

                    val failPermissions = getUnGranted()

                    //成功
                    if (failPermissions.isEmpty()) {

                        grantedAction?.success()

                        return
                    }

                    //重试达到最大值
                    if (retryCurrentTime >= retryTimes) {

                        failAction?.failMaxTimeAction(failPermissions, retryTimes)

                        return
                    }

                    retryCurrentTime += 1

                    failAction?.fail(failPermissions, retryCurrentTime)
                }

            }

        }

        fun addPermission(init: Config.() -> String): Config = apply {

            if (!allowPermissionStatus(init())) {
                permissions.add(init())
            }
        }

        fun addPermissions(init: Config.() -> Array<String>): Config = apply {

            init().forEach { permission ->

                if (!allowPermissionStatus(permission, context)) {
                    permissions.add(permission)
                }

            }
        }

        fun setRequestPermissions(init: Config.() -> ArrayList<String>): Config = apply {

            permissions.clear()

            val fillerPermissions = init().filter { permission ->
                return@filter (!allowPermissionStatus(permission, context))
            }

            permissions.addAll(fillerPermissions)
        }

        fun setRetryTime(init: Config.() -> Int): Config = apply {
            retryTimes = init()
        }

        fun setGrantedAction(init: Config.() -> KTPermissionGrantedAction): Config = apply {
            grantedAction = init()
        }

        fun setFailAction(init: Config.() -> KTPermissionFailAction): Config = apply {
            failAction = init()
        }

        fun setResponsePro(init: Config.() -> KTPermissionResponsePro): Config = apply {
            init().setResponseAction { responseAction }
        }

        fun config() {

            unallowedPermission = getUnGranted()

            if (unallowedPermission.isEmpty()) {

                grantedAction?.success()

                return
            }

            ActivityCompat
                .requestPermissions(
                    context, unallowedPermission.toTypedArray(),
                    REQUEST_PERMISSION_CODE
                )
        }

        private fun getUnGranted(): List<String> {
            val fillerPermissions = permissions.filter { permission ->
                return@filter (!allowPermissionStatus(permission, context))
            }

            return fillerPermissions
        }

    }

    interface KTPermissionGrantedAction {
        fun success()
    }

    interface KTPermissionFailAction {

        fun fail(failPermission: List<String>, failTimes: Int)

        fun failMaxTimeAction(retryTimes: List<String>, retryTimes1: Int)
    }

    interface KTPermissionResponseAction {
        fun response(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    }

    class KTPermissionResponsePro {

        protected open var action: KTPermissionResponseAction? = null

        fun setResponseAction(init: KTPermissionResponsePro.() -> KTPermissionResponseAction?) {
            action = init()
        }

        fun response(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            action?.response(requestCode, permissions, grantResults)
        }
    }

    companion object {

        const val REQUEST_PERMISSION_CODE = 0x76

        fun getBuilder(context: Activity): Config {
            return Config(context)
        }

        fun allowPermissionStatus(
            permission: String,
            context: Context = KTResHelperTools.appContext
        ): Boolean {

            val checkCode = ContextCompat.checkSelfPermission(context, permission)

            return checkCode == PackageManager.PERMISSION_GRANTED
        }
    }

}