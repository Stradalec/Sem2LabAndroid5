package com.example.sem2labandroid5

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var backgroundBitmap: Bitmap? = null
    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }
    private val currentPath = Path()
    private val paths = mutableListOf<Path>()
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath.moveTo(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.lineTo(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                paths.add(currentPath)
                currentPath.reset()
            }
        }
        return super.onTouchEvent(event)
    }

    fun setColor(color: Int) {
        paint.color = color
        invalidate()
    }

    fun setBrushSize(size: Float) {
        paint.strokeWidth = size
        invalidate()
    }
    fun setBackgroundBitmap(bitmap: Bitmap?) {
        backgroundBitmap = bitmap
        invalidate()
    }
    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)


        backgroundBitmap?.let {
            canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
        }

        for (path in paths) {
            canvas.drawPath(path, paint)
        }


        canvas.drawPath(currentPath, paint)

        return bitmap
    }

    override fun onDraw(canvas: Canvas) {
        backgroundBitmap?.let {
            canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
        }
        for (path in paths) {
            canvas.drawPath(path, paint)
        }
        canvas.drawPath(currentPath, paint)
    }
}