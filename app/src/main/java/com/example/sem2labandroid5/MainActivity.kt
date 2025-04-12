package com.example.sem2labandroid5

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
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

        findViewById<Button>(R.id.btnRed).setOnClickListener {
            drawingView.setColor(Color.BLACK)
        }

        findViewById<Button>(R.id.btnBlue).setOnClickListener {
            drawingView.setColor(Color.BLUE)
        }
    }
}