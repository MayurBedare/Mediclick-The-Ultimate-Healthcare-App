package com.example.mediclick

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

open class MS3 : MS2Crop() {

    private lateinit var resultTextView: TextView
    private lateinit var noteTextView: TextView
    private lateinit var bl: LinearLayout
    private lateinit var animationView: LottieAnimationView
    private lateinit var animationViewh: LottieAnimationView
    private lateinit var animationViewh1: LottieAnimationView
    private val apiToken = "apify_api_oAe9dBmGOx7zyRHqFWcvycOR9boqL34v3JhB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ms3)

        resultTextView = findViewById(R.id.medinf)
        animationView = findViewById(R.id.breatheView)
        animationViewh = findViewById(R.id.scan)
        animationViewh1 = findViewById(R.id.alert)
        noteTextView = findViewById(R.id.note)
        bl = findViewById(R.id.mia)

        // Replace "YOUR_IMAGE_URL" with the URL of the image you want to perform OCR on
        val imageUrl = intent.getStringExtra(IMAGE_URL_EXTRA)

        Log.d("HP3", "Image URL: $imageUrl")

        // Construct the Apify API endpoint for the OCR actor
        val endpoint = "https://api.apify.com/v2/acts/alexey~google-lens/runs?token=$apiToken"

        val backButton: ImageView = findViewById(R.id.imageNext)
        backButton.setOnClickListener {
            val intent = Intent(this, MS2::class.java)
            startActivity(intent)
            finish()
        }

        bl.setOnClickListener {
            // Handle the click action here, navigate to HealthTipsActivity
            val intent = Intent(this, ML1::class.java)
            startActivity(intent)
        }

        // Start animation
        animationView.playAnimation()
        animationView.visibility = View.VISIBLE


        // Create JSON object for actor input
        val inputJson = JSONObject().apply {
            put("startUrls", JSONArray().put(JSONObject().put("url", imageUrl)))
            put("proxy", JSONObject().put("useApifyProxy", true))
        }

        // Create the request
        val request = Request.Builder()
            .url(endpoint)
            .post(inputJson.toString().toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        // Make the HTTP request using OkHttp
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()

                    // Log the entire JSON file for debugging
                    Log.d("HP3", "Entire JSON File: $responseBody")

                    // Check if the response body is not null or empty
                    if (!responseBody.isNullOrBlank()) {
                        // Parse the JSON response
                        val jsonResponse = JSONObject(responseBody)
                        val runId = jsonResponse.getJSONObject("data").getString("id")

                        // Poll for actor run result after the actor finishes processing
                        pollActorRunResult(runId)
                    }
                } catch (e: Exception) {
                    // Handle JSON parsing exception
                    Log.e("HP3", "JSON Parsing Error: $e")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle the error
                Log.e("HP3", "Error: $e")
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        navigateToPreviousActivity()
    }
    private fun pollActorRunResult(runId: String) {
        val resultEndpoint = "https://api.apify.com/v2/acts/alexey~google-lens/runs/$runId?token=$apiToken"

        // Coroutine scope for background task
        CoroutineScope(Dispatchers.IO).launch {
            var isResultReceived = false
            while (isActive && !isResultReceived) {
                val request = Request.Builder()
                    .url(resultEndpoint)
                    .get()
                    .build()

                try {
                    val response = OkHttpClient().newCall(request).execute()
                    val responseBody = response.body?.string()

                    // Log the entire JSON result for debugging
                    Log.d("HP3", "Entire JSON Result: $responseBody")

                    // Check if the response body is not null or empty
                    if (!responseBody.isNullOrBlank()) {
                        // Parse the JSON response
                        val jsonResult = JSONObject(responseBody)
                        val status = jsonResult.getJSONObject("data").getString("status")

                        // Check if the actor run is finished
                        if (status == "SUCCEEDED") {
                            val datasetId = jsonResult.getJSONObject("data").getString("defaultDatasetId")
                            // Retrieve the JSON result after the actor finishes processing
                            retrieveJsonResult(datasetId)
                            isResultReceived = true
                        } else if (status == "FAILED" || status == "TIMED_OUT") {
                            // Handle if the actor run fails or times out
                            Log.e("HP3", "Actor run failed or timed out.")
                            isResultReceived = true
                        }
                    }

                    // Delay before making the next request
                    delay(5000) // Adjust the delay time as needed
                } catch (e: Exception) {
                    // Handle the error
                    Log.e("HP3", "Error: $e")
                }
            }
        }
    }

    private fun retrieveJsonResult(datasetId: String) {
        val resultEndpoint =
            "https://api.apify.com/v2/datasets/$datasetId/items?clean=true&format=json&limit=1000"

        val request = Request.Builder()
            .url(resultEndpoint)
            .get()
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()
                    Log.d("HP3", "Entire JSON Result: $responseBody")

                    if (!responseBody.isNullOrBlank()) {
                        val jsonArray = JSONArray(responseBody)

                        val extractedTexts = ArrayList<String>()

                        for (i in 0 until jsonArray.length()) {
                            val ocrText = jsonArray.getJSONObject(i).optString("ocrText")

                            if (!ocrText.isNullOrBlank()) {
                                // Extract text within quotes and add to the list
                                val textsWithinQuotes = extractTextWithinQuotes(ocrText)
                                extractedTexts.addAll(textsWithinQuotes)
                            }
                        }

                        // Update the UI on the main thread
                        runOnUiThread {
                            if (extractedTexts.isEmpty()) {
                                resultTextView.apply {
                                    text = "Sorry we don't able to extract medicines names right now"
                                    resultTextView.setTextColor(0xFFFF0000.toInt()) // Set text color to red (ARGB format)
                                }
                                animationView.cancelAnimation()
                                animationView.visibility = View.GONE
                                noteTextView.visibility = View.GONE
                                animationViewh1.playAnimation()
                                animationViewh1.visibility = View.VISIBLE
                                bl.visibility = View.VISIBLE
                            } else {
                                resultTextView.text = extractedTexts.joinToString("\n")
                                // Stop animation
                                animationView.cancelAnimation()
                                animationView.visibility = View.GONE
                                noteTextView.visibility = View.GONE
                                animationViewh.playAnimation()
                                animationViewh.visibility = View.VISIBLE
                                bl.visibility = View.VISIBLE
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("HP3", "Error: $e")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle the error
                Log.e("HP3", "Error: $e")
            }
        })
    }

    private fun extractTextWithinQuotes(text: String): List<String> {
        val regex = "\"([^\"]*)\"".toRegex()
        return regex.findAll(text).map { it.groupValues[1] }.toList()
    }

    private fun navigateToPreviousActivity() {
        val intent = Intent(this, MS2::class.java)
        startActivity(intent)
        finish()
    }
}
