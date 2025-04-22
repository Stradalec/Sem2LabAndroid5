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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private val drawingViewModel: DrawingViewModel by viewModels()
    private val pictureCode = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawingView)


        drawingViewModel.paths.observe(this) { drawingView.paths = it }
        drawingViewModel.backgroundBitmap.observe(this) { drawingView.backgroundBitmap = it }
        drawingViewModel.currentColor.observe(this) { drawingView.currentColor = it }
        drawingViewModel.currentStrokeWidth.observe(this) { drawingView.currentStrokeWidth = it }

        drawingView.onStartPath = { x, y -> drawingViewModel.startPath(x, y) }
        drawingView.onChangePath = { x, y ->
            drawingViewModel.addPointToPath(x, y)
        }
        drawingView.onFinishPath = { drawingViewModel.finishPath() }

        val seekBarBrushSize = findViewById<SeekBar>(R.id.seekBarBrushSize)
        val tvBrushSizeValue = findViewById<TextView>(R.id.tvBrushSizeValue)

        seekBarBrushSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawingViewModel.setStrokeWidth(progress.toFloat())
                tvBrushSizeValue.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        findViewById<Button>(R.id.btnBlack).setOnClickListener {
            drawingViewModel.setColor(Color.BLACK)
        }
        findViewById<Button>(R.id.btnBlue).setOnClickListener {
            drawingViewModel.setColor(Color.BLUE)
        }
        findViewById<Button>(R.id.btnRed).setOnClickListener {
            drawingViewModel.setColor(Color.RED)
        }
        findViewById<Button>(R.id.btnYellow).setOnClickListener {
            drawingViewModel.setColor(Color.YELLOW)
        }
        findViewById<Button>(R.id.btnGreen).setOnClickListener {
            drawingViewModel.setColor(Color.GREEN)
        }
        findViewById<Button>(R.id.btnViolet).setOnClickListener {
            drawingViewModel.setColor(Color.parseColor("#660099"))
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener { drawingViewModel.clearAll() }
        findViewById<Button>(R.id.btnUndo).setOnClickListener { drawingViewModel.undoLast() }

        findViewById<Button>(R.id.btnLoadImage).setOnClickListener {
            openGallery()
        }

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val bitmap = drawingView.getBitmap()
            val savedPath = drawingViewModel.saveDrawing(bitmap)
            if (savedPath != null) {
                Toast.makeText(this, "Сохранено в: $savedPath", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Не удалось сохранить", Toast.LENGTH_SHORT).show()
            }
        }

        drawingViewModel.saveStatus.observe(this) { status ->
            status?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                drawingViewModel.resetStatus()
            }
        }
        drawingViewModel.loadStatus.observe(this) { status ->
            status?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                drawingViewModel.resetStatus()
            }
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        }
        startActivityForResult(intent, pictureCode)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pictureCode && resultCode == RESULT_OK) {
            val uri = data?.data
            uri?.let {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                    drawingViewModel.setBackgroundBitmap(bitmap)
                } catch (e: IOException) {
                    Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
