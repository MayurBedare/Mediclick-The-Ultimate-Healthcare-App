package com.example.mediclick

import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ML2 : AppCompatActivity() {

    private lateinit var webView: WebView
    private val pageHistory: Stack<String> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ml2)

        webView = findViewById(R.id.webView)

        // Enable JavaScript (if needed)
        webView.settings.javaScriptEnabled = true

        // Set a WebViewClient to handle events inside the WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // Handle links within the WebView itself
                val url = request?.url.toString()
                view?.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Handle page finished events if needed

                // Check if the current URL is different from the one at the top of the stack
                if (url != null && !pageHistory.isEmpty() && url != pageHistory.peek()) {
                    // Add the loaded URL to the history stack
                    pageHistory.push(url)
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                // Handle errors during page loading
            }
        }

        // Get user input (replace "yourEditTextId" with the actual ID of your EditText)
        val userInput = "paracetamol"// Get user input from EditText

        // Construct the URL based on user input
        val url = "https://www.webmd.com/drugs/2/index"

        // Load the constructed URL in the WebView
        webView.loadUrl(url)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            // If there's history, go back to the previous page of the current URL
            webView.goBack()
        } else {
            // If no history, perform the default back action (finish the activity)
            super.onBackPressed()
        }
    }
}
