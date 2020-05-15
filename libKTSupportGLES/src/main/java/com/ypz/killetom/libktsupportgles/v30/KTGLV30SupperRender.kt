package com.ypz.killetom.libktsupportgles.v30

import android.graphics.Bitmap
import android.opengl.GLES30
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


}