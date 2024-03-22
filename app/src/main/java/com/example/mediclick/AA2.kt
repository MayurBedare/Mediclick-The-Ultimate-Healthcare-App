package com.example.mediclick

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

open class AA2 : AA1() {

    private val apiKey = "T61f2/PVbqLwoFNOmne84g==uVedQjezuMAZp1fB"
    private lateinit var webView: WebView
    private lateinit var openWebViewButton: ImageView
    private lateinit var muscleTextView: TextView
    private lateinit var exerciseNameTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var equipmentTextView: TextView
    private lateinit var instructionsTextView: TextView
    private lateinit var progressBar: LottieAnimationView
    private lateinit var backButon: ImageView
    private lateinit var main: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aa2)

        muscleTextView = findViewById(R.id.ermuscle)
        exerciseNameTextView = findViewById(R.id.ername)
        progressBar = findViewById(R.id.progressBar)
        typeTextView = findViewById(R.id.ertype)
        equipmentTextView = findViewById(R.id.erequipment)
        instructionsTextView = findViewById(R.id.erinstructions)
        webView = findViewById(R.id.yourWebView)
        openWebViewButton = findViewById(R.id.er2)
        backButon = findViewById(R.id.imageNext)
        main = findViewById(R.id.constraintLayout7)

        val muscleSpinnerValue = intent.getStringExtra("muscleN")
        Log.d("ER2", "Muscle name: $muscleSpinnerValue")
        val exerciseSpinnerValue = intent.getStringExtra("exerciseN")

        fetchData(muscleSpinnerValue, exerciseSpinnerValue)

        backButon.setOnClickListener {
            val intent = Intent(this, AA1::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchData(selectedMuscle: String?, selectedExercise: String?) {
        if (selectedMuscle.isNullOrBlank() || selectedExercise.isNullOrBlank()) {
            showToast("Muscle or exercise value is blank or null.")
            return
        }

        val apiUrl = "https://api.api-ninjas.com/v1/exercises?muscle=$selectedMuscle"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("X-Api-Key", apiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()

                        val responseObject = responseBody?.let { JSONArray(it) }
                        if (responseObject != null) {
                            runOnUiThread {
                                handleResponse(selectedExercise, responseObject)
                            }
                        } else {
                            Log.d("ER1", "No items found in the response")
                        }
                    } else {
                        handleFailedResponse(response)
                    }
                } catch (e: Exception) {
                    handleException(e)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                handleException(e)
            }
        })
    }

    private fun handleFailedResponse(response: Response) {
        Log.e("ER1", "Response not successful: ${response.code}")
        showToast("Failed to fetch data. Code: ${response.code}")
    }

    private fun handleException(e: Exception) {
        e.printStackTrace()
        runOnUiThread {
            showToast("Error: ${e.localizedMessage}")
        }
    }


    private fun handleResponse(selectedExercise: String?, responseObject: JSONArray) {
        try {
            for (i in 0 until responseObject.length()) {
                val exerciseObject = responseObject.getJSONObject(i)
                val name = exerciseObject.getString("name")

                if (name.equals(selectedExercise, ignoreCase = true)) {
                    val exerciseDetails = getExerciseDetails(exerciseObject)
                    showExerciseDetails(exerciseDetails)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        progressBar.visibility = View.GONE
    }

    private fun getExerciseDetails(exerciseObject: JSONObject): ExerciseDetails {
        return ExerciseDetails(
            exerciseObject.getString("name"),
            exerciseObject.getString("type"),
            exerciseObject.getString("muscle"),
            exerciseObject.getString("equipment"),
            exerciseObject.getString("difficulty"),
            exerciseObject.getString("instructions")
        )
    }

    private fun showExerciseDetails(exerciseDetails: ExerciseDetails) {
        Log.d(
            "ER2",
            "Exercise: ${exerciseDetails.name}, Type: ${exerciseDetails.type}, " +
                    "Muscle: ${exerciseDetails.muscle}, Equipment: ${exerciseDetails.equipment}, " +
                    "Difficulty: ${exerciseDetails.difficulty}, Instructions: ${exerciseDetails.instructions}"
        )
        // Set the text of TextViews based on exercise details
        exerciseNameTextView.text = "Exercise Name: ${exerciseDetails.name}"
        typeTextView.text = "Type: ${exerciseDetails.type}"
        muscleTextView.text = "Muscle: ${exerciseDetails.muscle}"
        equipmentTextView.text = "Equipment: ${exerciseDetails.equipment}"
        instructionsTextView.text = "Instructions: ${exerciseDetails.instructions}"

        main.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        val url = getUrlForExercise(exerciseDetails.name)
        openWebViewButton.setOnClickListener {
            showToast("Don't rotate the screen")
            showExerciseUrl(url)
        }
    }

    private val exampleUrl = "https://www.youtube.com/watch?v=HQfF5XRVXjU"

    private fun getUrlForExercise(exerciseName: String): String {
        val exerciseList = getExerciseList()

        for (exercise in exerciseList) {
            if (exerciseName.equals(exercise.optString("name"), ignoreCase = true)) {
                val actualUrl = exercise.optString("url")
                return if (isValidUrl(actualUrl)) actualUrl else exampleUrl
            }
        }

        return exampleUrl
    }

    private fun isValidUrl(url: String?): Boolean {
        return url?.startsWith("https://www.example.com") == false
    }

    private fun getExerciseList(): List<JSONObject> {
        val rawResource = resources.openRawResource(R.raw.exercise_data)
        val jsonString = rawResource.bufferedReader().use { it.readText() }

        val exerciseArray = try {
            JSONArray(jsonString)
        } catch (e: JSONException) {
            e.printStackTrace()
            JSONArray()
        }

        val exerciseList = mutableListOf<JSONObject>()
        for (i in 0 until exerciseArray.length()) {
            exerciseList.add(exerciseArray.optJSONObject(i) ?: JSONObject())
        }

        return exerciseList
    }

    private fun showExerciseUrl(url: String) {
        // Assuming you have a WebView with id 'yourWebView' in your layout
        val webView = findViewById<WebView>(R.id.yourWebView)

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: android.view.View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
            }
        }

        webView.loadUrl(url)

        webView.visibility = View.VISIBLE
        openWebViewButton.visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        if (webView.visibility == View.VISIBLE) {
            webView.visibility = View.GONE
            openWebViewButton.visibility = View.VISIBLE
        } else {
            val intent = Intent(this, AA1::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    data class ExerciseDetails(
        val name: String,
        val type: String,
        val muscle: String,
        val equipment: String,
        val difficulty: String,
        val instructions: String
    )
}