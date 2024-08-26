package com.rahim.data.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.rahim.data.model.routine.RoutineModel
import com.rahim.data.notification.NotificationManager
import com.rahim.utils.Constants.ALARM_MESSAGE
import com.rahim.utils.Constants.ROUTINE
import timber.log.Timber

class YadinoBroadCastReceiver() : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    sendNotificationAlarm(intent, context)
    Timber.tag("alarmReceiver").d(intent?.getStringExtra(ALARM_MESSAGE))
  }

  private fun sendNotificationAlarm(intent: Intent?, context: Context?) {
    intent?.extras?.let { extras ->
      val routineModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        extras.getParcelable(ROUTINE, RoutineModel::class.java)
      } else {
        extras.getParcelable(ROUTINE)
      }
      val nothing = NotificationManager()
      context?.let {
        Timber.tag("intentTitle").d("YadinoBroadCastReceiver-> ${routineModel?.name}")
        nothing.createFullNotification(context, routineModel)
      }
    }
  }
}
