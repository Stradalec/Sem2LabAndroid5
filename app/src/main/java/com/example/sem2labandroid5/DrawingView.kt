package com.example.sem2labandroid5

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
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
    override fun onDraw(canvas: Canvas) {
        paths.forEach { canvas.drawPath(it, paint) }
        canvas.drawPath(currentPath, paint)
    }
}