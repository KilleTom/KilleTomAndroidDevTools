package com.ypz.killetom.basedevsdk.tools

import android.util.Log
import kotlin.experimental.and

fun String?.isNull(): Boolean {

    val message = this ?: return true
    if (message == "null" || message.isEmpty())
        return true

    return false
}

fun String.logD(logMessage: String) {
    Log.d(this, logMessage)
}

fun String.logD(logMessage: String, throwable: Throwable) {
    Log.d(this, logMessage, throwable)
}

fun String.logW(logMessage: String) {
    Log.w(this, logMessage)
}

fun String.logW(logMessage: String, throwable: Throwable) {
    Log.w(this, logMessage, throwable)
}

fun String.logI(logMessage: String) {
    Log.i(this, logMessage)
}

fun String.logI(logMessage: String, throwable: Throwable) {
    Log.i(this, logMessage, throwable)
}

fun String.logE(logMessage: String) {
    Log.e(this, logMessage)
}

fun String.logE(logMessage: String, throwable: Throwable) {
    Log.e(this, logMessage, throwable)
}

fun ByteArray?.transforString(offest: Int, length: Int, spliter: String = " "): String {

    val buffer = this?:return ""

    if (buffer.isEmpty()) {
        return ""
    }

    val max = offest + length
    val sb = StringBuffer()


    for (index in offest..max) {

        if (index >= this.size) {

            break
        } else {

            val data = Integer.toHexString((buffer[index].toInt() and 0xFF))

            if (data.length < 2) {
                sb.append(0)
            }

            sb.append(data)

            sb.append(spliter)

        }
    }

    return sb.toString()
}

