package com.ypz.killetom.basedevsdkui.ui.widget.opengl.v30

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.ypz.killetom.basedevsdk.tools.KTBlurTools
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.KTBluerData
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.KTBluerSupportRender
import com.ypz.killetom.basedevsdkui.ui.widget.opengl.RenderBuildException
import com.ypz.killetom.libktsupportgles.KTGLHelperTools
import com.ypz.killetom.libktsupportgles.KTGLShareProgramCode
import com.ypz.killetom.libktsupportgles.KTTextureBean
import com.ypz.killetom.libktsupportgles.v30.KTGLV30SupperRender
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.concurrent.read
import kotlin.concurrent.write

class KTV30BlurRender internal constructor(val context: Context) : KTBluerSupportRender(context) {


    private fun initMatrix() {

        lock.write {

            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)
            val uMatrixLocation = getUniform(getProgram(mainProgramTarget), "u_Matrix")
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -1f, 1f, -1f, 1f)
            GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0)

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
            GLES30.glVertexAttribPointer(
                mAPositionLocation, POSITION_COMPONENT_COUNT,
                GLES30.GL_FLOAT, false, 0, bluerBuffer
            )
            GLES30.glEnableVertexAttribArray(mAPositionLocation)

            // 设置当前活动的纹理单元为纹理单元0
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
            // 将纹理ID绑定到当前活动的纹理单元上
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, bean.textureId)
            GLES30.glUniform1i(uTextureUnitLocation, 0)
            GLES30.glDrawArrays(
                GLES30.GL_TRIANGLE_FAN,
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
        GLES30.glVertexAttribPointer(
            aTexCoordLocation, TEX_VERTEX_COMPONENT_COUNT, GLES30.GL_FLOAT,
            false, 0, BluerexVertexBuffer
        )

        GLES30.glEnableVertexAttribArray(aTexCoordLocation)


        GLES30.glClearColor(255f, 255f, 255f, 0f)
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_ONE, GLES30.GL_ONE_MINUS_SRC_ALPHA)

    }


    companion object {

        fun getBuilder(context: Context): KTBluerSupportRender.Builder<KTV30BlurRender> {

            val builder = object : Builder<KTV30BlurRender>(context) {

                override fun buildByData(bluerData: KTBluerData): KTV30BlurRender {

                    return KTV30BlurRender(context).apply {
                        applyBluerData(bluerData)
                    }
                }
            }

            return builder
        }
    }
}