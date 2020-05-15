package com.ypz.killetom.basedevsdk.constant

import java.util.concurrent.TimeUnit

object TimeRadioConstant {

    private val radio = 60

    val MILLISECOND = 1L

    val SECOND by lazy { 1000L  }

    val MINUTE by lazy { radio * SECOND }

    val HOUR by lazy { radio * MINUTE }

    val DAY by lazy { 24 * HOUR }
}