package com.ypz.killetom.killetomeasyandroiddevtools

import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
import com.ypz.killetom.basedevsdk.tools.android.BaseActivity
import com.ypz.killetom.basedevsdk.tools.android.KTPermissionHelperTools
import com.ypz.killetom.basedevsdk.tools.android.KTResHelperTools
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.KTBluerSupportRender
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.v30.KTV30BlurRender
import com.ypz.killetom.libktsupportgles.KTGLHelperTools
import com.ypz.killetom.libktsupportgles.KTGLSupperRender
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KTResHelperTools.init(this.applicationContext)

        val blurRender = KTBluerSupportRender
            .getBuilder(this)
            .setScale { 0.55f }
            .setRadius { 25 }
            .setOriginBitmap {
                KTResHelperTools.getDrawable(R.drawable.qrlogo2).toBitmap()
            }.build()

        KTGLHelperTools.init(gl, blurRender)

        gl.requestRender()


    }

    fun requestPermission() {
        KTPermissionHelperTools
            .getBuilder(this)
            .addPermission { "" }
            .setFailAction {
                object : KTPermissionHelperTools.KTPermissionFailAction {

                    override fun fail(failPermission: List<String>, failTimes: Int) {

                    }

                    override fun failMaxTimeAction(retryTimes: List<String>, retryTimes1: Int) {
                    }

                }
            }.setGrantedAction {
                object : KTPermissionHelperTools.KTPermissionGrantedAction {
                    override fun success() {

                    }
                }
            }.setResponsePro { responsePermission }
            .config()
    }
}
