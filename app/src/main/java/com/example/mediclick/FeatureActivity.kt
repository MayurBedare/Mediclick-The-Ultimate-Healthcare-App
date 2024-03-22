package com.example.mediclick

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.mediclick.presentation.activity.BMI
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

open class FeatureActivity : LoginAddUser() {
    private lateinit var animationView: LottieAnimationView

    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature)

        // Initialize views
        val myButtonw: LinearLayout = findViewById(R.id.bmia)
        val myButtonu: ImageView = findViewById(R.id.imageView21)
        val myButtond: ImageView = findViewById(R.id.imageView22)
        val myButtont: ImageView = findViewById(R.id.imageView23)
        val myButtonq: ImageView = findViewById(R.id.imageView24)
        val myButtonc: ImageView = findViewById(R.id.imageView26)
        val myButtony: ImageView = findViewById(R.id.imageView25)
        val image: CircleImageView = findViewById(R.id.image)

        // Retrieve image URI from intent
        val profileImageUri: Uri? = intent.getParcelableExtra("profileImageUri")

        // Set profile image URI to CircleImageView using Picasso
        profileImageUri?.let {
            Picasso.get().load(it).into(image)
        }

        // Initialize LottieAnimationView
        animationView = findViewById(R.id.featureAnimationView)

        // Play the animation
        animationView.playAnimation()
        animationView.visibility = View.VISIBLE

        // Delay for 3 seconds and then set visibility to GONE
        animationView.postDelayed({
            animationView.visibility = android.view.View.GONE
        }, 3000)

        // Set onClickListeners for the buttons
        myButtonw.setOnClickListener {
            val intent = Intent(this, BMI::class.java)
            startActivity(intent)
        }
        myButtonu.setOnClickListener {
            val intent = Intent(this, MS1::class.java)
            startActivity(intent)
        }
        myButtond.setOnClickListener {
            val intent = Intent(this, NV1::class.java)
            startActivity(intent)
        }
        myButtont.setOnClickListener {
            val intent = Intent(this, ML1::class.java)
            startActivity(intent)
        }
        myButtonq.setOnClickListener {
            val intent = Intent(this, AC1::class.java)
            startActivity(intent)
        }
        myButtonc.setOnClickListener {
            val intent = Intent(this, AA1::class.java)
            startActivity(intent)
        }
        myButtony.setOnClickListener {
            val intent = Intent(this, IP1::class.java)
            startActivity(intent)
        }
        image.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed() // Call the super method first


        if (doubleBackToExitPressedOnce) {
            onBackPressedDispatcher.onBackPressed()
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}