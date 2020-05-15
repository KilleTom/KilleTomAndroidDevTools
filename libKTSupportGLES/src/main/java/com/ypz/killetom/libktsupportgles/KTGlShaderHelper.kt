package com.ypz.killetom.libktsupportgles

interface KTGlShaderHelper {

    fun getTAG(): String {
        return this::class.java.simpleName
    }

    var openLog: Boolean

    /**
     * 编译顶点着色器
     *
     * @param shaderCode 编译代码
     * @return 着色器对象ID
     */
    fun compileVertexShader(shaderCode: String): Int

    /**
     * 编译片段着色器
     *
     * @param shaderCode 编译代码
     * @return 着色器对象ID
     */
    fun compileFragmentShader(shaderCode: String): Int


    /**
     * 创建OpenGL程序：通过链接顶点着色器、片段着色器
     *
     * @param vertexShaderId   顶点着色器ID
     * @param fragmentShaderId 片段着色器ID
     * @return OpenGL程序ID
     */
    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int

    /**
     * 验证OpenGL程序对象状态
     *
     * @param programObjectId OpenGL程序ID
     * @return 是否可用
     */
    fun validateProgram(programObjectId: Int): Boolean

    fun glGetUniformLocation(program: Int, name: String): Int

    fun glGetAttribLocation(program: Int, name: String): Int
}