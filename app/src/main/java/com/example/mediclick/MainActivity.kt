package com.example.mediclick

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
//import com.chaquo.python.Python
//import com.chaquo.python.android.AndroidPlatform

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val containerLayout: LinearLayout = findViewById(R.id.ma)
        containerLayout.setOnClickListener {
            // Handle the click action here, navigate to HealthTipsActivity
            val intent = Intent(this@MainActivity, OnboardingActivity::class.java)
            startActivity(intent)
        }
    }
}
