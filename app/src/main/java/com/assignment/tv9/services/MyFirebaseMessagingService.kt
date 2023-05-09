package com.assignment.tv9.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.assignment.tv9.MainActivity
import com.assignment.tv9.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.ThreadLocalRandom

const val channelId = "notification_channel"
const val channelName = "com.assignment.tv9.services"

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "Service"

    fun getToken(context: Context): String? {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcmToken", "empty")
    }
    override fun onNewToken(s: String) {
        super.onNewToken(s!!)
        Log.e("new firebase token", s)
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcmToken", s).apply()
        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.v("TV9 Message Received","New notification $remoteMessage")
        generateNotification(
            remoteMessage.data["title"]?:"",
            remoteMessage.data["body"]?:"",
            applicationContext
        )
    }

    fun generateNotification(title:String,message:String, context: Context) {
        var builder: NotificationCompat.Builder =NotificationCompat.Builder(context,
            channelId)
            .setSmallIcon(R.drawable.tv9_logo)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentTitle(title)
            .setContentText(message)
        builder.setContentIntent(returnDeeplinkIntent(R.id.detailsfragment,context,Bundle()))

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(ThreadLocalRandom.current().nextInt(1,500),builder.build())
    }

    private fun getRemoteView(title: String, message: String, context: Context): RemoteViews? {
        val remoteView = RemoteViews(context.packageName,R.layout.notification )

        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.tv9_logo)

        return remoteView
    }

    fun returnDeeplinkIntent(fragmentId:Int, context: Context, args: Bundle): PendingIntent {
        val iUniqueId = (System.currentTimeMillis() and 0xfffffff).toInt()
        return NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(fragmentId)
            .setArguments(args)
            .createPendingIntent()
//            .createTaskStackBuilder()
//            .getPendingIntent(
//                iUniqueId,
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//                }else PendingIntent.FLAG_UPDATE_CURRENT
//            )!!
    }

}