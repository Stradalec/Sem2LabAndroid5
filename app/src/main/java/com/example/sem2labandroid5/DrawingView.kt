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


    var paths: List<DrawingPath> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    var backgroundBitmap: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    var currentColor: Int = Color.BLACK
    var currentStrokeWidth: Float = 5f


    var tempPath: Path? = null
        set(value) {
            field = value
            invalidate()
        }


    var onStartPath: ((Float, Float) -> Unit)? = null
    var onChangePath: ((Float, Float) -> Unit)? = null
    var onFinishPath: (() -> Unit)? = null


    private val paint = Paint().apply { style = Paint.Style.STROKE }

    override fun onDraw(canvas: Canvas) {

        backgroundBitmap?.let {
            canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
        }


        for (drawingPath in paths) {
            paint.color = drawingPath.color
            paint.strokeWidth = drawingPath.strokeWidth
            canvas.drawPath(drawingPath.path, paint)
        }


        tempPath?.let {
            paint.color = currentColor
            paint.strokeWidth = currentStrokeWidth
            canvas.drawPath(it, paint)
        }
    }
    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        backgroundBitmap?.let {
            canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
        }

        val paint = Paint().apply { style = Paint.Style.STROKE }
        for (drawingPath in paths) {
            paint.color = drawingPath.color
            paint.strokeWidth = drawingPath.strokeWidth
            canvas.drawPath(drawingPath.path, paint)
        }

        tempPath?.let {
            paint.color = currentColor
            paint.strokeWidth = currentStrokeWidth
            canvas.drawPath(it, paint)
        }

        return bitmap
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onStartPath?.invoke(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                onChangePath?.invoke(x, y)
                return true
            }
            MotionEvent.ACTION_UP -> {
                onFinishPath?.invoke()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}