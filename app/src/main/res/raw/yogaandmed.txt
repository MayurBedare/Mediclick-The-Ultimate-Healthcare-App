package com.example.mediclick

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class YogaAndMeditation : FeatureActivity() {

    private lateinit var webView: WebView
    private lateinit var rotateIcon: ImageView
    private lateinit var text: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.yoga_and_meditation)

        webView = findViewById(R.id.webView1)
        rotateIcon = findViewById(R.id.rotateIcon)
        text = findViewById(R.id.textView)

        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                showFullScreenMessage()
            }

        }

        val youtubeVideoUrl = "https://www.youtube.com/embed/dAqQqmaI9vY"
        webView.loadUrl(youtubeVideoUrl)

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.isFocused && webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun showFullScreenMessage() {
        rotateIcon.visibility = ImageView.VISIBLE
        text.visibility = ImageView.VISIBLE

        Toast.makeText(
            applicationContext,
            "Please do not click on the minimize icon.",
            Toast.LENGTH_LONG
        ).show()

        val snackbar = Snackbar.make(
            webView,
            "Rotate the screen",
            Snackbar.LENGTH_LONG
        )
        snackbar.show()

        rotateIcon.setOnClickListener {
            showFullScreenMessage()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
    }
}
