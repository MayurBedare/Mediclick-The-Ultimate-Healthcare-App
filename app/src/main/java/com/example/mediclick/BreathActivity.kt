package com.example.mediclick

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import java.util.concurrent.TimeUnit

class BreathActivity : AppCompatActivity() {
    private lateinit var textIndicator: TextView
    private lateinit var timer: CountDownTimer
    private var isRunning = false
    private lateinit var viewModel: HomeViewModel
    private lateinit var currentUser: FirebaseUser
    private var minutes = 3L
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breath)

        currentUser = FirebaseAuth.getInstance().currentUser!!
        val uid = FirebaseAuth.getInstance().uid
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.init(applicationContext, uid!!)

        textIndicator = findViewById(R.id.indicator)
        timer = createTimer()

        findViewById<Button>(R.id.start).setOnClickListener {
            toggle()
        }

        findViewById<ImageButton>(R.id.close).setOnClickListener {
            showDialog(this)
        }
    }

    private fun toggle() {
        if (isRunning) {
            stopExercise()
            findViewById<Button>(R.id.start).text = "Start"
        } else {
            findViewById<LottieAnimationView>(R.id.breathe).playAnimation()
            findViewById<Button>(R.id.start).text = getString(R.string.str_end)
            timer.start()
        }
    }

    private fun createTimer(): CountDownTimer {
        return object : CountDownTimer(3 * 60000, 1000) {
            var sec = 0L

            override fun onTick(ms: Long) {
                isRunning = true
                minutes =
                    TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(ms)
                    )
                sec = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(ms)
                )

                textIndicator.text =
                    "${minutes.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"

                if (minutes == 2L && sec == 57L) {
                    tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                        if (it == TextToSpeech.SUCCESS) {
                            tts.language = Locale.US
                            tts.setSpeechRate(0.8F)
                            tts.speak(
                                "Inhale through your nose and exhale through your mouth",
                                TextToSpeech.QUEUE_ADD,
                                null
                            )
                        }
                    })
                }
            }

            override fun onFinish() {
                stopExercise()
            }
        }
    }

    private fun stopExercise() {
        findViewById<LottieAnimationView>(R.id.breathe).pauseAnimation()
        isRunning = false
        timer.cancel()
    }

    private fun showDialog(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setMessage("Do you want to stop breathing exercise ?").setCancelable(true)
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            finish()
        })

        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        val alert = builder.create()
        alert.setTitle("Are you sure")
        alert.show()
    }

    override fun onStop() {
        super.onStop()

        viewModel.updateBreatheMin((3L - minutes).toInt())
        viewModel.updateBreatheCount()
        timer.cancel()
    }
}
