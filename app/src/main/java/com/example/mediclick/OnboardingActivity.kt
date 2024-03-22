package com.example.mediclick

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton

open class OnboardingActivity : MainActivity() {
    private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
    private lateinit var indicatorContainer: LinearLayout
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        preferences = applicationContext.getSharedPreferences("ONBOARD", Context.MODE_PRIVATE)

        setOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)
    }

    private fun setOnboardingItems(){
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.ocr1,
                    title = "MedScan Vision",
                    description = "Simply scan your medical prescriptions, and watch as it effortlessly unveils the names of your medicines. Your hassle-free solution for quick and accurate medication details"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.nutrition1,
                    title = "Nutri View",
                    description = " Elevate your nutrition game with NutriView – your personalized dietary companion. Instant insights into your meals, making healthy choices a breeze. Your key to a well-nourished, balanced lifestyle"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.medi1,
                    title = "Medi Learn",
                    description = "Unlock the mysteries of your medications with MediLearn. Quick, reliable insights into your prescriptions. Your go-to source for understanding and managing your medicines effortlessly"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.air1,
                    title = "Air Checker",
                    description = "Keep an eye on the air quality with Air Checker – your environmental companion. Instant pollution forecasts, real-time updates. Your pocket environmentalist for a seamless day"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.exercise1,
                    title = "Exercise Coach",
                    description = "Level up your fitness game with Exercise Coach, your pocket-sized workout guru. Tailored routines, expert guidance – it's your path to a fitter you, courtesy of Mediclick"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.meditation13,
                    title = "Inner Path",
                    description = "Effortlessly blend exercise and meditation, finding your inner balance for a healthier, happier you. Embrace a journey of self-discovery with Mediclick's InnerPath"
                )
            )
        )
        val onboardingViewPager = findViewById<ViewPager2>(R.id.onboardingViewPager)
        onboardingViewPager.adapter = onboardingItemsAdapter
        onboardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (onboardingViewPager.getChildAt(0) as RecyclerView). overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
        findViewById<LottieAnimationView>(R.id.breatheView).setOnClickListener{
            if (onboardingViewPager.currentItem+1 < onboardingItemsAdapter.itemCount){
                onboardingViewPager.currentItem += 1
            } else {
                setCompleteOnboarding()
            }
        }
        findViewById<TextView>(R.id.textSkip).setOnClickListener{
            navigateToHomeActivity()
        }
        findViewById<LinearLayout>(R.id.oa).setOnClickListener{
            navigateToHomeActivity()
            setCompleteOnboarding()
        }

    }

    private fun navigateToHomeActivity() {
        startActivity(Intent(applicationContext,SignMainActivity::class.java))
        finish()
    }

    private fun setCompleteOnboarding() {
        preferences.edit().putBoolean("ISCOMPLETE", true).apply()
    }


    private fun setupIndicators(){
        indicatorContainer = findViewById(R.id.indicatorsContainer)
        val indicators = arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT )
        layoutParams.setMargins(8,0,8,0,)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_background
                    )
                )
                it.layoutParams = layoutParams
                indicatorContainer.addView(it)
            }
        }
    }
    private fun setCurrentIndicator (position: Int){
        val childCount = indicatorContainer.childCount
        for( i  in 0 until childCount){
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            imageView.scaleType = ImageView.ScaleType.FIT_END
            val layoutParams: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT )
            imageView.layoutParams = layoutParams
            if(i == position){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_background
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_backgound
                    )
                )
            }
        }
    }
}