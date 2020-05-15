package com.ypz.killetom.basedevsdk.tools.file

import android.os.Environment
import androidx.annotation.NonNull
import com.ypz.killetom.basedevsdk.tools.*
import com.ypz.killetom.basedevsdk.tools.android.KTResHelperTools
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class KTFileLogTools private constructor(
    filepath: String,
    fileName: String,
    private val logTag: String = fileName
) : EasySupperLogTools {

    private val logFile: File

    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS", Locale.CHINA)

    override fun getTag(): String {
        return logTag
    }

    init {

        logFile = File("$filepath${File.separator}$fileName")

        if (!KTFileTools.instant.createFileByDeleteOldFile(logFile)) {
            throw RuntimeException("File log create error ")
        }

    }

    fun logMessage(type: EasySupperLogType, message: String) {
        val tag = "${getTag()}${getLogTag(type)}"
        val time = System.currentTimeMillis()

        val data = format(time,tag,message)

        logMessage(type,data)

        logFile.appendText(data)
    }

    fun logText(byteArray: ByteArray, offset: Int, length: Int) {

        val data = String(byteArray, offset, length) + "\r\n"

        logMessage(EasySupperLogType.D,data)

        logFile.appendText(data)
    }

     fun log(tag: String, byteArray: ByteArray, offest: Int, length: Int) {
        val time = System.currentTimeMillis()

         val data = format(time,tag,byteArray.transforString(offest, length).toUpperCase(Locale.CHINA))

         logMessage(EasySupperLogType.D,data)

        logFile.appendText(data)
    }


    private fun format(time: Long, tag: String, content: String): String {
        return "${format.format(time)} $tag $content\r\n"
    }


    private fun getLogTag(type: EasySupperLogType): String {

        val typeMessage = when (type) {
            EasySupperLogType.I -> {
                "-I"
            }
            EasySupperLogType.W -> {
                "-w"
            }
            EasySupperLogType.D -> {
                "-D"
            }
            EasySupperLogType.E -> {
                "-E"
            }
        }

        return typeMessage
    }

    companion object {
        fun getCreateBuilder(): Builder {
            return Builder()
        }

    }

    class Builder() {

        private var filePath = (KTResHelperTools.appContext.getExternalFilesDir("")?.path
            ?: Environment.getExternalStorageDirectory().path) + File.separator + "log"

        private var fileName: String =
            "${System.currentTimeMillis()}-${KTResHelperTools.getAppName()}.txt"

        private var fileTag: String = ""
            get() {

                if (field.isEmpty())
                    return fileName

                return field
            }

        fun setFile(@NonNull filePath: String): Builder {

            this.filePath = filePath

            return this
        }

        fun setFileName(fileName: String): Builder {

            this.fileName = fileName

            return this
        }

        fun setFileLogTag(fileLogTag: String): Builder {

            this.fileTag = fileLogTag

            return this
        }

        fun builder(): KTFileLogTools {
            return KTFileLogTools(
                filePath,
                fileName,
                fileTag
            )
        }

    }
}