package com.ypz.killetom.basedevsdkui.ui.widget.opengl

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import com.ypz.killetom.basedevsdk.tools.KTBlurTools
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.v20.KTV20BlurRender
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.v30.KTV30BlurRender
import com.ypz.killetom.libktsupportgles.KTGLHelperTools
import com.ypz.killetom.libktsupportgles.KTGLShareProgramCode
import com.ypz.killetom.libktsupportgles.KTTextureBean
import com.ypz.killetom.libktsupportgles.v20.KTGLV20SupperRender
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.write

abstract class KTBluerSupportRender internal constructor(context: Context) : KTGLV20SupperRender() {

    protected val lock = ReentrantReadWriteLock()

    protected val blurVertexShaderCode = KTGLShareProgramCode.KT_VERTEX_2D_SHADER
    protected val blurFragmentShaderCode = KTGLShareProgramCode.KT_FRAGMENT_2D_SHADER

    protected val TEX_VERTEX_COMPONENT_COUNT = 2
    protected val POSITION_COMPONENT_COUNT = 3

    protected var uTextureUnitLocation: Int = 0
    protected var mAPositionLocation: Int = 0

    protected val texturePoint = floatArrayOf(
        0f, 0f,
        0f, 1f,
        1f, 1f,
        1f, 0f
    )
    protected val BluerexVertexBuffer = KTGLHelperTools.createFloatBuffer(texturePoint)

    //x[-1,1]  y[-1,1]  L(-1)->R(1) T(1)->B(-1)

    protected val blueT = 1f
    protected val blueB = -1f
    protected val blurL = -1f
    protected val blurR = 1f

    protected val bluerPoint = floatArrayOf(
        blurL, blueT, 0f,  //左上
        blurL, blueB, 0f,  //左下
        blurR, blueB, 0f,  //右下
        blurR, blueT, 0f //右上
    )
    protected var bluerBuffer = KTGLHelperTools.createFloatBuffer(bluerPoint)


    protected val mProjectionMatrix = FloatArray(16)

    protected var bluerData: KTBluerData? = null
    protected var textureBean: KTTextureBean? = null

    //    protected val

    protected val mainProgramTarget = "${this::class.java.simpleName}_MAIN"


    protected fun applyBluerData(bluerData: KTBluerData) {

        lock.write {
            this.bluerData = bluerData

            val w = bluerData.originalBitmap.width
            val h = bluerData.originalBitmap.height

            if (w == h) {
                bluerBuffer = KTGLHelperTools.createFloatBuffer(texturePoint)
            }

            if (w > h) {
                val radio = h * 1.0f / w

                val point = floatArrayOf(
                    blurL, blueT * radio, 0f,  //左上
                    blurL, blueB * radio, 0f,  //左下
                    blurR, blueB * radio, 0f,  //右下
                    blurR, blueT * radio, 0f //右上
                )
                bluerBuffer = KTGLHelperTools.createFloatBuffer(point)
            }

            if (w < h) {
                val radio = w * 1.0f / h

                val point = floatArrayOf(
                    blurL * radio, blueT, 0f,  //左上
                    blurL * radio, blueB, 0f,  //左下
                    blurR * radio, blueB, 0f,  //右下
                    blurR * radio, blueT, 0f //右上
                )
                bluerBuffer = KTGLHelperTools.createFloatBuffer(point)
            }

        }
    }

    abstract class Builder<R : KTBluerSupportRender> internal constructor(val context: Context) {

        private var originBitmap: Bitmap? = null

        private var bluerWay: KTBlurTools.BlueWay = KTBlurTools.BlueWay.RenderScript

        private var scale = 0.75f

        private var radius = 10

        fun build(): R {
            val bitmap = originBitmap ?: throw RenderBuildException("origin bitmap null")

            if (bitmap.isRecycled)
                throw RenderBuildException("origin bitmap already recycled")

            val bluerData = KTBluerData(bluerWay, bitmap, scale, radius)

            val r = buildByData(bluerData)

            return r
        }

        protected abstract fun buildByData(bluerData: KTBluerData): R

        fun setOriginBitmap(init: Builder<R>.() -> Bitmap): Builder<R> = apply {
            originBitmap = init()
        }

        fun setBlurWay(init: Builder<R>.() -> KTBlurTools.BlueWay): Builder<R> = apply {
            bluerWay = init()
        }

        fun setScale(init: Builder<R>.() -> Float): Builder<R> = apply {
            scale = init()
        }

        fun setRadius(init: Builder<R>.() -> Int): Builder<R> = apply {
            radius = init()
        }


    }

    companion object {

        fun getBuilder(context: Context): Builder<*> {

            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

            val info = activityManager.deviceConfigurationInfo

            val version = info.reqGlEsVersion

            if (version >= 0x30000) {
                return KTV30BlurRender.getBuilder(context)
            } else {
                return KTV20BlurRender.getBuilder(context)
            }


        }
    }
}