// HP2Crop.kt

package com.example.mediclick

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.canhub.cropper.CropImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*

open class MS2Crop : MS2() {

    private lateinit var cropImageView: CropImageView
    private lateinit var selectedImageUri: Uri
    private lateinit var animationView: LottieAnimationView

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ms2crop)


        cropImageView = findViewById(R.id.cropImageView)
        animationView = findViewById(R.id.breatheView)

        // Retrieve the selected image URI from the intent
        selectedImageUri = Uri.parse(intent.getStringExtra(MS2.IMAGE_URL_EXTRA) ?: "")

        // Set the selected image to CropImageView
        cropImageView.setImageUriAsync(selectedImageUri)

        val confirmButton: LinearLayout = findViewById(R.id.hpa2)
        confirmButton.setOnClickListener {

            cropImageView.isVisible = false
            animationView.isVisible = true

            // Start the Lottie animation
            animationView.playAnimation()

            // Get the cropped image bitmap synchronously
            val cropped: Bitmap? = cropImageView.getCroppedImage()

            if (cropped != null) {
                // Save the cropped image to Firebase
                saveCroppedImageToFirebase(cropped)
            } else {
                Log.e("HP2Crop", "Cropped bitmap is null.")
            }
        }

        val backButton: ImageView = findViewById(R.id.imageNext)

        backButton.setOnClickListener {
            val intent = Intent(this, MS2::class.java)
            startActivity(intent)
            finish()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MS2::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveCroppedImageToFirebase(bitmap: Bitmap) {
        val user = auth.currentUser
        user?.let {
            val imageName = "${System.currentTimeMillis()}_${user.uid}.jpg"
            val storageRef: StorageReference =
                FirebaseStorage.getInstance().reference.child("images/${it.uid}/$imageName")

            val uploadTask: UploadTask = storageRef.putFile(saveBitmapToUri(bitmap))

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: RuntimeException("Upload failed with an unknown exception.")
                }
                return@continueWithTask storageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    Log.d("HP2Crop", "Cropped Image URL: $downloadUri")
                    startNextActivity(downloadUri.toString())
                } else {
                    Log.e("HP2Crop", "Upload failed: ${task.exception?.message}")
                }
            }
        } ?: run {
            Log.e("HP2Crop", "User is not authenticated.")
        }
    }

    private fun saveBitmapToUri(bitmap: Bitmap): Uri {
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

        val imageUri = contentResolver.insert(collection, values)

        imageUri?.let {
            try {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                        throw IOException("Failed to save bitmap.")
                    }
                }
            } catch (e: IOException) {
                Log.e("HP2Crop", "Failed to save image to media store.")
            }
        }

        return imageUri ?: Uri.EMPTY
    }

    private fun startNextActivity(imageUrl: String) {
        val intent = Intent(this, MS3::class.java)
        intent.putExtra(IMAGE_URL_EXTRA, imageUrl)
        Log.d("HP2Crop", "Cropped Image URL: $imageUrl")
        startActivity(intent)
    }
}
