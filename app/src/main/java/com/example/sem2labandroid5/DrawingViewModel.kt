package com.example.sem2labandroid5

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Path
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DrawingViewModel(application: Application) : AndroidViewModel(application) {

    private val _paths = MutableLiveData<List<DrawingPath>>(emptyList())
    val paths: LiveData<List<DrawingPath>> = _paths

    private val _backgroundBitmap = MutableLiveData<Bitmap?>(null)
    val backgroundBitmap: LiveData<Bitmap?> = _backgroundBitmap

    private val _currentColor = MutableLiveData<Int>(Color.BLACK)
    val currentColor: LiveData<Int> = _currentColor

    private val _currentStrokeWidth = MutableLiveData<Float>(5f)
    val currentStrokeWidth: LiveData<Float> = _currentStrokeWidth

    private val _saveStatus = MutableLiveData<String?>(null)
    val saveStatus: LiveData<String?> = _saveStatus

    private val _loadStatus = MutableLiveData<String?>(null)
    val loadStatus: LiveData<String?> = _loadStatus

    private var currentPath: Path? = null

    fun setColor(color: Int) {
        _currentColor.value = color
    }

    fun setStrokeWidth(size: Float) {
        _currentStrokeWidth.value = size
    }

    fun setBackgroundBitmap(bitmap: Bitmap?) {
        _backgroundBitmap.value = bitmap
    }

    fun startPath(x: Float, y: Float) {
        currentPath = Path().apply { moveTo(x, y) }
    }

    fun addPointToPath(x: Float, y: Float) {
        currentPath?.lineTo(x, y)
    }

    fun finishPath() {
        val c = _currentColor.value ?: Color.BLACK
        val w = _currentStrokeWidth.value ?: 5f
        val path = currentPath ?: return
        _paths.value = _paths.value!! + DrawingPath(Path(path), c, w)
        currentPath = null
    }

    fun clearAll() {
        _paths.value = emptyList()
        currentPath = null
    }

    fun undoLast() {
        val list = _paths.value ?: return
        if (list.isNotEmpty()) {
            _paths.value = list.dropLast(1)
        }
    }


    fun saveDrawing(saveBitmap: Bitmap): String? {
        val context = getApplication<Application>()
        val fileName = "drawing_${System.currentTimeMillis()}.png"
        val file = File(context.getExternalFilesDir(null), fileName)
        return try {
            FileOutputStream(file).use { out ->
                saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            _saveStatus.value = "Сохранено: ${file.absolutePath}"
            file.absolutePath
        } catch (e: Exception) {
            _saveStatus.value = "Ошибка сохранения"
            null
        }
    }

    fun loadDrawing(absolutePath: String): Boolean {
        return try {
            val file = File(absolutePath)
            val bitmap = android.graphics.BitmapFactory.decodeStream(FileInputStream(file))
            _backgroundBitmap.value = bitmap
            _loadStatus.value = "Загружено: $absolutePath"
            true
        } catch (e: Exception) {
            _loadStatus.value = "Ошибка загрузки"
            false
        }
    }


    fun resetStatus() {
        _saveStatus.value = null
        _loadStatus.value = null
    }
}
