package com.ypz.killetom.libktsupportgles

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

object KTGLHelperTools {

    /**
     * Float类型占4Byte
     */
    val BYTES_PER_FLOAT = 4
    /**
     * Short类型占2Byte
     */
    val BYTES_PER_SHORT = 2

    /**
     * 创建一个FloatBuffer
     */
    fun createFloatBuffer(array: FloatArray): FloatBuffer {
        val buffer = ByteBuffer
            // 分配顶点坐标分量个数 * Float占的Byte位数
            .allocateDirect(array.size * BYTES_PER_FLOAT)
            // 按照本地字节序排序
            .order(ByteOrder.nativeOrder())
            // Byte类型转Float类型
            .asFloatBuffer()

        // 将Dalvik的内存数据复制到Native内存中
        buffer.put(array)
        return buffer
    }

    /**
     * 创建一个FloatBuffer
     */
    fun createShortBuffer(array: ShortArray): ShortBuffer {
        val buffer = ByteBuffer
            // 分配顶点坐标分量个数 * Float占的Byte位数
            .allocateDirect(array.size * BYTES_PER_SHORT)
            // 按照本地字节序排序
            .order(ByteOrder.nativeOrder())
            // Byte类型转Float类型
            .asShortBuffer()

        // 将Dalvik的内存数据复制到Native内存中
        buffer.put(array)
        return buffer
    }

    fun init(
        glSurfaceView: GLSurfaceView,
        renderer: GLSurfaceView.Renderer,
        renderMode: Int = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    ) {

        val context = glSurfaceView.context

        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val info = activityManager.deviceConfigurationInfo

        val version = info.reqGlEsVersion

        if (version >= 0x30000) {
            initV30(glSurfaceView, renderer, renderMode)
        } else {
            initV20(glSurfaceView, renderer, renderMode)
        }

    }

    fun initV20(
        glSurfaceView: GLSurfaceView,
        renderer: GLSurfaceView.Renderer,
        renderMode: Int = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    ) {

        glSurfaceView.setEGLContextClientVersion(2)
        glSurfaceView.setRenderer(renderer)
        glSurfaceView.renderMode = renderMode
    }

    fun initV30(
        glSurfaceView: GLSurfaceView,
        renderer: GLSurfaceView.Renderer,
        renderMode: Int = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    ) {

        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setRenderer(renderer)
        glSurfaceView.renderMode = renderMode
    }
}