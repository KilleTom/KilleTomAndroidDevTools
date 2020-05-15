package com.ypz.killetom.libktsupportgles

data class KTProgramData(
    val vertexShaderCode: String,
    val fragmentShaderCode: String,
    val vertexShaderId: Int,
    val fragmentShaderId: Int,
    val program: Int
)