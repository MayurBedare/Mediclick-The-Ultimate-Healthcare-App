package com.example.mediclick

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView

open class LoginAddUser : SignMainActivity() {

    companion object {
        private const val TAG = "LoginAddUser"
        private const val REQUEST_IMAGE_PICK = 100
    }

    private lateinit var firstNameEdt: TextInputEditText
    private lateinit var lastNameEdt: TextInputEditText
    private lateinit var phoneNumberEdt: TextInputEditText
    private lateinit var emailEdt: TextInputEditText
    private lateinit var passwordEdt: TextInputEditText
    private lateinit var cpasswordEdt: TextInputEditText
    private lateinit var createuserBtn: MaterialButton
    private lateinit var loadingPB: ProgressBar
    private lateinit var profileImage: CircleImageView

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                handleGalleryResult(imageUri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_add_user)

        initializeViews()

        createuserBtn.setOnClickListener {
            if (validateInput()) {
                loadingPB.visibility = View.VISIBLE
                val profileImageUri = profileImage.tag as? Uri
                val intent = Intent(this, FeatureActivity::class.java).apply {
                    putExtra("profileImageUri", profileImageUri)
                }
                startActivity(intent)
                finish()
            }
        }

        profileImage.setOnClickListener {
            openGallery()
        }
    }

    private fun validateInput(): Boolean {
        val firstName = firstNameEdt.text.toString().trim()
        val lastName = lastNameEdt.text.toString().trim()
        val phoneNumber = phoneNumberEdt.text.toString().trim()
        val email = emailEdt.text.toString().trim()
        val password = passwordEdt.text.toString().trim()
        val confirmPassword = cpasswordEdt.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            showToast("Please fill in all the details")
            return false
        }

        if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
            showToast("Passwords do not match")
            return false
        }

        return true
    }

    open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun handleGalleryResult(imageUri: Uri?) {
        if (imageUri != null) {
            profileImage.setImageURI(imageUri)
            profileImage.tag = imageUri // Set the URI as a tag to the profile image view
        } else {
            Log.e(TAG, "Image URI is null. Image selection may have failed.")
        }
    }

    private fun initializeViews() {
        firstNameEdt = findViewById(R.id.ptxt_firstname)
        lastNameEdt = findViewById(R.id.ptxt_lastname)
        phoneNumberEdt = findViewById(R.id.ptxt_phonenumber)
        emailEdt = findViewById(R.id.ptxt_email)
        passwordEdt = findViewById(R.id.ptxtpassword)
        cpasswordEdt = findViewById(R.id.ptxt_cpassword)
        createuserBtn = findViewById(R.id.btn_createuser)
        loadingPB = findViewById(R.id.progressbar)
        profileImage = findViewById(R.id.select_photo_imageview_register)
    }
}