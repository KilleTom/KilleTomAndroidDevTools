package com.ypz.killetom.killetomeasyandroiddevtools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.drawable.toBitmap
import com.ypz.killetom.basedevsdk.tools.android.KTResHelperTools
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.v20.KTBlurRender
import com.ypz.killetom.libktsupportgles.KTGLHelperTools
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KTResHelperTools.init(this.applicationContext)

        val blueV2Render = KTBlurRender
            .getBuilder(this)
            .setScale {
                0.55f
            }
            .setRadius {
                25
            }
            .setOriginBitmap {
                KTResHelperTools.getDrawable(R.drawable.qrlogo2).toBitmap()
            }.build()

        KTGLHelperTools.initV20(gl, blueV2Render)

        gl.requestRender()


    }
}
