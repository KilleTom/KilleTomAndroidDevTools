package com.ypz.killetom.basedevsdk.tools

import com.ypz.killetom.basedevsdk.BuildConfig

interface EasySupperLogTools {

    fun getTag(): String {
        return this::class.java.simpleName
    }

    fun inputLog(logType: EasySupperLogType = EasySupperLogType.D, message: String) {

        if (!logStatus())
            return

        val tag = getTag()

        when (logType) {

            EasySupperLogType.I -> {
                tag.logI(message)
            }
            EasySupperLogType.W -> {
                tag.logW(message)
            }
            EasySupperLogType.D -> {
                tag.logD(message)
            }
            EasySupperLogType.E -> {
                tag.logE(message)
            }
        }
    }

    fun inputLog(
        logType: EasySupperLogType = EasySupperLogType.D,
        message: String,
        throwable: Throwable
    ) {

        val tag = getTag()

        when (logType) {

            EasySupperLogType.I -> {
                tag.logI(message, throwable)
            }
            EasySupperLogType.W -> {
                tag.logW(message, throwable)
            }
            EasySupperLogType.D -> {
                tag.logD(message, throwable)
            }
            EasySupperLogType.E -> {
                tag.logE(message, throwable)
            }
        }
    }

    fun logStatus(): Boolean {
        return BuildConfig.DEBUG
    }

}

enum class EasySupperLogType {
    I, W, D, E
}