package com.example.totw

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
const val notificationID = 121
class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Show the notification
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, channelID)
            .setContentTitle("Top O' The World")
            .setContentText("Check out our newest articles!")
            .setSmallIcon(R.drawable.logo_new)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationID, notification)
    }
}