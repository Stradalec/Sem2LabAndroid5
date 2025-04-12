package com.example.sem2labandroid5

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var drawingView: DrawingView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawingView)
        val seekBarBrushSize = findViewById<SeekBar>(R.id.seekBarBrushSize)
        val tvBrushSizeValue = findViewById<TextView>(R.id.tvBrushSizeValue)

        val initialBrushSize = seekBarBrushSize.progress.toFloat()
        drawingView.setBrushSize(initialBrushSize)
        tvBrushSizeValue.text = initialBrushSize.toString()


        seekBarBrushSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val brushSize = progress.toFloat()
                drawingView.setBrushSize(brushSize)
                tvBrushSizeValue.text = brushSize.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        findViewById<Button>(R.id.btnRed).setOnClickListener {
            drawingView.setColor(Color.BLACK)
        }

        findViewById<Button>(R.id.btnBlue).setOnClickListener {
            drawingView.setColor(Color.BLUE)
        }
    }
}