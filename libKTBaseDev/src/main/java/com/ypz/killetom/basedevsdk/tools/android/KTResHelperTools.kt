package com.ypz.killetom.basedevsdk.tools.android

import android.R
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.TypedValue
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat

object KTResHelperTools {

    lateinit var appContext: Context
    lateinit var resources: Resources

    fun init(appContext: Context) {
        KTResHelperTools.appContext = appContext
        resources = appContext.resources
    }

    /*---------------------获取字符串-------------------------------*/

    fun getResourceSting(@StringRes resId: Int): String {
        return getResourceSting(
            resId,
            resources
        )
    }

    fun getResourceSting(@StringRes resId: Int, resources: Resources): String {
        return resources.getString(resId)
    }

    /*----------------------获取dimens------------------------------*/

    fun getDimens(@DimenRes dimension: Int): Float {
        return getDimens(
            dimension,
            resources
        )
    }

    fun getDimens(@DimenRes dimension: Int, resources: Resources): Float {
        return resources.getDimension(dimension)
    }

    fun getDimenOffset(@DimenRes resId: Int): Int {
        return getDimenOffset(
            resId,
            resources
        )
    }

    fun getDimenOffset(@DimenRes resId: Int, resources: Resources): Int {
        return resources.getDimensionPixelOffset(resId)
    }

    fun getDimenPixel(@DimenRes resId: Int): Int {
        return getDimenPixel(
            resId,
            resources
        )
    }

    fun getDimenPixel(@DimenRes resId: Int, resources: Resources): Int {
        return resources.getDimensionPixelSize(resId)
    }

    /*--------------------获取color--------------------------------*/

    fun getColor(@ColorRes colorRes: Int): Int {
        return getColor(
            appContext,
            colorRes
        )
    }

    fun getColor(appContext: Context, @ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(KTResHelperTools.appContext, colorRes)
    }

    /*--------------------获取drawable--------------------------------*/

    fun getDrawable(@DrawableRes drawableRes: Int): Drawable {

        return getDrawable(
            appContext,
            drawableRes
        )
    }

    fun getDrawable(appContext: Context, @DrawableRes drawableRes: Int): Drawable {

        val drawable =
            ContextCompat.getDrawable(appContext, drawableRes)
                ?: appContext.resources.getDrawable(drawableRes)

        return drawable
    }


    /*--------------------屏幕单位转换--------------------------------*/

    /**
     * dpתPx
     */
    @JvmStatic
    fun dpToPx(dp: Float): Int {
        return dpToPx(
            dp,
            resources
        )
    }

    /**
     * dpתPx
     */
    @JvmStatic
    fun dpToPx(dp: Float, resources: Resources): Int {
        val px =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
        return px.toInt()
    }

    @JvmStatic
    fun spToPx(sp: Float): Int {
        return spToPx(
            sp,
            resources
        )
    }


    @JvmStatic
    fun spToPx(sp: Float, resources: Resources): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
            .toInt()
    }

    /*------------------------获取字体宽高----------------------------*/
    @JvmStatic
    fun getTextWidth(paint: TextPaint, str: String?): Float {
        return paint.measureText(str) //文字宽
    }

    @JvmStatic
    fun getTextHeighet(paint: TextPaint, str: String): Int {
        val rect = Rect()
        paint.getTextBounds(str, 0, str.length, rect)
        return rect.height() //文字高;//文字宽
    }

    /*---------------------获取控件的绝对左上角位置-------------------------------*/

    fun getRelativeTop(myView: View): Int { //	    if (myView.getParent() == myView.getRootView())
        return if (myView.id == R.id.content) myView.top else myView.top + getRelativeTop(
            myView.parent as View
        )
    }

    fun getRelativeLeft(myView: View): Int { //	    if (myView.getParent() == myView.getRootView())
        return if (myView.id == R.id.content) myView.left else myView.left + getRelativeLeft(
            myView.parent as View
        )
    }

    fun getAppName(): String {

        val appName =
            getResourceSting(
                appContext.applicationInfo.labelRes
            )

        return appName
    }
}