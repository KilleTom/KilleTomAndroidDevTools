package com.ypz.killetom.libktsupportgles

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import javax.microedition.khronos.opengles.GL10

abstract class KTGLSupperRender : GLSurfaceView.Renderer {

    protected var programsMap = HashMap<String, KTProgramData>()

    protected var outputWidth: Int = 0
    protected var outputHeight: Int = 0

    protected var isLog = BuildConfig.DEBUG

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        outputWidth = width
        outputHeight = height
    }

    protected fun getProgram(target: String): Int {
        return programsMap[target]?.program ?: 0
    }

    protected open fun getUniform(program: Int, name: String): Int {
        return getShaderHelper().glGetUniformLocation(program, name)
    }

    protected fun getAttrib(program: Int, name: String): Int {
        return getShaderHelper().glGetAttribLocation(program, name)
    }

    protected abstract fun getShaderHelper(): com.ypz.killetom.libktsupportgles.KTGlShaderHelper

    protected open fun createProgram(target: String, vertexShader: String, fragmentShader: String) {
        // 步骤1：编译顶点着色器
        val vertexShaderId = getShaderHelper().compileVertexShader(vertexShader)
        // 步骤2：编译片段着色器
        val fragmentShaderId = getShaderHelper().compileFragmentShader(fragmentShader)
        // 步骤3：将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
        val program = getShaderHelper().linkProgram(vertexShaderId, fragmentShaderId)

        if (isLog) {
            getShaderHelper().validateProgram(program)
        }

        val data =
            KTProgramData(vertexShader, fragmentShader, vertexShaderId, fragmentShaderId, program)


        programsMap.put(target, data)
    }

    protected abstract fun useTargetProgram(target: String)

    protected abstract fun readPixel(w: Int = outputWidth, h: Int = outputHeight): Bitmap

    protected abstract fun createTextureBean(bitmap: Bitmap?): KTTextureBean

}