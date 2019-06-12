package com.example.roexchange

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MessagingService: FirebaseMessagingService(){
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        showNotification(p0!!.notification!!.title!!, p0!!.notification!!.body!!)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)

    }

    fun showNotification(Title: String, Body: String){
        var notificationManager:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var NOTIFICATION_CHANNEL_ID = "com.example.ROExchange"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationManager.createNotificationChannel(notificationChannel)

        }

        var notificationBuilder = NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle(Title)
                .setContentText(Body)
                .setContentInfo("info")
                .setStyle(NotificationCompat.BigTextStyle().bigText(Body))
        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }
}


private operator fun String.invoke(s: String) {

}
