package com.rahim.data.service.notification

//class FirebaseNotificationService : FirebaseMessagingService() {
//  override fun onMessageReceived(remoteMessage: RemoteMessage) {
//    super.onMessageReceived(remoteMessage)
//    remoteMessage.data?.let {
//      val pendingIntent =
//        PendingIntent.getActivity(
//          this,
//          0,
//          Intent(this, MainActivity::class.java),
//          PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
//        )
//      remoteMessage.notification?.let {
//        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
//          .setSmallIcon(R.mipmap.ic_launcher_foreground)
//          .setContentTitle(it.title)
//          .setContentText(it.body)
//          .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//          .setContentIntent(pendingIntent)
//        with(NotificationManagerCompat.from(this)) {
//          if (ActivityCompat.checkSelfPermission(
//              this@FirebaseNotificationService,
//              Manifest.permission.POST_NOTIFICATIONS,
//            ) != PackageManager.PERMISSION_GRANTED
//          ) {
//            return
//          }
//          notify(Random().nextInt(), builder.build())
//        }
//      }
//    }
//  }
//
//  override fun onDeletedMessages() {
//    super.onDeletedMessages()
//  }
//}
