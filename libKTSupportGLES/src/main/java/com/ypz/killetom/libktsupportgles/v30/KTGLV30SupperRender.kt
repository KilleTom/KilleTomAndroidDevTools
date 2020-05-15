package com.ypz.killetom.libktsupportgles.v30

import android.graphics.Bitmap
import android.opengl.GLES30
import android.opengl.GLUtils
import android.util.Log
import com.ypz.killetom.libktsupportgles.KTTextureBean
import java.nio.ByteBuffer

abstract class KTGLV30SupperRender() : com.ypz.killetom.libktsupportgles.KTGLSupperRender() {

    private val shaderHelper by lazy { KTGLV30ShaderHelper.instant }

    override fun getShaderHelper(): com.ypz.killetom.libktsupportgles.KTGlShaderHelper {
        return shaderHelper
    }

    override fun useTargetProgram(target: String) {

        val program = programsMap[target]?.program ?: return

        GLES30.glUseProgram(program)

    }

    override fun readPixel(w: Int, h: Int): Bitmap {

        val buffer = ByteBuffer.allocate(w * h * 4)

        GLES30.glReadPixels(
            0, 0, w, h,
            GLES30.GL_RGBA,
            GLES30.GL_UNSIGNED_BYTE, buffer
        )

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        bitmap.copyPixelsFromBuffer(buffer)

        return bitmap
    }

    override fun createTextureBean(bitmap: Bitmap?): KTTextureBean {

        val bean = KTTextureBean()

        val textureObjectIds = IntArray(1)
        // 1. 创建纹理对象
        GLES30.glGenTextures(1, textureObjectIds, 0)


        if (null == bitmap) {
            if (isLog) {
                Log.w(this::class.java.simpleName, "bitmap was null")
                return bean
            }
        }

        if (bitmap!!.isRecycled) {
            if (isLog) {
                Log.w(this::class.java.simpleName, "bitmap already recycled")
            }
            // 加载Bitmap资源失败，删除纹理Id
            GLES30.glDeleteTextures(1, textureObjectIds, 0)
            return bean
        }
        // 2. 将纹理绑定到OpenGL对象上
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureObjectIds[0])

        // 3. 设置纹理过滤参数:解决纹理缩放过程中的锯齿问题。若不设置，则会导致纹理为黑色
        GLES30.glTexParameteri(
            GLES30.GL_TEXTURE_2D,
            GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_LINEAR_MIPMAP_LINEAR
        )
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT)
        // 4. 通过OpenGL对象读取Bitmap数据，并且绑定到纹理对象上，之后就可以回收Bitmap对象
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)

        // and mipmap generation will work.
        // 5. 生成Mip位图
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)

        bitmap.recycle()

        // 7. 将纹理从OpenGL对象上解绑
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0)

        // 所以整个流程中，OpenGL对象类似一个容器或者中间者的方式，将Bitmap数据转移到OpenGL纹理上
        bean.textureId = textureObjectIds[0]
        bean.textureObjectIds = textureObjectIds
        return bean
    }


}