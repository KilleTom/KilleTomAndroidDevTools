package com.ypz.killetom.basedevsdkui.ui.widget.opengl.v20

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.ypz.killetom.basedevsdk.tools.KTBlurTools
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.KTBluerData
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.RenderBuildException
import com.ypz.killetom.libktsupportgles.KTGLHelperTools
import com.ypz.killetom.libktsupportgles.KTGLShareProgramCode
import com.ypz.killetom.libktsupportgles.KTTextureBean
import com.ypz.killetom.libktsupportgles.v20.KTGLV20SupperRender
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.concurrent.read
import kotlin.concurrent.write

class KTBlurRender private constructor(val context: Context) : KTGLV20SupperRender() {

    private val lock = ReentrantReadWriteLock()

    private val blurVertexShaderCode = KTGLShareProgramCode.KT_VERTEX_2D_SHADER
    private val blurFragmentShaderCode = KTGLShareProgramCode.KT_FRAGMENT_2D_SHADER

    private val TEX_VERTEX_COMPONENT_COUNT = 2
    private val POSITION_COMPONENT_COUNT = 3

    private var uTextureUnitLocation: Int = 0
    private var mAPositionLocation: Int = 0

    private val texturePoint = floatArrayOf(
        0f, 0f,
        0f, 1f,
        1f, 1f,
        1f, 0f
    )
    private val BluerexVertexBuffer = KTGLHelperTools.createFloatBuffer(texturePoint)

    //x[-1,1]  y[-1,1]  L(-1)->R(1) T(1)->B(-1)
    private val bluerPoint = floatArrayOf(
        -1f, +1f, 0f,  //左上
        -1f, -1f, 0f,  //左下
        +1f, -1f, 0f,  //右下
        +1f, +1f, 0f //右上
    )
    private val bluerBuffer = KTGLHelperTools.createFloatBuffer(bluerPoint)


    private val mMVPMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    private var bluerData: KTBluerData? = null
    private var textureBean: KTTextureBean? = null

    //    private val

    private val mainProgramTarget = "${this::class.java.simpleName}_MAIN"

    private fun initMatrix() {

        lock.write {

            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
            val uMatrixLocation = getUniform(getProgram(mainProgramTarget), "u_Matrix")
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -1f, 1f, -1f, 1f)
            GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0)

        }

    }

    fun destory() {

    }

    override fun onDrawFrame(gl: GL10?) {

        initMatrix()

        lock.read {

            val data = bluerData ?: return

            val bean = createOrGetTexture(data)

            if (bean.textureId == 0) {
                return
            }

            bluerBuffer.position(0)
            GLES20.glVertexAttribPointer(
                mAPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, bluerBuffer
            )
            GLES20.glEnableVertexAttribArray(mAPositionLocation)

            // 设置当前活动的纹理单元为纹理单元0
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            // 将纹理ID绑定到当前活动的纹理单元上
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, bean.textureId)
            GLES20.glUniform1i(uTextureUnitLocation, 0)
            GLES20.glDrawArrays(
                GLES20.GL_TRIANGLE_FAN,
                0,
                bluerPoint.size / POSITION_COMPONENT_COUNT
            )

        }

    }

    private fun createOrGetTexture(data: KTBluerData): KTTextureBean {

        val temBean = textureBean ?: createTexture(data)

        if (null == textureBean) {
            textureBean = temBean
        }

        return temBean
    }

    private fun createTexture(data: KTBluerData): KTTextureBean {

        val bluerBitmap = KTBlurTools
            .Config
            .getInstance(context)
            .OriginalBtimap(data.originalBitmap)
            .blueWay(data.bluerBlueWay)
            .radius(data.radius)
            .scale(data.scale)
            .apply()

        return createTextureBean(bluerBitmap)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        createProgram(mainProgramTarget, blurVertexShaderCode, blurFragmentShaderCode)

        useTargetProgram(mainProgramTarget)

        mAPositionLocation = getAttrib(getProgram(mainProgramTarget), "a_Position")
//        mProjectionMatrixHelper = ProjectionMatrixHelper(program, "u_Matrix")
        // 纹理坐标索引
        val aTexCoordLocation = getAttrib(getProgram(mainProgramTarget), "a_TexCoord")
        uTextureUnitLocation = getUniform(getProgram(mainProgramTarget), "u_TextureUnit")

        // 加载纹理坐标
        BluerexVertexBuffer.position(0)
        GLES20.glVertexAttribPointer(
            aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, 0, BluerexVertexBuffer
        )

        GLES20.glEnableVertexAttribArray(aTexCoordLocation)

        GLES20.glClearColor(0f, 0f, 0f, 1f)
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)

    }


    class Builder internal constructor(val context: Context) {

        private var originBitmap: Bitmap? = null

        private var bluerWay: KTBlurTools.BlueWay = KTBlurTools.BlueWay.RenderScript

        private var scale = 0.75f

        private var radius = 10

        fun build(): KTBlurRender {

            val bitmap = originBitmap ?: throw RenderBuildException("origin bitmap null")

            if (bitmap.isRecycled)
                throw RenderBuildException("origin bitmap already recycled")

            val render = KTBlurRender(context)

            val bluerData = KTBluerData(bluerWay, bitmap, scale, radius)

            render.setBluerData(bluerData)

            return render
        }

        fun setOriginBitmap(init: Builder.() -> Bitmap): Builder = apply {
            originBitmap = init()
        }

        fun setBlurWay(init: Builder.() -> KTBlurTools.BlueWay): Builder = apply {
            bluerWay = init()
        }

        fun setScale(init: Builder.() -> Float): Builder = apply {
            scale = init()
        }

        fun setRadius(init: Builder.() -> Int): Builder = apply {
            radius = init()
        }


    }

    private fun setBluerData(bluerData: KTBluerData) {

        lock.write {
            this.bluerData = bluerData
        }
    }


    companion object {

        fun getBuilder(context: Context): Builder {
            return Builder(context)
        }
    }
}