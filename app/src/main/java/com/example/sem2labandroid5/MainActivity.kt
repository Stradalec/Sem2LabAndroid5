package com.example.sem2labandroid5

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var drawingView: DrawingView
    private val PICK_IMAGE_REQUEST = 100
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
        findViewById<Button>(R.id.btnLoadImage).setOnClickListener {
            openGallery()
        }
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveDrawing()
        }

    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    private fun saveDrawing() {
        val bitmap = drawingView.getBitmap()

        val fileName = "drawing_${System.currentTimeMillis()}.png"
        val file = File(getExternalFilesDir(null), fileName)

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            Toast.makeText(this, "Saved to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                drawingView.setBackgroundBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}