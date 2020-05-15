package com.ypz.killetom.basedevsdk.tools.android

import android.app.Activity

abstract class BaseActivity : Activity() {

    protected val responsePermission by lazy {
        KTPermissionHelperTools.KTPermissionResponsePro()
    }


    override fun onRequestPermissionsResult(
        code: Int, permissions: Array<String>, results: IntArray
    ) {

        super.onRequestPermissionsResult(code, permissions, results)

        responsePermission.response(code, permissions, results)
    }
}