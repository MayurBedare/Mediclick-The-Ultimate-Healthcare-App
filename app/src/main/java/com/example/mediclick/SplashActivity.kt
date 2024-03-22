package com.example.mediclick

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
open class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set splash screen background color
        window.decorView.setBackgroundColor(Color.WHITE)

            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // If the user is logged in, navigate to the MainActivity
                startActivity(Intent(this, FeatureActivity::class.java))
            } else {
                // If the user is not logged in, navigate to the LoginActivity
                startActivity(Intent(this, MainActivity::class.java))
            }
            // Close the splash screen activity
            finish()
    }
}
