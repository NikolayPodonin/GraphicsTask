package com.podonin.xygraph.presentation.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.podonin.points_count_api.domain.entity.XYPoint
import com.podonin.xygraph.R
import kotlin.math.abs

/**
 * Вью для отображения графика
 */
class XYGraphView : View {

    private val axisPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private val gridPaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    private val pointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val linePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    private val points = mutableListOf<XYPoint>()
    private var isSmooth = false
    private var isCentered = true

    private var padding = 0f
    private var minX = 0f
    private var maxX = 0f
    private var minY = 0f
    private var maxY = 0f
    private var scaleX = 0f
    private var scaleY = 0f
    private var zeroX = 0f
    private var zeroY = 0f
    private var stepX = 0f
    private var stepY = 0f
    private val mappedPoints = mutableListOf<XYPoint>()
    private val linePath = Path()

    private var scaleFactor = 1f
    private val scaleGestureDetector by lazy {
        ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor

                scaleFactor = scaleFactor.coerceIn(0.5f, 3f)

                invalidate()
                return true
            }
        })
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.XYGraphView, defStyle, 0)

        axisPaint.color = a.getColor(R.styleable.XYGraphView_axisColor, axisPaint.color)
        gridPaint.color = a.getColor(R.styleable.XYGraphView_gridColor, gridPaint.color)
        linePaint.color = a.getColor(R.styleable.XYGraphView_lineColor, linePaint.color)
        pointPaint.color = a.getColor(R.styleable.XYGraphView_pointColor, pointPaint.color)
        val defPadding = context.resources.getDimensionPixelSize(R.dimen.graph_view_padding)
        padding = a.getDimensionPixelSize(R.styleable.XYGraphView_graphPadding, defPadding).toFloat()
        isCentered = a.getBoolean(R.styleable.XYGraphView_isCentered, true)

        a.recycle()
    }

    fun setPoints(newPoints: List<XYPoint>) {
        if (newPoints.isEmpty()) return

        points.clear()
        points.addAll(newPoints)
        minX = points.minOf { it.x }
        maxX = points.maxOf { it.x }
        minY = points.minOf { it.y }
        maxY = points.maxOf { it.y }
        recalculateSizes()

        invalidate()
    }

    fun setIsSmooth(isSmooth: Boolean) {
        this.isSmooth = isSmooth
        invalidate()
    }

    fun switchIsSmooth() {
        isSmooth = !isSmooth
        invalidate()
    }

    fun saveGraphToBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        draw(canvas)

        return bitmap
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.pointerCount > 1) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return scaleGestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        recalculateSizes()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val size: Int = when {
            widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST -> widthSize
            heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST -> heightSize
            else -> context.resources.getDimensionPixelSize(R.dimen.graph_view_min_size) // Размер по умолчанию, если нет ограничений
        }

        val squareSize = minOf(widthSize, size)

        setMeasuredDimension(squareSize, squareSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        if (points.isEmpty()) return


        canvas.save()
        canvas.scale(scaleFactor, scaleFactor, width / 2f, height / 2f)

        canvas.drawLine(padding, zeroY, width - padding, zeroY, axisPaint) // X
        canvas.drawLine(zeroX, padding, zeroX, height - padding, axisPaint) // Y

        canvas.drawAxisLabels(minX, maxX, stepX, zeroY, zeroX, scaleX, isXAxis = true)
        canvas.drawAxisLabels(minY, maxY, stepY, zeroX, zeroY, scaleY, isXAxis = false)

        if (isSmooth) {
            canvas.drawSmoothLine()
        } else {
            canvas.drawStrictLine()
        }

        mappedPoints.forEach { point ->
            canvas.drawCircle(point.x, point.y, 8f, pointPaint)
        }

        canvas.restore()
    }

    private fun recalculateSizes() {
        if (isCentered || points.size == 1 || maxX == minX || maxY == minY) {
            recalculateCentered()
        } else {
            recalculateUncentered()
        }
    }

    private fun recalculateCentered() {
        val rangeX = abs(maxX - minX)
        val rangeY = abs(maxY - minY)
        val max = maxOf(abs(maxX) * 2, abs(maxY) * 2, rangeX, rangeY)
        scaleX = (width - 2 * padding) / max
        scaleY = (height - 2 * padding) / max
        zeroX = width / 2f
        zeroY = height / 2f
        stepX = calculateStep(if (rangeX != 0f) rangeX else 1f)
        stepY = calculateStep(if (rangeY != 0f) rangeY else 1f)

        mappedPoints.clear()
        mappedPoints.addAll(
            points.map { point ->
                val x = zeroX + point.x * scaleX
                val y = zeroY - point.y * scaleY
                XYPoint(point.index, x, y)
            }
        )
    }

    private fun recalculateUncentered() {
        scaleX = (width - 2 * padding) / (maxX - minX)
        scaleY = (height - 2 * padding) / (maxY - minY)
        zeroX = if (minX < 0) {
            padding + (-minX / (maxX - minX)) * (width - 2 * padding)
        } else {
            padding
        }
        zeroY = if (minY < 0) {
            height - padding - (-minY / (maxY - minY)) * (height - 2 * padding)
        } else {
            height - padding
        }
        stepX = calculateStep(maxX - minX)
        stepY = calculateStep(maxY - minY)

        mappedPoints.clear()
        mappedPoints.addAll(
            points.map { point ->
                val x = zeroX + point.x * scaleX
                val y = zeroY - point.y * scaleY
                XYPoint(point.index, x, y)
            }
        )
    }

    private fun calculateStep(range: Float): Float {
        return when {
            range <= 2 -> 0.1f
            range <= 20 -> 1f
            range <= 100 -> 5f
            else -> 10f
        }
    }

    private fun Canvas.drawAxisLabels(
        min: Float,
        max: Float,
        step: Float,
        zeroPos: Float,  // Для X это zeroY, для Y это zeroX
        axisZero: Float,  // Для X это zeroX, для Y это zeroY
        scale: Float,
        isXAxis: Boolean
    ) {
        var current = (min / step).toInt() * step
        while (current <= max) {
            val pos = if (isXAxis) axisZero + current * scale else axisZero - current * scale

            val markSize = 10f // Размер деления

            if (isXAxis) {
                drawLine(pos, zeroPos - markSize, pos, zeroPos + markSize, gridPaint)
            } else {
                drawLine(zeroPos - markSize, pos, zeroPos + markSize, pos, gridPaint)
            }
            current += step
        }
    }

    private fun Canvas.drawSmoothLine() {
        if (mappedPoints.size < 2) return

        linePath.reset()
        linePath.moveTo(mappedPoints[0].x, mappedPoints[0].y)

        for (i in 0 until mappedPoints.lastIndex) {
            val p0 = if (i > 0) mappedPoints[i - 1] else mappedPoints[i]
            val p1 = mappedPoints[i]
            val p2 = mappedPoints[i + 1]
            val p3 = if (i < mappedPoints.size - 2) mappedPoints[i + 2] else p2

            // Контрольные точки для плавного перехода
            val controlX1 = p1.x + (p2.x - p0.x) / 6f
            val controlY1 = p1.y + (p2.y - p0.y) / 6f
            val controlX2 = p2.x - (p3.x - p1.x) / 6f
            val controlY2 = p2.y - (p3.y - p1.y) / 6f

            linePath.cubicTo(controlX1, controlY1, controlX2, controlY2, p2.x, p2.y)
        }

        drawPath(linePath, linePaint)
    }

    private fun Canvas.drawStrictLine() {
        if (mappedPoints.size < 2) return

        for (i in 0 until mappedPoints.lastIndex) {
            val p1 = mappedPoints[i]
            val p2 = mappedPoints[i + 1]
            drawLine(p1.x, p1.y, p2.x, p2.y, linePaint)
        }
    }

}


