package com.example.mediclick

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.Calendar

class About : AppCompatActivity() {
    private val viewModel: AboutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about)

        observeViewModel()

        findViewById<CalendarView>(R.id.cal).date = Calendar.getInstance().timeInMillis

        findViewById<Button>(R.id.logout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)

            val preferences: SharedPreferences? =
                getSharedPreferences("ONBOARD", Context.MODE_PRIVATE)
            preferences?.edit()?.remove("ISCOMPLETE")?.apply()
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

    private fun observeViewModel() {
        viewModel.liveFirstName.observe(this, Observer {
            findViewById<TextView>(R.id.name).text = "Hi ${it.toString()}!"
        })

        viewModel.liveProfilePicture.observe(this, Observer {
            val url = it.toString()
            if (url.isNotEmpty()) {
                Picasso.get().load(url).into(findViewById<ImageView>(R.id.avatar))
            } else {
                Toast.makeText(this, "Profile picture URL is empty", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
