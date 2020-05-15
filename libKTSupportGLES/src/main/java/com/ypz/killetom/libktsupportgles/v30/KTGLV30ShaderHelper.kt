package com.ypz.killetom.libktsupportgles.v30

import android.opengl.GLES30
import android.util.Log

open class KTGLV30ShaderHelper private constructor() :
    com.ypz.killetom.libktsupportgles.KTGlShaderHelper {

    override var openLog: Boolean = false
        get() = field

    override fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GLES30.GL_VERTEX_SHADER, shaderCode)
    }

    /**
     * 编译片段着色器
     *
     * @param type       着色器类型
     * @param shaderCode 编译代码
     * @return 着色器对象ID
     */
    private fun compileShader(type: Int, shaderCode: String): Int {
        // 1.创建一个新的着色器对象
        val shaderObjectId = GLES30.glCreateShader(type)

        // 2.获取创建状态
        if (shaderObjectId == 0) {
            // 在OpenGL中，都是通过整型值去作为OpenGL对象的引用。之后进行操作的时候都是将这个整型值传回给OpenGL进行操作。
            // 返回值0代表着创建对象失败。
            if (openLog) {
                Log.w(getTAG(), "Could not create new shader.")
            }
            return 0
        }

        // 3.将着色器代码上传到着色器对象中
        GLES30.glShaderSource(shaderObjectId, shaderCode)

        // 4.编译着色器对象
        GLES30.glCompileShader(shaderObjectId)

        // 5.获取编译状态：OpenGL将想要获取的值放入长度为1的数组的首位
        val compileStatus = IntArray(1)

        GLES30.glGetShaderiv(shaderObjectId, GLES30.GL_COMPILE_STATUS, compileStatus, 0)

        if (openLog) {
            // 打印编译的着色器信息
            Log.v(
                getTAG(), "Results of compiling source:" + "\n" + shaderCode + "\n:"
                        + GLES30.glGetShaderInfoLog(shaderObjectId)
            )
        }

        // 6.验证编译状态
        if (compileStatus[0] == 0) {
            // 如果编译失败，则删除创建的着色器对象
            GLES30.glDeleteShader(shaderObjectId)

            if (openLog) {
                Log.w(getTAG(), "Compilation of shader failed.")
            }

            // 7.返回着色器对象：失败，为0
            return 0
        }

        // 7.返回着色器对象：成功，非0
        return shaderObjectId
    }

    override fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GLES30.GL_FRAGMENT_SHADER, shaderCode)
    }

    override fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {


        // 1.创建一个OpenGL程序对象
        val programObjectId = GLES30.glCreateProgram()

        // 2.获取创建状态
        if (programObjectId == 0) {
            if (openLog) {
                Log.w(getTAG(), "Could not create new program")
            }
            return 0
        }

        // 3.将顶点着色器依附到OpenGL程序对象
        GLES30.glAttachShader(programObjectId, vertexShaderId)
        // 3.将片段着色器依附到OpenGL程序对象
        GLES30.glAttachShader(programObjectId, fragmentShaderId)

        // 4.将两个着色器链接到OpenGL程序对象
        GLES30.glLinkProgram(programObjectId)

        // 5.获取链接状态：OpenGL将想要获取的值放入长度为1的数组的首位
        val linkStatus = IntArray(1)
        GLES30.glGetProgramiv(programObjectId, GLES30.GL_LINK_STATUS, linkStatus, 0)

        if (openLog) {
            // 打印链接信息
            Log.v(
                getTAG(),
                "Results of linking program:\n" + GLES30.glGetProgramInfoLog(programObjectId)
            )
        }

        // 6.验证链接状态
        if (linkStatus[0] == 0) {
            // 链接失败则删除程序对象
            GLES30.glDeleteProgram(programObjectId)
            if (openLog) {
                Log.w(getTAG(), "Linking of program failed.")
            }
            // 7.返回程序对象：失败，为0
            return 0
        }

        // 7.返回程序对象：成功，非0
        return programObjectId
    }

    override fun validateProgram(programObjectId: Int): Boolean {
        GLES30.glValidateProgram(programObjectId)

        val validateStatus = IntArray(1)
        GLES30.glGetProgramiv(programObjectId, GLES30.GL_VALIDATE_STATUS, validateStatus, 0)
        if (openLog) {
            Log.v(
                getTAG(), "Results of validating program: " + validateStatus[0]
                        + "\nLog:" + GLES30.glGetProgramInfoLog(programObjectId)
            )
        }

        return validateStatus[0] != 0
    }

    override fun glGetUniformLocation(program: Int, name: String): Int {
        return GLES30.glGetUniformLocation(program, name)
    }

    override fun glGetAttribLocation(program: Int, name: String): Int {
        return GLES30.glGetAttribLocation(program, name)
    }

    companion object {
        val instant by lazy { KTGLV30ShaderHelper() }
        val newInstant by lazy { KTGLV30ShaderHelper() }
    }
}