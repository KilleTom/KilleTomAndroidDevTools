package com.ypz.killetom.basedevsdkui.ui.widget.spiderweb

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.ContextCompat
import com.ypz.killetom.basedevsdkui.R
import com.ypz.killetom.basedevsdkui.ui.widget.base.BaseView
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.math.cos
import kotlin.math.sin

class KTSpiderWeb @JvmOverloads constructor(
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
    private var spiderStrokePaint: Paint? = null
    private var spiderBgPaint: Paint? = null
    private var scoreBgPaint: Paint? = null
    private var scoreStrokePaint: Paint? = null

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

    private lateinit var lock: ReentrantReadWriteLock


    private lateinit var drawData: DrawData

    override fun initAttr(context: Context, attrs: AttributeSet?) {

        lock = ReentrantReadWriteLock()

        lock.write {

            drawData = DrawData()

            if (attrs != null) {

                val typedArray =
                    context.obtainStyledAttributes(attrs, R.styleable.KTSpiderWeb)

                isComplexOffset =
                    typedArray.getBoolean(
                        R.styleable.KTSpiderWeb_isComplexOffset,
                        KTSpiderConstant.isComplexOffset
                    )

                isSpiderBg =
                    typedArray.getBoolean(
                        R.styleable.KTSpiderWeb_isSpiderBg,
                        KTSpiderConstant.isSpiderBg
                    )

                isSpiderStroke =
                    typedArray.getBoolean(
                        R.styleable.KTSpiderWeb_isSpiderStroke,
                        KTSpiderConstant.isSpiderStroke
                    )

                isScoreBg =
                    typedArray.getBoolean(
                        R.styleable.KTSpiderWeb_isScoreBg,
                        KTSpiderConstant.isScoreBg
                    )

                isScoreStroke =
                    typedArray.getBoolean(
                        R.styleable.KTSpiderWeb_isScoreStroke,
                        KTSpiderConstant.isScoreStroke
                    )

                angleCount =
                    typedArray.getInt(
                        R.styleable.KTSpiderWeb_angleCount,
                        KTSpiderConstant.angleCount
                    )

                hierarchyCount =
                    typedArray.getInt(
                        R.styleable.KTSpiderWeb_hierarchyCount,
                        KTSpiderConstant.hierarchyCount
                    )

                maxScore = typedArray.getFloat(R.styleable.KTSpiderWeb_maxScore, maxScore)

                if (isSpiderBg) {

                    spiderColor =
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWeb_spiderBg,
                            KTSpiderConstant.spiderColor
                        )

                }

                isGradientSpider =
                    typedArray.getBoolean(
                        R.styleable.KTSpiderWeb_isGradientSpider,
                        KTSpiderConstant.isGradientSpider
                    )

                if (isGradientSpider) {
                    spiderColor =
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWeb_spiderBg,
                            KTSpiderConstant.spiderColor
                        )

                    spiderEndColor =
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWeb_spiderEndBg,
                            KTSpiderConstant.spiderEndColor
                        )

                    resetGradientSpiderColors()
                }

                if (isSpiderStroke) {

                    spiderStrokeWidth =
                        typedArray.getDimension(R.styleable.KTSpiderWeb_spiderStrokeWidth, 10f)

                    spiderStrokeColor =
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWeb_spiderStokeBg,
                            KTSpiderConstant.spiderStrokeColor
                        )

                }
                if (isScoreBg) {
                    scoreColor =
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWeb_scoreBg,
                            KTSpiderConstant.scoreColor
                        )
                }

                if (isScoreStroke) {

                    scoreStrokeWidth =
                        typedArray.getDimension(R.styleable.KTSpiderWeb_spiderStrokeWidth, 10f)

                    scoreStrokeColor =
                        typedArray.getResourceId(
                            R.styleable.KTSpiderWeb_scoreStokeBg,
                            KTSpiderConstant.scoreStrokeColor
                        )

                }

                typedArray.recycle()
            }

            resetScoreStrokePaint()
            resetSpiderStrokePaint()
            resetSpiderBgPaint()
            resetScoreBgPaint()

            resetAngle()

            initDrawData()
        }
    }

    //--------------------------初始化渲染元素的设置例如画笔线框背景等等---------------------------------

    private fun resetPaint(paint: Paint?, style: Paint.Style): Paint {

        lock.write {

            val initPaint = paint ?: Paint()

            initPaint.style = style
            initPaint.isAntiAlias = true

            return initPaint
        }
    }

    private fun resetSpiderBgPaint() {
        lock.write {
            spiderBgPaint = resetPaint(spiderBgPaint, Paint.Style.FILL)
            paintSetColor(spiderBgPaint, spiderColor)
        }
    }

    private fun resetSpiderStrokePaint() {
        lock.write {
            spiderStrokePaint = resetPaint(spiderStrokePaint, Paint.Style.STROKE)
            paintSetColor(spiderStrokePaint, spiderStrokeColor)
            paintSetStroke(spiderStrokePaint, spiderStrokeWidth)
        }
    }

    private fun resetScoreStrokePaint() {
        lock.write {
            scoreStrokePaint = resetPaint(scoreStrokePaint, Paint.Style.STROKE)
            paintSetColor(scoreStrokePaint, scoreStrokeColor)
            paintSetStroke(scoreStrokePaint, scoreStrokeWidth)
        }
    }

    private fun resetScoreBgPaint() {
        lock.write {
            scoreBgPaint = resetPaint(scoreBgPaint, Paint.Style.FILL)
            paintSetColor(scoreBgPaint, scoreColor)
        }
    }

    private fun resetGradientSpiderColors() {

        lock.write {

            try {

                gradientSpiderColors = IntArray(hierarchyCount)
                gradientSpiderColors[hierarchyCount - 1] =
                    ContextCompat.getColor(context, spiderEndColor)
                gradientSpiderColors[0] = ContextCompat.getColor(context, spiderColor)
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
        }

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
            if (spiderStrokePaint == null) {
                resetSpiderStrokePaint()
            }
        }
        postInvalidate()
    }

    fun setSpiderBg(spiderBg: Boolean) {

        lock.write {

            if (isSpiderBg == spiderBg)
                return

            isSpiderBg = spiderBg

            if (isSpiderBg) {
                if (spiderBgPaint == null) resetSpiderBgPaint()
            }
        }

        postInvalidate()
    }

    fun setScoreBg(scoreBg: Boolean) {

        lock.write {
            isScoreBg = scoreBg
            if (isScoreBg) {
                if (scoreBgPaint == null) resetScoreBgPaint()
            }
        }

        postInvalidate()
    }

    fun setGradientSpider(gradientSpider: Boolean) {
        lock.write {

            if (isGradientSpider == gradientSpider)
                return

            isGradientSpider = gradientSpider

            if (isGradientSpider) {

                if (spiderEndColor == 0) {
                    setSpiderEndColor(KTSpiderConstant.spiderEndColor)
                } else {
                    setSpiderEndColor(spiderEndColor)
                }
            }
        }
    }

    fun setSpiderColor(spiderColor: Int) {

        if (this.spiderColor == spiderColor)
            return

        this.spiderColor = spiderColor

        setSpiderBg(true)
        paintSetColor(spiderBgPaint, spiderColor)

        if (isGradientSpider)
            resetGradientSpiderColors()

        postInvalidate()
    }

    fun setSpiderEndColor(spiderEndColor: Int) {

        lock.write {
            if (this.spiderEndColor == spiderEndColor)
                return

            this.spiderEndColor = spiderEndColor

            isGradientSpider = true

            resetGradientSpiderColors()
        }

    }

    fun setScoreColor(scoreColor: Int) {

        lock.write {
            if (scoreColor == this.scoreColor)
                return

            this.scoreColor = scoreColor

            setScoreBg(true)

            resetScoreBgPaint()

            paintSetColor(scoreBgPaint, scoreColor)
        }

        postInvalidate()
    }

    fun setSpiderStrokeColor(spiderStrokeColor: Int) {

        lock.write {
            if (this.spiderStrokeColor == spiderStrokeColor) {
                return
            }

            this.spiderStrokeColor = spiderStrokeColor
            setSpiderStroke(true)
            paintSetColor(spiderStrokePaint, spiderStrokeColor)
        }

        postInvalidate()
    }

    fun setScoreStrokeColor(scoreStrokeColor: Int) {

        lock.write {
            if (this.scoreStrokeColor == scoreStrokeColor)
                return

            this.scoreStrokeColor = scoreStrokeColor
            setScoreStroke(true)
            paintSetColor(scoreStrokePaint, scoreStrokeColor)
        }

        postInvalidate()
    }

    fun setSpiderStrokeWidth(spiderStrokeWidth: Float) {

        lock.write {
            if (spiderStrokeWidth == this.spiderStrokeWidth)
                return

            this.spiderStrokeWidth = spiderStrokeWidth

            if (spiderStrokePaint == null) {
                resetSpiderStrokePaint()
            } else {
                spiderStrokePaint!!.strokeWidth = spiderStrokeWidth
            }

        }

        postInvalidate()
    }

    fun setScoreStrokeWidth(scoreStrokeWidth: Float) {

        lock.write {

            if (this.scoreStrokeWidth == scoreStrokeWidth)
                return

            this.scoreStrokeWidth = scoreStrokeWidth
            if (scoreStrokePaint == null) resetScoreStrokePaint() else scoreStrokePaint!!.strokeWidth =
                scoreStrokeWidth
        }

        postInvalidate()
    }

    protected fun paintSetColor(paint: Paint?, colorId: Int) {

        val initPaint = paint ?: return
        initPaint.color = colorId
    }

    protected fun paintSetStroke(paint: Paint?, strokeWidth: Float) {
        if (paint != null && strokeWidth > 0) {
            paint.strokeWidth = scoreStrokeWidth
        }
    }

    //------------------------初始化绘制渲染相关Path数据-------------------------------------------------------

    fun setComplexOffset(complexOffset: Boolean) {
        lock.write {

            if (isComplexOffset == complexOffset) return
            isComplexOffset = complexOffset
            offsetAngle = if (isComplexOffset) averageAngle / 2 else 0f

            initDrawData()
        }

        postInvalidate()
    }

    fun setScores(scores: FloatArray?) {

        lock.write {

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

        }

        postInvalidate()
    }

    /**
     * @param angleCount 设置多边形
     */
    fun setAngleCount(angleCount: Int) {

        lock.write {

            require(angleCount > 2) { "最小的多边形为三角形所以角的总数不能小于2" }

            if (angleCount == this.angleCount)
                return

            this.angleCount = angleCount

            resetAngle()

            initDrawData()

        }
        invalidate()
    }

    private fun resetAngle() {

        averageAngle = 360 / angleCount.toFloat()

        offsetAngle = if (isComplexOffset) averageAngle / 2 else 0f
    }

    fun getAngleCount(): Int {
        return angleCount
    }

    fun setHierarchyCount(hierarchyCount: Int) {

        lock.write {

            require(hierarchyCount > 0) { "蜘蛛网状层必须大于0" }

            if (this.hierarchyCount == hierarchyCount) return

            this.hierarchyCount = hierarchyCount

            if (isGradientSpider) resetGradientSpiderColors()

            initDrawData()
        }

        postInvalidate()
    }

    fun setMaxScore(maxScore: Float) {

        lock.write {

            require(maxScore > 0) { "分数最大值必须是大于等于0对于一些负数操作你可以转化为绝对数进行操作" }

            if (this.maxScore == maxScore) return

            this.maxScore = maxScore

            initDrawData()
        }

        postInvalidate()
    }

    private fun initDrawData() {

        lock.write {

            val bgPath = getBgPath()
            val strokeLinePath = getStrokePath()

            drawData.bgPath.clear()
            drawData.strokePath.clear()
            drawData.scorePath = getScorePath()

            drawData.strokePath = strokeLinePath
            drawData.bgPath = bgPath
        }

    }

    //-------------------------------因为布局发生改变动态初始化下DrawData-----------------------------------------

    private fun reset() {
        lock.write {

            if (angleCount >= 3 && hierarchyCount >= 1) {
                centerX = width / 2.toFloat()
                centerY = height / 2.toFloat()
                radius = Math.min(width, height) / 2 - 20.toFloat()
            }

            initDrawData()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var measuredWidth = widthMeasureSpec
        super.onMeasure(measuredWidth, heightMeasureSpec)
        if (isSetDimen(measuredWidth)) {
            measuredWidth = 100
        }
        if (isSetDimen(heightMeasureSpec)) {
            measuredWidth = 100
        }
        setMeasuredDimension(measuredWidth, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        reset()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        reset()
    }

    override fun onDraw(canvas: Canvas) {

        lock.read {

            if (drawData.bgPath.isEmpty()) {
                return
            }

            val isDrawStorke = (!isSpiderStroke && !isSpiderBg || isSpiderStroke)

            //绘制背景
            if (isSpiderBg && !isGradientSpider) {

                drawSpiderBg(canvas)
            } else {

                drawGradientSpider(canvas)
            }

            if (isDrawStorke) {
                drawSpiderStoke(canvas)
            }

            drawScore(canvas)
        }
    }

    private fun getStrokePath(): ArrayList<Path> {

        val paths = ArrayList<Path>()

        val averageRadius = radius / hierarchyCount

        for (index in hierarchyCount downTo 1) {

            val radius = averageRadius * index

            paths.add(getRadiusWebPath(radius))

        }

        val strokeTopPath = Path()

        for (position in 0 until angleCount) {

            val angle = offsetAngle + position * averageAngle

            val radians = Math.toRadians(angle.toDouble()).toFloat()

            val pointX =
                (centerX + Math.sin(radians.toDouble()) * radius).toFloat()

            val pointY =
                (centerY - Math.cos(radians.toDouble()) * radius).toFloat()

            strokeTopPath.moveTo(centerX, centerY)

            strokeTopPath.lineTo(pointX, pointY)
        }

        strokeTopPath.close()

        paths.add(strokeTopPath)

        return paths
    }

    private fun getBgPath(): ArrayList<Path> {

        val paths = ArrayList<Path>()
        val averageRadius = radius / hierarchyCount

        for (index in hierarchyCount downTo 1) {

            val radius = averageRadius * index

            val nextRadius = averageRadius * (index - 1)

            paths.add(getRadiusRanaglePath(radius, nextRadius))

        }

        return paths
    }

    private fun getScorePath(): Path {
        lock.write {

            val path = Path()

            if (scores == null || scores!!.isEmpty()) {
                path.close()
                return path
            }

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

            return path
        }
    }

    private fun getRadiusWebPath(currentRadius: Float): Path {

        val path = Path()

        for (position in 0 until angleCount * 2) {
            val nextAngle = offsetAngle + position * averageAngle
            val nextRadians = Math.toRadians(nextAngle.toDouble()).toFloat()
            val nextPointX = (centerX + Math.sin(nextRadians.toDouble()) * currentRadius).toFloat()
            val nextPointY =
                (centerY - Math.cos(nextRadians.toDouble()) * currentRadius).toFloat()

            if (position == 0 || position == angleCount) {
                path.moveTo(nextPointX, nextPointY)
            } else {
                path.lineTo(nextPointX, nextPointY)
            }
        }

        path.close()

        return path
    }

    /**
     * 返回每一层蜘蛛网的path路径
     */
    private fun getRadiusRanaglePath(currentRadius: Float, nextRadius: Float): Path {

        val path = Path()

        for (position in 0 until angleCount * 2) {

            var radius = currentRadius

            if (position == angleCount) {
                radius = nextRadius
            }

            val nextAngle = offsetAngle + position * averageAngle
            val nextRadians = Math.toRadians(nextAngle.toDouble()).toFloat()
            val nextPointX = (centerX + sin(nextRadians.toDouble()) * radius).toFloat()
            val nextPointY = (centerY - cos(nextRadians.toDouble()) * radius).toFloat()

            if (position == 0 || position == angleCount) {
                path.moveTo(nextPointX, nextPointY)
            } else {
                path.lineTo(nextPointX, nextPointY)
            }

        }

        path.close()

        return path
    }

    /**
     * 绘制蜘蛛网中心点到最外层形状的的背景path
     */
    protected fun drawSpiderBg(canvas: Canvas) {

        lock.read {

            if (drawData.bgPath.isEmpty()) {
                return
            }

            canvas.drawPath(drawData.bgPath[0], spiderBgPaint!!)
        }
    }

    /**
     * 绘制渐变的蜘蛛网 注意是从最外层到最内层间的绘制避免画笔颜色会被覆盖
     */
    protected fun drawGradientSpider(canvas: Canvas) {

        lock.read {

            val paths = drawData.bgPath

            if (paths.isEmpty())
                return

            val lastIndex = paths.lastIndex

            paths.forEachIndexed { index, path ->
                val drawIndex = lastIndex - index
                paintSetColor(spiderBgPaint, gradientSpiderColors[drawIndex])
                canvas.drawPath(path, spiderBgPaint!!)
            }

        }
    }

    /**
     * 绘制描边蜘蛛网每一层的形状Path
     * 最后绘制每一个顶点到圆心的路径
     */
    protected fun drawSpiderStoke(canvas: Canvas) {

        lock.read {

            val paint = spiderStrokePaint ?: return

            drawData.strokePath.forEach { path -> canvas.drawPath(path, paint) }

        }
    }

    /**
     * 绘制分数图形
     */
    protected fun drawScore(canvas: Canvas) {
        lock.read {
            if (scores == null || scores!!.isEmpty()) return

            val path = drawData.scorePath
            canvas.drawPath(path, scoreBgPaint!!)
            // 绘制描边
            if (isScoreStroke) canvas.drawPath(path, scoreStrokePaint!!)
        }
    }


    private class DrawData() {

        var scorePath: Path = Path()
        var strokePath = ArrayList<Path>()
        var bgPath = ArrayList<Path>()
    }
}