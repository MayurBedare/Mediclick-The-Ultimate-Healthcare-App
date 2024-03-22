package com.example.mediclick

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class AC2 : AC1() {

    private lateinit var customToast: Toast
    private lateinit var button: TextView
    private lateinit var progressBar: LottieAnimationView
    private lateinit var main: ConstraintLayout
    private lateinit var progressBar3: LottieAnimationView
    private lateinit var overallAqiTextView: TextView
    private lateinit var coTextView: TextView
    private lateinit var coTextViewa: TextView
    private lateinit var pm10TextView: TextView
    private lateinit var so2TextView: TextView
    private lateinit var pm25TextView: TextView
    private lateinit var o3TextView: TextView
    private lateinit var no2TextView: TextView
    private lateinit var pm10TextViewa: TextView
    private lateinit var so2TextViewa: TextView
    private lateinit var pm25TextViewa: TextView
    private lateinit var o3TextViewa: TextView
    private lateinit var no2TextViewa: TextView
    private lateinit var uvIndexTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var textV25: TextView
    private lateinit var textView26: TextView
    private lateinit var textView27: TextView
    private lateinit var uvindexremark: TextView
    private lateinit var pm10Info: String
    private lateinit var pm25Info: String
    private lateinit var no2Info: String
    private lateinit var o3Info: String
    private lateinit var so2Info: String
    private lateinit var backButton: ImageView
    private lateinit var coInfo: String

    private val apiKey = "c48rHRZ3U7jkRrLDwMNgMg==ybXyXCeiJu8k8DGs"
    private val openUVApiKey = "openuv-1fac6rlt8ce03a-io"
    private val openUVApiUrl = "https://api.openuv.io/api/v1/uv"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac2)

        button = findViewById(R.id.ck)
        backButton = findViewById(R.id.imageNext)
        progressBar = findViewById(R.id.progressBar)
        progressBar3 = findViewById(R.id.progressBar3)
        main = findViewById(R.id.constraintLayout5)
        overallAqiTextView = findViewById(R.id.overallAqiTextView)
        coTextView = findViewById(R.id.coTextView)
        pm10TextView = findViewById(R.id.pm10TextView)
        so2TextView = findViewById(R.id.so2textView)
        pm25TextView = findViewById(R.id.pm25TextView)
        o3TextView = findViewById(R.id.o3TextView)
        no2TextView = findViewById(R.id.no2TextView)
        coTextViewa = findViewById(R.id.coTextViewa)
        pm10TextViewa = findViewById(R.id.pm10TextViewa)
        so2TextViewa = findViewById(R.id.so2TextViewa)
        pm25TextViewa = findViewById(R.id.pm25TextViewa)
        o3TextViewa = findViewById(R.id.o3TextViewa)
        no2TextViewa = findViewById(R.id.no2TextViewa)
        uvIndexTextView = findViewById(R.id.textView26)
        cityTextView = findViewById(R.id.city)
        textV25 = findViewById(R.id.textV25)
        textView27 = findViewById(R.id.textView27)
        uvindexremark = findViewById(R.id.uvindexremark)

        val content = "Click on each pollutant like CO, NO2, etc from below list"
        val spannableString = SpannableString(content)
        spannableString.setSpan(UnderlineSpan(), 0, content.length, 0)
        findViewById<TextView>(R.id.textView12).text = spannableString

        button.setOnClickListener {
            // Fetch UV index data
            fetchUVIndex()
        }

        fetch1Data()

        // Set information for each pollutant
        setPollutantInformation()

        backButton.setOnClickListener {
            val intent = Intent(this, AC1::class.java)
            startActivity(intent)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, AC1::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetch1Data() {
        val userCity = intent.getStringExtra("NAME")
        val userState = intent.getStringExtra("LOCATION_STATE")
        val userCountry = intent.getStringExtra("LOCATION_COUNTRY")

        cityTextView.text = "$userCity,$userState,$userCountry"

        val apiUrl = "https://api.api-ninjas.com/v1/airquality?city=$userCity"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("X-Api-Key", apiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    handleResponse(response)
                } catch (e: Exception) {
                    handleError(e)
                } finally {
                    hideProgressBar()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                handleError(e)
                hideProgressBar()
            }
        })
    }

    private fun showProgressBar3() {
        runOnUiThread {
            progressBar3.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar3() {
        runOnUiThread {
            progressBar3.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        // Add an animation listener to the progress bar
        progressBar.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator, isReverse: Boolean) {
                // Not needed
            }

            override fun onAnimationStart(p0: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                // Hide the progress bar after the animation ends
                runOnUiThread {
                    progressBar.visibility = View.INVISIBLE
                    progressBar3.visibility = View.GONE
                    main.visibility = View.VISIBLE
                }
            }

            override fun onAnimationEnd(p0: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationCancel(p0: Animator) {
                // Not needed
            }

            override fun onAnimationRepeat(p0: Animator) {
                // Not needed
            }
        })
    }


    private fun handleResponse(response: Response) {
        val responseBody = response.body?.string()
        Log.d("AirActivity2", "Entire Response: $responseBody")

        // Check if the response body is not null or empty
        if (!responseBody.isNullOrEmpty()) {
            displayAttributes(responseBody)
            progressBar.repeatCount = 0
        } else {
            showToast("Empty or null response received.")
            progressBar.repeatCount = LottieDrawable.INFINITE
        }
    }

    private fun displayAttributes(responseBody: String) {
        // Parse the JSON response
        val jsonResponse = JSONObject(responseBody)

        // Extract attributes
        val overallAqi = jsonResponse.optInt("overall_aqi", -1)
        val co = jsonResponse.optJSONObject("CO")
        val pm10 = jsonResponse.optJSONObject("PM10")
        val so2 = jsonResponse.optJSONObject("SO2")
        val pm25 = jsonResponse.optJSONObject("PM2.5")
        val o3 = jsonResponse.optJSONObject("O3")
        val no2 = jsonResponse.optJSONObject("NO2")

        // Display attributes in corresponding TextViews
        runOnUiThread {
            overallAqiTextView.text = "$overallAqi"
            coTextView.text = "${co?.optDouble("concentration", -1.0)}"
            pm10TextView.text = "${pm10?.optDouble("concentration", -1.0)}"
            so2TextView.text = "${so2?.optDouble("concentration", -1.0)} "
            pm25TextView.text = "${pm25?.optDouble("concentration", -1.0)} "
            o3TextView.text = "${o3?.optDouble("concentration", -1.0)} "
            no2TextView.text = "${no2?.optDouble("concentration", -1.0)} "
        }

        // Log individual attributes
        Log.d("AirActivity2", "Overall AQI: $overallAqi")
        Log.d("AirActivity2", "CO: ${co?.optDouble("concentration", -1.0)}")
        Log.d("AirActivity2", "PM10: ${pm10?.optDouble("concentration", -1.0)}")
        Log.d("AirActivity2", "SO2: ${so2?.optDouble("concentration", -1.0)}")
        Log.d("AirActivity2", "PM2.5: ${pm25?.optDouble("concentration", -1.0)}")
        Log.d("AirActivity2", "O3: ${o3?.optDouble("concentration", -1.0)}")
        Log.d("AirActivity2", "NO2: ${no2?.optDouble("concentration", -1.0)}")

        // Set remarks and advice for overall AQI and UV Index
        setRemarksAndAdvice(overallAqi)
    }

    private fun handleError(error: Throwable) {
        showToast("Failed to fetch data. Check your internet connection.")
        Log.e("AirActivity2", "Error: ${error.message}")
    }

    override fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUVIndex() {
        showProgressBar3()
        val latitude = intent.getDoubleExtra("LOCATION_LATITUDE",0.0)
        val longitude =  intent.getDoubleExtra("LOCATION_LONGITUDE",0.0)

        val apiUrl = "$openUVApiUrl?lat=$latitude&lng=$longitude"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("x-access-token", openUVApiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    handleUVIndexResponse(response)
                } catch (e: Exception) {
                    handleError(e)
                } finally {
                    hideProgressBar3()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                handleError(e)
                hideProgressBar3()
            }
        })
    }

    private fun handleUVIndexResponse(response: Response) {
        val responseBody = response.body?.string()
        Log.d("AirActivity2", "UV Index Response: $responseBody")

        // Parse the UV Index JSON response
        val uvJson = responseBody?.let { JSONObject(it).optJSONObject("result") }
        val uvIndex = uvJson?.optDouble("uv", -1.0)

        // Display UV Index in textView26
        runOnUiThread {
            uvIndexTextView.text = "$uvIndex"
            setUVIndexRemarkAndAdvice(uvIndex)
        }
    }

    private fun setRemarksAndAdvice(overallAqi: Int) {
        setOverallAQIRemarkAndAdvice(overallAqi)
    }

    private fun setOverallAQIRemarkAndAdvice(overallAqi: Int) {
        val overallAqiRemark: String
        val overallAqiAdvice: String

        when (overallAqi) {
            in 0..50 -> {
                overallAqiRemark = "Good"
                overallAqiAdvice = "Air quality is excellent. Enjoy the fresh air!"
            }
            in 51..100 -> {
                overallAqiRemark = "Satisfactory"
                overallAqiAdvice = "Air quality is acceptable. No health impacts expected."
            }
            in 101..200 -> {
                overallAqiRemark = "Moderate"
                overallAqiAdvice = "Air quality is fair. Sensitive groups may experience minor breathing discomfort. Consider reducing outdoor activity."
            }
            in 201..300 -> {
                overallAqiRemark = "Poor"
                overallAqiAdvice = "Air quality is unhealthy for sensitive groups. Everyone may experience breathing discomfort. Reduce outdoor activity and consider using air purifiers indoors."
            }
            in 301..400 -> {
                overallAqiRemark = "Very Poor"
                overallAqiAdvice = "Air quality is unhealthy for everyone. Limit outdoor activity and stay indoors as much as possible."
            }
            else -> {
                overallAqiRemark = "Severe"
                overallAqiAdvice = "Air quality is hazardous. Everyone may experience serious health effects. Avoid outdoor activity and take precautions to protect yourself indoors."
            }
        }

        runOnUiThread {
            textV25.text = "$overallAqiRemark"
            textView27.text = "$overallAqiAdvice"
        }
    }

    private fun setUVIndexRemarkAndAdvice(uvIndex: Double?) {
        if (uvIndex != null) {
            val uvIndexRemark = when (uvIndex) {
                in 0.0..2.0 -> "Low"
                in 3.0..5.0 -> "Moderate"
                in 6.0..7.0 -> "High"
                in 8.0..10.0 -> "Very High"
                else -> "Extreme"
            }

            val uvIndexAdvice = when (uvIndex) {
                in 0.0..2.0 -> "Minimal sun protection required for normal activity. Wear sunglasses on bright days. If outside for more than one hour, cover up and use sunscreen. Reflection off snow can nearly double UV strength, so wear sunglasses and apply sunscreen on your face."
                in 3.0..5.0 -> "Take precautions by covering up, and wearing a hat, sunglasses, and sunscreen, especially if you will be outside for 30 minutes or more. Look for shade near midday when the sun is strongest."
                in 6.0..7.0 -> "Protection required - UV damages the skin and can cause sunburn. Reduce time in the sun between 11 a.m. and 3 p.m. and take full precautions by seeking shade, covering up exposed skin, wearing a hat and sunglasses, and applying sunscreen."
                in 8.0..10.0 -> "Extra precaution required - unprotected skin will be damaged and can burn quickly. Avoid the sun between 11 a.m. and 3 p.m. and seek shade, cover up, and wear a hat, sunglasses, and sunscreen."
                else -> "Take full precaution. Unprotected skin will be damaged and can burn in minutes. Avoid the sun between 11 a.m. and 3 p.m., cover up, and wear a hat, sunglasses, and sunscreen. Don’t forget that white sand and other bright surfaces reflect UV and increase UV exposure."
            }

            runOnUiThread {
                uvindexremark.text = "$uvIndexRemark"
                textView27.append("\n $uvIndexAdvice")
            }
        } else {
            runOnUiThread {
                textView26.text = "UV Index: N/A"
            }
        }
    }

    private fun setPollutantInformation() {
        pm10Info = """
            PM10 (Particulate Matter 10): Small solid particles and liquid droplets found in the air. 
            PM10 is particles with a diameter smaller than 10 micrometers. It’s emitted from motor vehicles, wood heaters, and industry. 
            Fires and dust storms can also produce high concentrations of Particulate Matter.
        """.trimIndent()

        pm25Info = """
            PM2.5 (Particulate Matter 2.5): Small solid particles and liquid droplets found in the air. 
            PM2.5 is particles with a diameter smaller than 2.5 micrometers. It’s emitted from motor vehicles, wood heaters, and industry. 
            Fires and dust storms can also produce high concentrations of Particulate Matter.
        """.trimIndent()

        no2Info = """
            Nitrogen Dioxide (NO2): A gas and a major component of central city air pollution. 
            It mainly comes from vehicles, industry, power stations, and heating.
        """.trimIndent()

        o3Info = """
            Ozone (O3): A gas found in the stratosphere. It protects us from harmful ultraviolet radiation and the troposphere. 
            Ozone is a harmful pollutant produced by a chemical reaction between the sunlight, organic gases, and nitrogen oxides released by:
            - Cars
            - Power plants
            - Other sources
        """.trimIndent()

        so2Info = """
            Sulfur Dioxide (SO2): A toxic gas with a pungent, irritating odor. 
            It can come from electric industries that burn fossil fuels, petrol refineries, cement manufacturing, and volcano emissions.
        """.trimIndent()

        coInfo = """
            Carbon Monoxide (CO): A gas from motor vehicles or machinery that burns fossil fuels.
        """.trimIndent()

        // Set onClickListeners for pollutant TextViews
        setPollutantClickListeners()
    }

    private fun setPollutantClickListeners() {
        pm10TextViewa.setOnClickListener { showInfoToast(pm10Info) }
        pm25TextViewa.setOnClickListener { showInfoToast(pm25Info) }
        no2TextViewa.setOnClickListener { showInfoToast(no2Info) }
        o3TextViewa.setOnClickListener { showInfoToast(o3Info) }
        so2TextViewa.setOnClickListener { showInfoToast(so2Info) }
        coTextViewa.setOnClickListener { showInfoToast(coInfo) }
    }

    private fun showInfoToast(info: String) {
        // Create a custom Toast with a layout containing the information
        val toastLayout = layoutInflater.inflate(R.layout.custom_toast_layout, findViewById(R.id.ctl))
        toastLayout.findViewById<TextView>(R.id.ctt).text = info

        customToast = Toast(applicationContext)
        customToast.view = toastLayout
        customToast.duration = Toast.LENGTH_LONG

        // Show the custom Toast
        customToast.show()

        // Set an OnTouchListener to detect clicks outside the Toast and dismiss it
        toastLayout.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    // Dismiss the custom Toast when clicked outside
                    customToast.cancel()
                }
                return true
            }
        })
    }
}
