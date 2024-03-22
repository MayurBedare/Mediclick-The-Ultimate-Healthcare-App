package com.example.mediclick

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.airbnb.lottie.LottieAnimationView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class NV2 : NV1() {

    private lateinit var progressBar: LottieAnimationView
    private lateinit var constraintLayout: ConstraintLayout
    private val apiKey = "T61f2/PVbqLwoFNOmne84g==uVedQjezuMAZp1fB"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nv2)

        constraintLayout = findViewById(R.id.CL)
        progressBar = findViewById(R.id.progressBar)

        val userQuery = intent.getStringExtra("userQuery")
        Log.d("NutritionalAnalysis", "$userQuery")

        if (isValidQuery(userQuery)) {
            fetchNutritionData(userQuery)
        } else {
            displayInvalidQueryError()
        }

        val backButton: ImageView = findViewById(R.id.imageNext)

        backButton.setOnClickListener {
            val intent = Intent(this, NV1::class.java)
            startActivity(intent)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, NV1::class.java)
        startActivity(intent)
        finish()
    }

    private fun isValidQuery(userQuery: String?): Boolean {
        return !userQuery.isNullOrBlank() && !userQuery.contains(",") && userQuery.length <= 1500
    }

    private fun fetchNutritionData(userQuery: String?) {
        val apiUrl = "https://api.api-ninjas.com/v1/nutrition?query=$userQuery"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("X-Api-Key", apiKey)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseBody = response.body?.string()
                    Log.d("NutritionalAnalysis", "JSON Response: $responseBody") // Log the JSON response with the appropriate tag

                    if (response.isSuccessful) {
                        val jsonArray = JSONArray(responseBody)
                        runOnUiThread {
                            try {
                                logExtractedData(jsonArray)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                            // Hide the progress bar after fetching and processing data
                            progressBar.visibility = View.GONE
                            constraintLayout.visibility = View.VISIBLE
                        }
                    } else {
                        Log.d("NutritionalAnalysis", "Response not successful: ${response.code}")
                        // Handle unsuccessful response (e.g., show error message to user)
                        // You can log the error response body for debugging
                        val errorBody = response.body?.string()
                        Log.d("NutritionalAnalysis", "Error Response Body: $errorBody")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    // Handle IO exception (e.g., network error)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Handle JSON exception (e.g., parsing error)
                }
            }


            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Handle failure (e.g., network error)
            }
        })
    }


    private fun logExtractedData(jsonArray: JSONArray) {
        val dynamicLinearLayouts = arrayOfNulls<LinearLayout>(12)
        dynamicLinearLayouts[0] = findViewById(R.id.L0)
        dynamicLinearLayouts[1] = findViewById(R.id.L1)
        dynamicLinearLayouts[2] = findViewById(R.id.L2)
        dynamicLinearLayouts[3] = findViewById(R.id.L3)
        dynamicLinearLayouts[4] = findViewById(R.id.L4)
        dynamicLinearLayouts[5] = findViewById(R.id.L5)
        dynamicLinearLayouts[6] = findViewById(R.id.L6)
        dynamicLinearLayouts[7] = findViewById(R.id.L7)
        dynamicLinearLayouts[8] = findViewById(R.id.L8)
        dynamicLinearLayouts[9] = findViewById(R.id.L9)
        dynamicLinearLayouts[10] = findViewById(R.id.L10)
        dynamicLinearLayouts[11] = findViewById(R.id.L11)

        val itemCount = jsonArray.length()

        val nutrientKeys = arrayOf(
            "name", "serving_size_g", "calories", "sugar_g", "protein_g", "cholesterol_mg",
            "sodium_mg", "carbohydrates_total_g", "fiber_g", "potassium_mg", "fat_total_g", "fat_saturated_g"
        )

        for (i in 0 until jsonArray.length()) {
            val itemObject = jsonArray.getJSONObject(i)

            for (nutrientKey in nutrientKeys) {
                val itemLayout = findLayoutForNutrient(dynamicLinearLayouts, nutrientKey)
                    ?: continue // Skip this iteration if layout is null
                var nutrientValue = itemObject.getString(nutrientKey)

                nutrientValue = when {
                    nutrientKey.contains("_g") -> "$nutrientValue g"
                    nutrientKey.contains("_mg") -> "$nutrientValue mg"
                    else -> nutrientValue
                }

                if (nutrientKey == "name") {
                    addTextViewToLayout1(itemLayout, nutrientValue, itemCount)
                } else {
                    addTextViewToLayout(itemLayout, nutrientValue, itemCount)
                }
            }
        }
    }

    private fun findLayoutForNutrient(layouts: Array<LinearLayout?>, nutrientKey: String): LinearLayout? {
        return when (nutrientKey) {
            "name" -> layouts[0]
            "serving_size_g" -> layouts[1]
            "calories" -> layouts[2]
            "sugar_g" -> layouts[3]
            "protein_g" -> layouts[4]
            "cholesterol_mg" -> layouts[5]
            "sodium_mg" -> layouts[6]
            "carbohydrates_total_g" -> layouts[7]
            "fiber_g" -> layouts[8]
            "potassium_mg" -> layouts[9]
            "fat_total_g" -> layouts[10]
            "fat_saturated_g" -> layouts[11]
            else -> null
        }
    }

    private fun addTextViewToLayout1(layout: LinearLayout?, text: String, itemCount: Int) {
        // Create a new TextView
        val textView = TextView(this)

        // Set the layout parameters for the new TextView
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.layoutParams = layoutParams

        // Set the text for the TextView
        textView.text = text

        // Apply properties to match your desired style
        textView.gravity = Gravity.CENTER
        val typeface = ResourcesCompat.getFont(this, R.font.lato)
        textView.typeface = typeface

        // Set the text color to black
        val textColor = ContextCompat.getColor(this, R.color.black)
        textView.setTextColor(textColor)

        // Add the TextView to the layout
        layout?.addView(textView)

        // Adjust layout parameters for the new TextView
        val newLayoutParams = LinearLayout.LayoutParams(
            250, // width in pixels
            50   // height in pixels
        )
        newLayoutParams.gravity = Gravity.CENTER
        newLayoutParams.weight = 0f // No weight for the new TextView

        // Apply updated layout parameters to the new TextView
        textView.layoutParams = newLayoutParams

        // Calculate the new width increment based on the item count
        val existingTextViewWidth = layout?.childCount?.times(300) // Assuming fixed width of 250

        // Calculate the new width increment based on the item count and existing TextView width
        val widthIncrement = (itemCount - layout?.childCount!!) * 50

        // Calculate the new width for the parent layout
        val parentLayoutParams = layout.layoutParams
        if (parentLayoutParams != null) {
            parentLayoutParams.width = existingTextViewWidth?.plus(widthIncrement)!!
            layout.layoutParams = parentLayoutParams
        }
    }

    private fun addTextViewToLayout(layout: LinearLayout?, text: String, itemCount: Int) {
        // Create a new TextView
        val textView = TextView(this)

        // Set the layout parameters for the new TextView
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        textView.layoutParams = layoutParams

        // Set the text for the TextView
        textView.text = text

        // Apply properties to match your desired style
        textView.gravity = Gravity.CENTER
        val typeface = ResourcesCompat.getFont(this, R.font.lato)
        textView.typeface = typeface

        // Get the color from resources using ContextCompat
        val textColor = ContextCompat.getColor(this, R.color.white)
        textView.setTextColor(textColor)

        // Add the new TextView to the layout
        layout?.addView(textView)

        // Adjust layout parameters for the new TextView
        val newLayoutParams = textView.layoutParams as LinearLayout.LayoutParams
        newLayoutParams.width = 250 // Set width to 100 pixels
        newLayoutParams.height = 40 // Set height to 34 pixels
        newLayoutParams.gravity = Gravity.CENTER
        newLayoutParams.weight = 0f   // No weight for the first TextView

        // Apply updated layout parameters to the new TextView
        textView.layoutParams = newLayoutParams

        // Calculate the width of existing TextViews
        val existingTextViewWidth = layout?.childCount?.times(300) // Assuming fixed width of 250

        // Calculate the new width increment based on the item count and existing TextView width
        val widthIncrement = (itemCount - layout?.childCount!!) * 50

        // Calculate the new width for the parent layout
        val parentLayoutParams = layout.layoutParams
        if (parentLayoutParams != null) {
            parentLayoutParams.width = existingTextViewWidth?.plus(widthIncrement)!!
            layout.layoutParams = parentLayoutParams
        }
    }

    private fun displayInvalidQueryError() {
        Toast.makeText(this, "Invalid query entered", Toast.LENGTH_SHORT).show()
        finish()
    }
    // Rest of your code
}
