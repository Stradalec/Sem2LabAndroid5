package com.example.sem2labandroid5

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var backgroundBitmap: Bitmap? = null
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
    }

    private var currentColor = Color.BLACK
    private var currentStrokeWidth = 5f
    private var currentPath = Path()
    private val paths = mutableListOf<DrawingPath>()
    private val borderPadding = 10f

    private val borderStats = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 15f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path()
                currentPath.moveTo(event.x, event.y)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                currentPath.lineTo(event.x, event.y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                paths.add(
                    DrawingPath(
                        path = Path(currentPath),
                        color = currentColor,
                        strokeWidth = currentStrokeWidth
                    )
                )
                currentPath.reset()
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    fun setColor(color: Int) {
        currentColor = color
        invalidate()
    }

    fun setBrushSize(size: Float) {
        currentStrokeWidth = size
        invalidate()
    }

    fun setBackgroundBitmap(bitmap: Bitmap?) {
        backgroundBitmap = bitmap
        invalidate()
    }

    fun clearAllPaths() {
        paths.clear()
        currentPath.reset()
        invalidate()
    }

    fun undoLastAction() {
        paths.remove(paths.last())
        currentPath.reset()
        invalidate()
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)


        backgroundBitmap?.let {
            canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
        }


        val tempPaint = Paint(paint)
        for (drawingPath in paths) {
            tempPaint.color = drawingPath.color
            tempPaint.strokeWidth = drawingPath.strokeWidth
            canvas.drawPath(drawingPath.path, tempPaint)
        }


        tempPaint.color = currentColor
        tempPaint.strokeWidth = currentStrokeWidth
        canvas.drawPath(currentPath, tempPaint)

        return bitmap
    }

    override fun onDraw(canvas: Canvas) {
        backgroundBitmap?.let {
            canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
        }
        val borderRectangle = RectF(
            borderPadding,
            borderPadding,
            width - borderPadding,
            height - borderPadding
        )

        canvas.drawRect(borderRectangle, borderStats)

        for (drawingPath in paths) {
            paint.color = drawingPath.color
            paint.strokeWidth = drawingPath.strokeWidth
            canvas.drawPath(drawingPath.path, paint)
        }
        paint.color = currentColor
        paint.strokeWidth = currentStrokeWidth
        canvas.drawPath(currentPath, paint)
    }
}