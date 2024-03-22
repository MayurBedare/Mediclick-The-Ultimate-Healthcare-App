package com.example.mediclick

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

open class AC1 : FeatureActivity() {

    private lateinit var button: LinearLayout
    private lateinit var progressBar: LottieAnimationView
    private lateinit var anim: LottieAnimationView
    private lateinit var cityEditText: EditText
    private lateinit var countryEditText: EditText

    private val apiKey = "c48rHRZ3U7jkRrLDwMNgMg==ybXyXCeiJu8k8DGs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac1)

        // Initialize UI components
        button = findViewById(R.id.apa1)
        progressBar = findViewById(R.id.progressBar)
        anim = findViewById(R.id.breatheView)
        cityEditText = findViewById(R.id.userCity)
        countryEditText = findViewById(R.id.userCountryAP)

        // Set click listener for the button
        button.setOnClickListener {
            cityEditText.visibility = View.INVISIBLE
            countryEditText.visibility = View.INVISIBLE
            anim.visibility = View.INVISIBLE
            fetchData()
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

    private fun fetchData() {
        val userCity = cityEditText.text.toString().trim()
        val userCountry = countryEditText.text.toString().trim()

        // Check for empty input fields
        if (userCity.isEmpty() || userCountry.isEmpty()) {
            showToast("Please enter both city and country.")
            progressBar.visibility = View.INVISIBLE
            cityEditText.visibility = View.VISIBLE
            countryEditText.visibility = View.VISIBLE
            anim.visibility= View.VISIBLE
            return
        }

        progressBar.playAnimation()
        progressBar.visibility = View.VISIBLE

        val apiUrl = buildApiUrl(userCity, userCountry)
        val client = createOkHttpClient()

        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("X-Api-Key", apiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    handleResponse(response, userCity)
                } catch (e: Exception) {
                    handleError(e)
                } finally {

                }
            }

            override fun onFailure(call: Call, e: IOException) {
                handleError(e)
            }
        })
    }

    private fun handleResponse(response: Response, userCity: String) {
        val responseBody = response.body?.string()

        try {
            // Log the API response
            Log.d("AirActivity", "API Response: $responseBody")

            val locationArray = JSONArray(responseBody)
            if (locationArray.length() > 0) {
                val firstLocation = locationArray.getJSONObject(0)
                navigateToNextPage(
                    userCity,
                    firstLocation.getString("state"),
                    firstLocation.getDouble("latitude"),
                    firstLocation.getDouble("longitude"),
                    firstLocation.getString("country"),
                    firstLocation.getString("name")
                )
            } else {
                showToast("No data found for the given location.")
            }
        } catch (e: Exception) {
            showToast("Error parsing API response.")
        }
    }

    private fun handleError(error: Throwable) {
        showToast("Failed to fetch data. Check your internet connection.")
    }

     override fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToNextPage(
        userCity: String,
        state: String,
        latitude: Double,
        longitude: Double,
        country: String,
        name: String
    ) {
        // Log the values after navigating to the next page
        Log.d("AirActivity", "userCity: $userCity")
        Log.d("AirActivity", "name: $state")
        Log.d("AirActivity", "latitude: $latitude")
        Log.d("AirActivity", "longitude: $longitude")
        Log.d("AirActivity", "country: $country")

        // Create an intent to start the next activity
        val intent = Intent(this, AC2::class.java).apply {
            putExtra("USER_CITY", userCity)
            putExtra("NAME",name)
            putExtra("LOCATION_STATE", state)
            putExtra("LOCATION_LATITUDE", latitude)
            putExtra("LOCATION_LONGITUDE", longitude)
            putExtra("LOCATION_COUNTRY", country)
        }
        // Start the next activity
        startActivity(intent)
    }

    private fun buildApiUrl(userCity: String, userCountry: String): String {
        return "https://api.api-ninjas.com/v1/geocoding?city=$userCity&country=$userCountry"
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}
