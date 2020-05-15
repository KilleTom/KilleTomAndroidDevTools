package com.ypz.killetom.basedevsdkui.ui.widget.opengl

import android.graphics.Bitmap
import com.ypz.killetom.basedevsdk.tools.KTBlurTools

data class KTBluerData(
    var bluerBlueWay: KTBlurTools.BlueWay,
    var originalBitmap: Bitmap,
    var scale: Float,
    var radius: Int) {


}