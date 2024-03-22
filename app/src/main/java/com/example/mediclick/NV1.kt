package com.example.mediclick

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView

open class NV1 : FeatureActivity() {

    private lateinit var button1: LinearLayout
    private lateinit var progressBar: LottieAnimationView
    private lateinit var queryEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nv1)

        // Find the ProgressBar by its ID
        progressBar = findViewById(R.id.progressBar)
        queryEditText = findViewById(R.id.editTextText3)

        button1 = findViewById(R.id.naa1)

        button1.setOnClickListener {
            val userQuery = queryEditText.text.toString().trim()
            progressBar.visibility = View.VISIBLE // Show progress bar

            // Simulate a delay for demonstration purposes
            Handler().postDelayed({
                analyzeQuery(userQuery)
                progressBar.visibility = View.GONE // Hide progress bar
            }, 5000) // Adjust this delay as needed

            analyzeQuery(userQuery)

            // Create an intent and pass the user query to the next activity
            val intent = Intent(this@NV1, NV2::class.java)
            intent.putExtra("userQuery", userQuery)
            startActivity(intent)
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

    private fun analyzeQuery(userQuery: String) {
        Log.d(TAG, "User Query: $userQuery")

        if (!userQuery.isEmpty()) {
            if (userQuery.contains(",")) {
                // Show an error message for using commas in the query
                Toast.makeText(this, "Do not use commas between items instead use spaces", Toast.LENGTH_SHORT).show()
            } else if (userQuery.length > 1500) {
                // Show an error message if query exceeds character limit
                Toast.makeText(this, "Query cannot exceed 1500 characters", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
