package com.example.mediclick

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mediclick.helper.NotificationReceiver
import com.google.android.material.card.MaterialCardView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import java.util.*

open class IP2 : IP1() {

    private lateinit var currentUser: FirebaseUser
    private lateinit var alarmManager: AlarmManager
    private lateinit var calendar: Calendar
    private lateinit var picker: MaterialTimePicker
    private lateinit var database: DatabaseReference
    private lateinit var viewModel: HomeViewModel

    private val NOTIFICATION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ip2)

        currentUser = FirebaseAuth.getInstance().currentUser ?: return

        val uid = currentUser.uid
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.init(this, uid)

        updateReportTexts()
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        findViewById<MaterialCardView>(R.id.button).setOnClickListener {
            startActivity(Intent(this, MeditationActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.breathe).setOnClickListener {
            startActivity(Intent(this, BreathActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.mia).setOnClickListener {
            startActivity(Intent(this, IP3::class.java))
        }

        updateReportProgress()

        findViewById<Button>(R.id.remind).setOnClickListener {
            showTimePicker()
        }
    }

    private fun updateReportTexts() {
        findViewById<TextView>(R.id.val_meditate_times).text = viewModel.getMeditationCount().toString()
        findViewById<TextView>(R.id.val_meditate).text = viewModel.getMeditationMin().toString()
        findViewById<TextView>(R.id.val_breathe_times).text = viewModel.getBreatheCount().toString()
        findViewById<TextView>(R.id.val_breathe).text = viewModel.getBreatheMin().toString()
    }

    private fun updateReportProgress() {
        val medMin = viewModel.getMeditationMin()
        val breMin = viewModel.getBreatheMin()
        val medCount = viewModel.getMeditationCount()
        val breCount = viewModel.getBreatheCount()
        val percentage = if (medCount > 0 && breCount > 0) {
            (medMin + breMin) * 100 / (medCount * 20) + (breCount * 3)
        } else {
            1
        }
        findViewById<ProgressBar>(R.id.statusBar).progress = percentage
        updateReportTexts()
    }

    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Reminder time")
            .build()

        picker.show(supportFragmentManager, "GENO")

        picker.addOnPositiveButtonClickListener {
            scheduleReminder()
        }
    }

    private fun scheduleReminder() {
        calendar = Calendar.getInstance()
        cancelAlarm()

        calendar.set(Calendar.HOUR_OF_DAY, picker.hour)
        calendar.set(Calendar.MINUTE, picker.minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        showReminderSetToast()
    }

    private fun cancelAlarm() {
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

    private fun showReminderSetToast() {
        showToast("Successfully set reminder at: ${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)} every day.")
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
