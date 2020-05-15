package com.ypz.killetom.basedevsdk.tools.android

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission


class KTVibrationTools private constructor(context: Context) {

    val vibrator: Vibrator

    init {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    }

    @RequiresPermission(android.Manifest.permission.VIBRATE)
    fun oneShot(vibratorTime: Long, vibratorCounts: Int) {

        cancleVibeate()

        if (Build.VERSION.SDK_INT >= 26) {

            var counts = Math.max(vibratorCounts,1)

            counts = Math.min(counts,255)

            val effect = VibrationEffect.createOneShot(vibratorTime, vibratorCounts)

            vibrator.vibrate(effect)

            return
        }

        vibrator.vibrate(vibratorTime)
    }

    @RequiresPermission(android.Manifest.permission.VIBRATE)
    fun vibrateComplicated( pattern: LongArray, repeate: Int) {

        cancleVibeate()

        if (Build.VERSION.SDK_INT >= 26) {

            val effect = VibrationEffect.createWaveform(pattern, repeate)

            vibrator.vibrate(effect)

            return
        }

        vibrator.vibrate(pattern, repeate)
    }

    @RequiresPermission(android.Manifest.permission.VIBRATE)
    fun cancleVibeate(){
        vibrator.cancel()
    }


    companion object {


        val instance by lazy {
            KTVibrationTools(
                KTResHelperTools.appContext
            )
        }
    }
}