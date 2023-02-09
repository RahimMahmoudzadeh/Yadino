package com.rahim.data.service.notification

import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//                var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.ic_launcher_foreground)
//                    .setContentTitle(it.title)
//                    .setContentText(it.body)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                with(NotificationManagerCompat.from(this)) {
//                    if (ActivityCompat.checkSelfPermission(
//                            this,
//                            Manifest.permission.POST_NOTIFICATIONS
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        return
//                    }
//                    notify(Random().nextInt(), builder.build())
//                }
            }

        }

    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}