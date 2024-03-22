package com.example.mediclick.presentation.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mediclick.FeatureActivity
import com.example.mediclick.R
import com.example.mediclick.databinding.BMIBinding
import com.example.mediclick.presentation.adapter.WeightPickerAdapter

open class BMI : FeatureActivity() {

    private lateinit var binding: BMIBinding
    private val _binding get() = binding

    private lateinit var weightAdapter: WeightPickerAdapter
    private var gender = 'M'
    private var height = 1
    private var weight = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.b_m_i)

        setupUI()
        setListeners()

        _binding.startButton.setOnActiveListener {
            animationViewUp()
            _binding.startButton.alpha = 0f
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToResultActivity()
            }, 500)
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

    private fun setupUI() {
        // Gender
        val titlesOfGender: List<String> = listOf("F", "O", "M")

        _binding.genderWheelView.apply {
            titles = titlesOfGender
            elevation = 0f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                isFocusedByDefault = true
            }
            isSelected = true
            focusedIndex = 2
        }

        // Weight
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.scrollToPositionWithOffset(49, 0)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(_binding.weightRecyclerBtn)

        weightAdapter = WeightPickerAdapter(this, getData(151), _binding.weightRecyclerBtn)

        _binding.weightRecyclerBtn.apply {
            this.layoutManager = layoutManager
            adapter = weightAdapter
            isSelected = true
            requestFocus()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                isFocusedByDefault = true
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val centerView = snapHelper.findSnapView(layoutManager)
                        val position = layoutManager.getPosition(centerView!!)

                        // Ensure position is within bounds
                        if (position in 0 until weightAdapter.itemCount) {
                            weight = Integer.parseInt(weightAdapter.dataList[position])
                        }
                    }
                }
            })
        }

        // Height
        _binding.heightWheel.onWheelChangedListener =
            com.cncoderx.wheelview.OnWheelChangedListener { view, _, newIndex ->
                val text = view.getItem(newIndex)
                height = Integer.parseInt(text.toString())
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _binding.heightWheel.defaultFocusHighlightEnabled = true
        }
    }

    private fun setListeners() {
        _binding.genderWheelView.selectListener = {
            gender = when (it) {
                0 -> 'F'
                1 -> 'O'
                else -> 'M'
            }
        }
    }

    private fun getData(count: Int): List<String> {
        return (0 until count).map { it.toString() }
    }

    private fun animationViewUp() {
        _binding.apply {
            iView.animate().translationY(0f).alpha(0f).setDuration(50).setStartDelay(0).start()
            bodyContainer.animate().translationY(-250f).alpha(0f).setDuration(500).setStartDelay(0).start()
            footerContainer.animate().translationY(-250f).alpha(0f).setDuration(500).setStartDelay(50).start()
            heightWheel.animate().translationY(-250f).alpha(0f).setDuration(500).setStartDelay(100).start()
            weightRecyclerBtn.animate().translationX(-250f).alpha(0f).setDuration(500).setStartDelay(150).start()
        }
    }

    private fun navigateToResultActivity() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("Height", height.toDouble())
        intent.putExtra("Weight", weight.toDouble())
        intent.putExtra("Gender", if (gender == 'M') 0 else 1)
        startActivity(intent)
    }

}
