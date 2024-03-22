package com.example.mediclick

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import de.hdodenhof.circleimageview.CircleImageView

open class ML1 : FeatureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ml1)

        val myButtonw: LinearLayout = findViewById(R.id.mia1)

        myButtonw.setOnClickListener {

            // Handle the click event, e.g., start a new activity
            val intent = Intent(this, ML2::class.java)
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