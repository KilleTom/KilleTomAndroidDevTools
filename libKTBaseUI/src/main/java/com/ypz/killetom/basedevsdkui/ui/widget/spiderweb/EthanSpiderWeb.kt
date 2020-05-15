package com.ypz.killetom.basedevsdkui.ui.widget.spiderweb

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import com.ypz.killetom.basedevsdkui.R
import com.ypz.killetom.basedevsdkui.ui.widget.base.BaseView

class EthanSpiderWeb @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : BaseView(context, attrs) {
    /**
     * @value spiderWebPaint 蜘蛛网线条画笔
     * @value spiderBgPaint 蜘蛛网背景颜色画笔
     * @value scoreBgPaint 分数背景颜色画笔
     * @value scoreStrokePaint 分数线条画笔
     * @value path 图形
     * @value scores 用户每一个评分点的具体数值分数列表
     * @value angleCount 整个蛛网有几个角
     * @value hierarchyCount 整个蛛网分多少层（例如最大分数是10分，分5层，那么每层就代表2分）
     * @value maxScore 每一个分数顶点代表分数值
     * @value averageAngle 平均角度
     * @value offsetAngle 偏移角度
     * @value spiderColor 蜘蛛网背景颜色当设置为变色模式则为起始颜色
     * @value scoreColor 分数图形的颜色
     * @value spiderStrokeColor  蛛网线条的颜色
     * @value scoreStrokeColor 分数图形描边的颜色
     * @value spiderEndColor 蜘蛛网变色的结束颜色
     * @value spiderStrokeWidth 蛛网线条的宽度
     * @value scoreStrokeWidth  分数图形描边的宽度
     * @value isScoreStroke  是否分数图形的描边
     * @value isSpiderStroke 是否蜘蛛网状层次描边
     * @value isSpiderBg  是否设置蜘蛛网背景
     * @value isScoreBg 是否设置分数网背景
     * @value isComplexOffset 是否设置复数边形的角度偏移量保证复数边形不会以一定角度旋转偏移默认不设置效果会好一点
     * @value gradientSpiderColors 每一层变色后的颜色值
     * @value radius 整个蛛网图的半径
     * @value centerX 蛛网圆点的X轴坐标
     * @value centerY 蛛网圆点的Y轴坐标
     */
    private var spiderWebPaint: Paint? = null
    private var spiderBgPaint: Paint? = null
    private var scoreBgPaint: Paint? = null
    private var scoreStrokePaint: Paint? = null
    private val path = Path()
    private var scores: FloatArray? = null
    private var angleCount = 0
    private var hierarchyCount = 0
    private var maxScore = 0f
    private var averageAngle = 0f
    private var offsetAngle = 0f
    private var spiderColor = 0
    private var scoreColor = 0
    private var spiderStrokeColor = 0
    private var scoreStrokeColor = 0
    private var spiderEndColor = 0
    private var spiderStrokeWidth = 0f
    private var scoreStrokeWidth = 0f
    private var isScoreStroke = false
    private var isSpiderStroke = false
    private var isSpiderBg = false
    private var isScoreBg = false
    private var isComplexOffset = false
    private var isGradientSpider = false
    private lateinit var gradientSpiderColors: IntArray
    private var radius = 0f
    private var centerX = 0f
    private var centerY = 0f
    override fun initAttr(
        context: Context,
        attrs: AttributeSet?
    ) {
        if (attrs != null) {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.EthanSpiderWeb)
            isComplexOffset = typedArray.getBoolean(
                R.styleable.EthanSpiderWeb_isComplexOffset,
                RxSpiderConstant.isComplexOffset
            )
            isSpiderBg = typedArray.getBoolean(
                R.styleable.EthanSpiderWeb_isSpiderBg,
                RxSpiderConstant.isSpiderBg
            )
            isSpiderStroke = typedArray.getBoolean(
                R.styleable.EthanSpiderWeb_isSpiderStroke,
                RxSpiderConstant.isSpiderStroke
            )
            isScoreBg = typedArray.getBoolean(
                R.styleable.EthanSpiderWeb_isScoreBg,
                RxSpiderConstant.isScoreBg
            )
            isScoreStroke = typedArray.getBoolean(
                R.styleable.EthanSpiderWeb_isScoreStroke,
                RxSpiderConstant.isScoreStroke
            )
            setAngleCount(
                typedArray.getInt(
                    R.styleable.EthanSpiderWeb_angleCount,
                    RxSpiderConstant.angleCount
                )
            )
            setHierarchyCount(
                typedArray.getInt(
                    R.styleable.EthanSpiderWeb_hierarchyCount,
                    RxSpiderConstant.hierarchyCount
                )
            )
            setMaxScore(typedArray.getFloat(R.styleable.EthanSpiderWeb_maxScore, maxScore))
            if (isSpiderBg) {
                spiderColor = typedArray.getResourceId(
                    R.styleable.EthanSpiderWeb_spiderBg,
                    RxSpiderConstant.spiderColor
                )
                setSpiderColor(spiderColor)
            }
            isGradientSpider = typedArray.getBoolean(
                R.styleable.EthanSpiderWeb_isGradientSpider,
                RxSpiderConstant.isGradientSpider
            )
            if (isGradientSpider) {
                spiderColor = typedArray.getResourceId(
                    R.styleable.EthanSpiderWeb_spiderBg,
                    RxSpiderConstant.spiderColor
                )
                spiderEndColor = typedArray.getResourceId(
                    R.styleable.EthanSpiderWeb_spiderEndBg,
                    RxSpiderConstant.spiderEndColor
                )
                resetGradientSpiderColors()
            }
            if (isSpiderStroke) {
                setSpiderStrokeWidth(
                    typedArray.getDimension(
                        R.styleable.EthanSpiderWeb_spiderStrokeWidth,
                        10f
                    )
                )
                setSpiderStrokeColor(
                    typedArray.getResourceId(
                        R.styleable.EthanSpiderWeb_spiderStokeBg,
                        RxSpiderConstant.spiderStrokeColor
                    )
                )
            }
            if (isScoreBg) setScoreColor(
                typedArray.getResourceId(
                    R.styleable.EthanSpiderWeb_scoreBg,
                    RxSpiderConstant.scoreColor
                )
            )
            if (isScoreStroke) {
                setScoreStrokeWidth(
                    typedArray.getDimension(
                        R.styleable.EthanSpiderWeb_spiderStrokeWidth,
                        10f
                    )
                )
                setScoreStrokeColor(
                    typedArray.getResourceId(
                        R.styleable.EthanSpiderWeb_scoreStokeBg,
                        RxSpiderConstant.scoreStrokeColor
                    )
                )
            }
            typedArray.recycle()
        } else {
            resetScoreStrokePaint()
            resetSpiderStrokePaint()
            resetSpiderBgPaint()
            resetScoreBgPaint()
        }
    }

    private fun resetPaint(
        paint: Paint?,
        style: Paint.Style
    ): Paint {
        var paint = paint
        if (paint == null) paint = Paint()
        paint.style = style
        paint.isAntiAlias = true
        return paint
    }

    private fun resetSpiderBgPaint() {
        spiderBgPaint = resetPaint(spiderBgPaint, Paint.Style.FILL)
        paintSetColor(spiderBgPaint, spiderColor)
    }

    private fun resetSpiderStrokePaint() {
        spiderWebPaint = resetPaint(spiderWebPaint, Paint.Style.STROKE)
        paintSetColor(spiderWebPaint, spiderStrokeColor)
        paintSetStroke(spiderWebPaint, spiderStrokeWidth)
    }

    private fun resetScoreStrokePaint() {
        scoreStrokePaint = resetPaint(scoreStrokePaint, Paint.Style.STROKE)
        paintSetColor(scoreStrokePaint, scoreStrokeColor)
        paintSetStroke(scoreStrokePaint, scoreStrokeWidth)
    }

    private fun resetScoreBgPaint() {
        scoreBgPaint = resetPaint(scoreBgPaint, Paint.Style.FILL)
        paintSetColor(scoreBgPaint, scoreColor)
    }

    private fun resetGradientSpiderColors() {
        try {
            gradientSpiderColors = IntArray(hierarchyCount)
            gradientSpiderColors[hierarchyCount - 1] = resources.getColor(spiderEndColor)
            gradientSpiderColors[0] = resources.getColor(spiderColor)
            val startRed = gradientSpiderColors[0] and 0xff0000 shr 16
            val startGreen = gradientSpiderColors[0] and 0x00ff00 shr 8
            val startBlue = gradientSpiderColors[0] and 0x0000ff
            val endRed = gradientSpiderColors[hierarchyCount - 1] and 0xff0000 shr 16
            val endGreen = gradientSpiderColors[hierarchyCount - 1] and 0x00ff00 shr 8
            val endBlue = gradientSpiderColors[hierarchyCount - 1] and 0x0000ff
            val nextRed = (endRed - startRed) / hierarchyCount
            val nextGreen = (endGreen - startGreen) / hierarchyCount
            val nextBlue = (endBlue - startBlue) / hierarchyCount
            for (i in 0 until hierarchyCount) {
                if (!(i == 0 || i == hierarchyCount - 1)) gradientSpiderColors[i] =
                    Color.argb(
                        255,
                        startRed + i * nextRed,
                        startGreen + i * nextGreen,
                        startBlue + i * nextBlue
                    )
            }
        } catch (e: NotFoundException) {
            Log.i("ypz", "error" + e.message)
        }
        invalidate()
    }

    fun setScoreStroke(scoreStroke: Boolean) {
        isScoreStroke = scoreStroke
        if (isScoreStroke) {
            if (scoreStrokePaint == null) {
                resetScoreStrokePaint()
            }
        }
        postInvalidate()
    }

    fun setSpiderStroke(spiderStroke: Boolean) {
        isSpiderStroke = spiderStroke
        if (isSpiderStroke) {
            if (spiderWebPaint == null) {
                resetSpiderStrokePaint()
            }
        }
        postInvalidate()
    }

    fun setSpiderBg(spiderBg: Boolean) {
        isSpiderBg = spiderBg
        if (isSpiderBg) {
            if (spiderBgPaint == null) resetSpiderBgPaint()
        }
        postInvalidate()
    }

    fun setScoreBg(scoreBg: Boolean) {
        isScoreBg = scoreBg
        if (isScoreBg) {
            if (scoreBgPaint == null) resetScoreBgPaint()
        }
        postInvalidate()
    }

    fun setComplexOffset(complexOffset: Boolean) {
        if (isComplexOffset == complexOffset) return
        isComplexOffset = complexOffset
        offsetAngle = if (isComplexOffset) averageAngle / 2 else 0f
        postInvalidate()
    }

    fun setGradientSpider(gradientSpider: Boolean) {
        isGradientSpider = gradientSpider

        if (isGradientSpider) {

            if (spiderEndColor == 0) {

                setSpiderEndColor(RxSpiderConstant.spiderEndColor)
            } else {

                setSpiderEndColor(spiderEndColor)
            }
        }
    }

    fun setScores(scores: FloatArray?) {
        if (scores == null) throw NullPointerException("分数集合不能为null")
        val length = scores.size
        if (length > angleCount) {
            setAngleCount(scores.size)
            this.scores = scores
        } else if (length < angleCount) {
            this.scores = FloatArray(angleCount)
            for (i in this.scores!!.indices) {
                if (i < length) this.scores!![i] = scores[i] else scores[i] = 0F
            }
        } else this.scores = scores
        Log.i("ypzZZ", "finish")
        invalidate()
    }

    /**
     * @param angleCount 设置多边形
     */
    fun setAngleCount(angleCount: Int) {
        require(angleCount > 2) { "最小的多边形为三角形所以角的总数不能小于2" }

        if (angleCount == this.angleCount)
            return

        this.angleCount = angleCount
        averageAngle = 360 / angleCount.toFloat()
        offsetAngle = if (isComplexOffset) averageAngle / 2 else 0f
        invalidate()
    }

    fun getAngleCount(): Int {
        return angleCount
    }

    fun setHierarchyCount(hierarchyCount: Int) {
        require(hierarchyCount > 0) { "蜘蛛网状层必须大于0" }
        if (this.hierarchyCount == hierarchyCount) return
        this.hierarchyCount = hierarchyCount
        if (isGradientSpider) resetGradientSpiderColors()
        postInvalidate()
    }

    fun setMaxScore(maxScore: Float) {
        require(maxScore > 0) { "分数最大值必须是大于等于0对于一些负数操作你可以转化为绝对数进行操作" }
        if (this.maxScore == maxScore) return
        this.maxScore = maxScore
        postInvalidate()
    }

    fun setSpiderColor(spiderColor: Int) {

        if (this.spiderColor == spiderColor)
            return

        this.spiderColor = spiderColor
        setSpiderBg(true)
        paintSetColor(spiderBgPaint, spiderColor)
        if (isGradientSpider) resetGradientSpiderColors()
        postInvalidate()
    }

    fun setSpiderEndColor(spiderEndColor: Int) {

        if (this.spiderEndColor == spiderEndColor)
            return

        this.spiderEndColor = spiderEndColor
        isGradientSpider = true
        resetGradientSpiderColors()
    }

    fun setScoreColor(scoreColor: Int) {

        if (scoreColor == this.scoreColor)
            return

        this.scoreColor = scoreColor

        setScoreBg(true)

        resetScoreBgPaint()

        paintSetColor(scoreBgPaint, scoreColor)

        postInvalidate()
    }

    fun setSpiderStrokeColor(spiderStrokeColor: Int) {

        if (this.spiderStrokeColor == spiderStrokeColor) {
            return
        }

        this.spiderStrokeColor = spiderStrokeColor
        setSpiderStroke(true)
        paintSetColor(spiderWebPaint, spiderStrokeColor)
        postInvalidate()
    }

    fun setScoreStrokeColor(scoreStrokeColor: Int) {

        if (this.scoreStrokeColor == scoreStrokeColor)
            return

        this.scoreStrokeColor = scoreStrokeColor
        setScoreStroke(true)
        paintSetColor(scoreStrokePaint, scoreStrokeColor)
        postInvalidate()
    }

    fun setSpiderStrokeWidth(spiderStrokeWidth: Float) {

        if (spiderStrokeWidth == this.spiderStrokeWidth)
            return

        this.spiderStrokeWidth = spiderStrokeWidth

        if (spiderWebPaint == null) {

            resetSpiderStrokePaint()
        } else {

            spiderWebPaint!!.strokeWidth = spiderStrokeWidth
        }

        postInvalidate()
    }

    fun setScoreStrokeWidth(scoreStrokeWidth: Float) {
        this.scoreStrokeWidth = scoreStrokeWidth
        if (scoreStrokePaint == null) resetScoreStrokePaint() else scoreStrokePaint!!.strokeWidth =
            scoreStrokeWidth
        postInvalidate()
    }

    protected fun paintSetColor(paint: Paint?, colorId: Int) {
        var paint = paint
        if (paint != null) paint.color = colorId else {
            paint = Paint()
            paint.isAntiAlias = true
            paint.color = colorId
        }
    }

    protected fun paintSetStroke(
        paint: Paint?,
        strokeWidth: Float
    ) {
        if (paint != null && strokeWidth > 0) {
            paint.strokeWidth = scoreStrokeWidth
        }
    }

    private fun reset() {
        if (angleCount >= 3 && hierarchyCount >= 1) {
            centerX = width / 2.toFloat()
            centerY = height / 2.toFloat()
            radius = Math.min(width, height) / 2 - 20.toFloat()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (isSetDimen(widthMeasureSpec)) {
            widthMeasureSpec = 100
        }
        if (isSetDimen(heightMeasureSpec)) {
            widthMeasureSpec = 100
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        reset()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        reset()
    }

    override fun onDraw(canvas: Canvas) {

        val basePath = computeBgSpriderPath()

        if (basePath.isEmpty())
            return

        //绘制背景
        if (isSpiderBg && !isGradientSpider) {
            canvas.drawPath(basePath[0], spiderBgPaint!!)
        } else {

            //绘制渐变色背景
            basePath.forEachIndexed { index, data ->
                paintSetColor(spiderBgPaint, gradientSpiderColors[basePath.size - index])
                canvas.drawPath(data, spiderBgPaint!!)
            }

        }

        //这里循环绘制边界线
        //在循环一遍的目的在于避免绘制渐变色的时候会覆盖边界线
        if (!isSpiderStroke && !isSpiderBg || isSpiderStroke) {

            drawSpiderStoke(canvas, basePath)
        }

        drawScore(canvas)
    }

    private fun computeBgSpriderPath(): ArrayList<Path> {

        val paths = ArrayList<Path>()
        val averageRadius = radius / hierarchyCount

        for (index in hierarchyCount downTo 1) {

            val radius = averageRadius * index

            paths.add(hierarchyByRadiusPath(radius))

        }

        return paths
    }

    /**
     * 绘制蜘蛛网中心点到最外层形状的的背景path
     */
    protected fun drawSpiderBg(canvas: Canvas) {
        canvas.drawPath(hierarchyByRadiusPath(radius), spiderBgPaint!!)
    }

    /**
     * 绘制渐变的蜘蛛网 注意是从最外层到最内层间的绘制避免画笔颜色会被覆盖
     */
    protected fun drawGradientSpider(canvas: Canvas) {
        val averageRadius = radius / hierarchyCount
        for (i in hierarchyCount - 1 downTo 0) {
            try {
                paintSetColor(spiderBgPaint, gradientSpiderColors[i])
                canvas.drawPath(hierarchyByRadiusPath(averageRadius * (i + 1)), spiderBgPaint!!)
            } catch (e: Exception) {
                Log.i("ypzZZ", e.message)
            }
        }
    }

    /**
     * 绘制描边蜘蛛网每一层的形状Path
     * 最后绘制每一个顶点到圆心的路径
     */
    protected fun drawSpiderStoke(
        canvas: Canvas,
        basePath: ArrayList<Path>) {

        basePath.forEach { path -> canvas.drawPath(path, spiderWebPaint!!) }

        for (position in 0 until angleCount) {

            val angle = offsetAngle + position * averageAngle

            val radians = Math.toRadians(angle.toDouble()).toFloat()

            val pointX =
                (centerX + Math.sin(radians.toDouble()) * radius).toFloat()

            val pointY =
                (centerY - Math.cos(radians.toDouble()) * radius).toFloat()

            canvas.drawLine(centerX, centerY, pointX, pointY, spiderWebPaint!!)
        }
    }

    /**
     * 绘制分数图形
     */
    protected fun drawScore(canvas: Canvas) {
        if (scores == null || scores!!.isEmpty()) return
        path.reset()
        Log.i("ypzZZ", "drawScore")
        var nextAngle: Float
        var nextRadians: Float
        var nextPointX: Float
        var nextPointY: Float
        var currentRadius: Float
        for (position in 0 until angleCount) {
            currentRadius = scores!![position] / maxScore * radius
            nextAngle = offsetAngle + position * averageAngle
            nextRadians = Math.toRadians(nextAngle.toDouble()).toFloat()
            nextPointX =
                (centerX + Math.sin(nextRadians.toDouble()) * currentRadius).toFloat()
            nextPointY =
                (centerY - Math.cos(nextRadians.toDouble()) * currentRadius).toFloat()
            if (position == 0) path.moveTo(nextPointX, nextPointY) else path.lineTo(
                nextPointX,
                nextPointY
            )
        }
        path.close()
        canvas.drawPath(path, scoreBgPaint!!)
        // 绘制描边
        if (isScoreStroke) canvas.drawPath(path, scoreStrokePaint!!)
    }

    /**
     * 返回每一层蜘蛛网的path路径
     */
    private fun hierarchyByRadiusPath(currentRadius: Float): Path {
        path.reset()
        var nextAngle: Float
        var nextRadians: Float
        var nextPointX: Float
        var nextPointY: Float
        for (position in 0 until angleCount) {
            nextAngle = offsetAngle + position * averageAngle
            nextRadians = Math.toRadians(nextAngle.toDouble()).toFloat()
            nextPointX =
                (centerX + Math.sin(nextRadians.toDouble()) * currentRadius).toFloat()
            nextPointY =
                (centerY - Math.cos(nextRadians.toDouble()) * currentRadius).toFloat()
            if (position == 0) path.moveTo(nextPointX, nextPointY) else path.lineTo(
                nextPointX,
                nextPointY
            )
        }
        path.close()
        return path
    }
}