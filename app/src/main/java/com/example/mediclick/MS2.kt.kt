package com.example.mediclick

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.*

open class MS2 : MS1() {

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleCameraResult(result.data?.extras?.get("data") as Bitmap?)
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleGalleryResult(result.data?.data)
        }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        const val IMAGE_URL_EXTRA = "image_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ms2)

        val cameraButton: ImageView = findViewById(R.id.cameraButton)
        val galleryButton: ImageView = findViewById(R.id.galleryButton)
        val backButton: ImageView = findViewById(R.id.imageNext)

        backButton.setOnClickListener {
            val intent = Intent(this, MS1::class.java)
            startActivity(intent)
            finish()
        }
        cameraButton.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        galleryButton.setOnClickListener {
            openGallery()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        navigateToPreviousActivity()
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (isCameraPermissionGranted()) {
            openCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun handleCameraResult(bitmap: Bitmap?) {
        if (bitmap != null) {
            val imageUri = saveImageToMediaStore(bitmap)
            if (imageUri != null) {
                startNextActivity(imageUri)
            } else {
                Log.e("HP2", "Failed to save image. Image URI is null.")
            }
        } else {
            Log.e("HP2", "Bitmap is null. Image capture may have failed.")
        }
    }

    private fun handleGalleryResult(imageUri: Uri?) {
        if (imageUri != null) {
            startNextActivity(imageUri)
        } else {
            Log.e("HP2", "Image URI is null. Image selection may have failed.")
        }
    }

    private fun saveImageToMediaStore(bitmap: Bitmap): Uri? {
        var imageUrl: Uri? = null

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "${UUID.randomUUID()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        contentResolver.insert(collection, values)?.also { uri ->
            try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                        throw IOException("Failed to save bitmap.")
                    }
                }
                imageUrl = uri
            } catch (e: IOException) {
                Log.e("HP2", "Failed to save image to media store.")
            }
        }

        return imageUrl
    }

    private fun startNextActivity(imageUri: Uri) {
        val intent = Intent(this, MS2Crop::class.java)
        intent.putExtra(IMAGE_URL_EXTRA, imageUri.toString())
        startActivity(intent)
    }

    private fun navigateToPreviousActivity() {
        val intent = Intent(this, MS1::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Handle permission result if needed
    }
}
