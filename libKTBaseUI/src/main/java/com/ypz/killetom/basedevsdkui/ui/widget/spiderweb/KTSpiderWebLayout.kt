package com.ypz.killetom.basedevsdkui.ui.widget.spiderweb

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.ypz.killetom.basedevsdk.tools.android.KTResHelperTools.dpToPx
import com.ypz.killetom.basedevsdkui.R
import com.ypz.killetom.basedevsdkui.ui.widget.base.BaseViewGroup
import kotlin.math.cos
import kotlin.math.sin

class KTSpiderWebLayout : BaseViewGroup {
    private var spiderWeb: KTSpiderWeb? = null

    var isBuildIn = false

    private var spiderWebMargin = 0f
    private var isComplexOffset = false
    private var centerX = 0f // 中心点X坐标 = 0f
    private var centerY = 0f// 中心点Y坐标 = 0f
    private var radius = 0f// 半径 = 0f
    private var measureSpec = 0f
    private var childSpiderCount = 0
    private var spacing = dpToPx(8f).toFloat()


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun initAttr(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.KTSpiderWebLayout)
            isBuildIn = typedArray.getBoolean(R.styleable.KTSpiderWebLayout_isBuildInSpider, false)
            isComplexOffset = typedArray.getBoolean(
                R.styleable.KTSpiderWebLayout_isComplexOffset,
                KTSpiderConstant.isComplexOffset
            )
            //判断是否需要自带蜘蛛网生成
            if (isBuildIn) {
                if (spiderWeb == null) spiderWeb = KTSpiderWeb(context)
                Log.i(
                    "ypz",
                    typedArray.getInt(
                        R.styleable.KTSpiderWebLayout_angleCount,
                        KTSpiderConstant.angleCount
                    ).toString() + ""
                )
                spiderWeb!!.setAngleCount(
                    typedArray.getInt(
                        R.styleable.KTSpiderWebLayout_angleCount,
                        KTSpiderConstant.angleCount
                    )
                )
                spiderWeb!!.setHierarchyCount(
                    typedArray.getInt(
                        R.styleable.KTSpiderWebLayout_hierarchyCount,
                        KTSpiderConstant.hierarchyCount
                    )
                )
                spiderWeb!!.setMaxScore(
                    typedArray.getFloat(
                        R.styleable.KTSpiderWebLayout_maxScore,
                        KTSpiderConstant.maxScore
                    )
                )
                spiderWeb!!.setComplexOffset(isComplexOffset)
                spiderWebMargin = typedArray.getDimension(
                    R.styleable.KTSpiderWebLayout_spiderMargin,
                    dpToPx(25f, resources).toFloat()
                )
                //判读是否存在设置蛛网背景 默认存在
                if (typedArray.getBoolean(
                        R.styleable.KTSpiderWebLayout_isSpiderBg,
                        KTSpiderConstant.isSpiderBg
                    )
                ) {
                    spiderWeb!!.setSpiderBg(true)
                    spiderWeb!!.setSpiderColor(
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWebLayout_spiderBg,
                            KTSpiderConstant.spiderColor
                        )
                    )
                } else spiderWeb!!.setSpiderBg(false)
                //判断是否存在蛛网层次绘画 默认不存在
                if (typedArray.getBoolean(
                        R.styleable.KTSpiderWebLayout_isSpiderStroke,
                        KTSpiderConstant.isSpiderStroke
                    )
                ) {
                    spiderWeb!!.setSpiderStroke(true)
                    spiderWeb!!.setSpiderStrokeWidth(
                        typedArray.getDimension(
                            R.styleable.KTSpiderWebLayout_spiderStrokeWidth,
                            dpToPx(1f, resources).toFloat()
                        )
                    )
                    spiderWeb!!.setSpiderStrokeColor(
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWebLayout_spiderStokeBg,
                            KTSpiderConstant.spiderStrokeColor
                        )
                    )
                } else spiderWeb!!.setSpiderStroke(false)
                //判断是否存在分数绘画背景 默认存在
                if (typedArray.getBoolean(
                        R.styleable.KTSpiderWebLayout_isScoreBg,
                        KTSpiderConstant.isScoreBg
                    )
                ) {
                    spiderWeb!!.setScoreBg(true)
                    spiderWeb!!.setScoreColor(
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWebLayout_scoreBg,
                            R.color.steelblue
                        )
                    )
                } else spiderWeb!!.setScoreBg(false)
                //判断是否存在分数绘画描线 默认不存在
                if (typedArray.getBoolean(
                        R.styleable.KTSpiderWebLayout_isScoreStroke,
                        KTSpiderConstant.isScoreStroke
                    )
                ) {
                    spiderWeb!!.setScoreStroke(true)
                    spiderWeb!!.setScoreStrokeWidth(
                        typedArray.getDimension(
                            R.styleable.KTSpiderWebLayout_spiderStrokeWidth,
                            dpToPx(1f, resources).toFloat()
                        )
                    )
                    spiderWeb!!.setScoreStrokeColor(
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWebLayout_scoreStokeBg,
                            KTSpiderConstant.spiderStrokeColor
                        )
                    )
                } else spiderWeb!!.setScoreStroke(false)
                if (typedArray.getBoolean(
                        R.styleable.KTSpiderWebLayout_isGradientSpider,
                        KTSpiderConstant.isGradientSpider
                    )
                ) {
                    spiderWeb!!.setGradientSpider(true)
                    spiderWeb!!.setSpiderEndColor(
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWebLayout_spiderEndBg,
                            KTSpiderConstant.spiderEndColor
                        )
                    )
                }
                addView(spiderWeb)
                reset()
            }
            typedArray.recycle()
        } else Log.i("ypz", "!!!!!!layout")
    }

    private fun reset() {
        val viewWidth = width
        val viewHeight = height
        centerX = viewWidth / 2.toFloat()
        centerY = viewHeight / 2.toFloat()
        radius = viewWidth.coerceAtMost(viewHeight) / 2.toFloat()
        if (isBuildIn) {
            measureSpec = radius.toInt() * 2.toFloat()
            measureSpec -= spiderWebMargin * 2
            spiderWeb!!.measure(measureSpec.toInt(), measureSpec.toInt())
        }
    }

    override fun addView(child: View, index: Int, params: LayoutParams) {
        super.addView(child, index, params)
        childSpiderCount = childCount
        reset()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childSpiderCount == 0) return
        // 循环处理每个位置的子View，先计算出子View在圆上所处的位置，然后按照其子View的宽高偏移一定的距离，保证子View全部在圆圈之外，并且子View的中心点和其圆上的点连同圆心在一条直线上
        var childView: View
        var nextAngle: Float
        var nextRadians: Float
        var nextPointX: Float
        var nextPointY: Float
        var childViewMeasuredWidth: Int
        var childViewMeasuredHeight: Int
        var childViewLeft: Float
        var childViewTop: Float
        val averageAngle: Float =
            if (isBuildIn && spiderWeb != null)
                if (childSpiderCount > 1)
                    (360 / (childSpiderCount - 1)).toFloat()
                else 0.toFloat()
            else if (childSpiderCount > 0)
                (360 / childSpiderCount).toFloat()
            else
                0.toFloat()

        var offsetAngle = 0f
        if (isComplexOffset) offsetAngle = averageAngle / 2
        for (position in 0 until childSpiderCount) {
            childView = getChildAt(position)
            childViewMeasuredWidth = childView.measuredWidth
            childViewMeasuredHeight = childView.measuredHeight
            if (childView === spiderWeb) {
                childViewLeft = centerX - measureSpec / 2
                childViewTop = centerY - measureSpec / 2
                childView.layout(
                    childViewLeft.toInt(),
                    childViewTop.toInt(),
                    (childViewLeft + measureSpec).toInt(),
                    (childViewTop + measureSpec).toInt()
                )
            } else {
                nextAngle = offsetAngle + position * averageAngle
                nextRadians = Math.toRadians(nextAngle.toDouble()).toFloat()
                if (isBuildIn) {
                    val builderRadius = radius - spiderWebMargin / 8 * 7
                    nextPointX =
                        (centerX + sin(nextRadians.toDouble()) * builderRadius).toFloat()
                    nextPointY =
                        (centerY - cos(nextRadians.toDouble()) * builderRadius).toFloat()
                    spacing /= 8f
                } else {
                    nextPointX =
                        (centerX + sin(nextRadians.toDouble()) * radius).toFloat()
                    nextPointY =
                        (centerY - cos(nextRadians.toDouble()) * radius).toFloat()
                }
                childViewLeft = nextPointX
                childViewTop = nextPointY
                when (calculateLocationByAngle(nextAngle)) {
                    KTSpiderConstant.LOCATION_NORTH -> {
                        childViewLeft -= childViewMeasuredWidth / 2.toFloat()
                        childViewTop -= childViewMeasuredHeight.toFloat()
                        childViewTop -= spacing
                    }
                    KTSpiderConstant.LOCATION_EAST_NORTH -> {
                        childViewTop -= childViewMeasuredHeight / 2.toFloat()
                        childViewLeft += spacing
                    }
                    KTSpiderConstant.LOCATION_EAST -> {
                        childViewTop -= childViewMeasuredHeight / 2.toFloat()
                        childViewLeft += spacing
                    }
                    KTSpiderConstant.LOCATION_EAST_SOUTH -> {
                        childViewLeft += spacing
                        childViewTop += spacing
                    }
                    KTSpiderConstant.LOCATION_SOUTH -> {
                        childViewLeft -= childViewMeasuredWidth / 2.toFloat()
                        childViewTop += spacing
                    }
                    KTSpiderConstant.LOCATION_WEST_SOUTH -> {
                        childViewLeft -= childViewMeasuredWidth.toFloat()
                        childViewLeft -= spacing
                        childViewTop += spacing
                    }
                    KTSpiderConstant.LOCATION_WEST -> {
                        childViewLeft -= childViewMeasuredWidth.toFloat()
                        childViewTop -= childViewMeasuredHeight / 2.toFloat()
                        childViewLeft -= spacing
                    }
                    KTSpiderConstant.LOCATION_WEST_NORTH -> {
                        childViewLeft -= childViewMeasuredWidth.toFloat()
                        childViewTop -= childViewMeasuredHeight / 2.toFloat()
                        childViewLeft -= spacing
                        childViewTop -= spacing
                    }
                }
                childView.layout(
                    childViewLeft.toInt(),
                    childViewTop.toInt(),
                    (childViewLeft + childViewMeasuredWidth).toInt(),
                    (childViewTop + childViewMeasuredHeight).toInt()
                )
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        reset()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var measuredWidth = widthMeasureSpec
        var measuredHeight = heightMeasureSpec
        measureChildren(measuredWidth, measuredHeight)
        super.onMeasure(measuredWidth, measuredHeight)
        if (!isSetDimen(measuredWidth)) {
            measuredWidth = dpToPx(150f, resources)
        }
        if (!isSetDimen(measuredHeight)) {
            measuredHeight = dpToPx(150f, resources)
        }
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    fun setBuildInScores(scores: FloatArray?) {
        Log.i("ypzZZ", "scores")

        val buildScores = scores ?: return

        if (buildScores.isEmpty())
            return

        if (isBuildIn && spiderWeb != null) {
            spiderWeb!!.setScores(buildScores)
        }
    }

    /**
     * 根据角度判断所处的方位，一个圆分成了8个方位（东、南、西、北、西北、东北、西南、东南），不同的方位有不同的偏移方式
     *
     * @param angle 角度
     * @return 方位
     */
    private fun calculateLocationByAngle(angle: Float): Int {

        //一个圆形理论角度范围是[0.360] 取余是保证在[0,360]范围内
        val angle = angle % 360

        return if (angle in 337.5f..360f || angle in 0f..22.5f) {
            KTSpiderConstant.LOCATION_NORTH
        } else if (angle in 22.5f..67.5f) {
            KTSpiderConstant.LOCATION_EAST_NORTH
        } else if (angle in 67.5f..112.5f) {
            KTSpiderConstant.LOCATION_EAST
        } else if (angle in 112.5f..157.5f) {
            KTSpiderConstant.LOCATION_EAST_SOUTH
        } else if (angle in 157.5..202.5) {
            KTSpiderConstant.LOCATION_SOUTH
        } else if (angle in 202.5..247.5) {
            KTSpiderConstant.LOCATION_WEST_SOUTH
        } else if (angle in 247.5..292.5) {
            KTSpiderConstant.LOCATION_WEST
        } else if (angle in 292.5..337.5) {
            KTSpiderConstant.LOCATION_WEST_NORTH
        } else {
            throw IllegalArgumentException("error angle $angle")
        }
    }
}