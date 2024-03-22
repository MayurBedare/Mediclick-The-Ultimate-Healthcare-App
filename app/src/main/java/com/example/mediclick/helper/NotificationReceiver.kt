package com.example.mediclick.helper

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mediclick.MeditationActivity

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val repeatingIntent = Intent(context, MeditationActivity::class.java)
        repeatingIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 200, repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "MORNING")
            .setContentIntent(pendingIntent)
//            .setSmallIcon(R.drawable.about)
            .setContentTitle("Ding!!! ")
            .setContentText("It is time to Meditate! Click here to start meditating.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context, // Use the context parameter
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Handle the case where the permission is not granted.
            return
        }
        notificationManager.notify(200, builder.build())
    }
}
