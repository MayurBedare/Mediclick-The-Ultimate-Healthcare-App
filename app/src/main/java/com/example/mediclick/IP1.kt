package com.example.mediclick

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout

open class IP1 : FeatureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ip1)

        val myButton: LinearLayout = findViewById(R.id.ipa1)

        // Set onClickListener for the button
        myButton.setOnClickListener {
            // Handle the click event, e.g., start a new activity
            val intent = Intent(this, IP2::class.java)
            startActivity(intent)
            finish()
        }

        val backButton: ImageView = findViewById(R.id.imageNext)

        backButton.setOnClickListener {
            super.onBackPressed()
            val intent = Intent(this, FeatureActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, FeatureActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}
