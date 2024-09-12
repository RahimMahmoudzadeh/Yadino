package com.rahim.yadino.routine.wekeup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rahim.yadino.base.Constants
import com.rahim.yadino.base.Constants.KEY_LAUNCH_ID
import com.rahim.yadino.base.Constants.KEY_LAUNCH_NAME
//import com.rahim.broadcast.NotificationManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject
@AndroidEntryPoint
class Broad : BroadcastReceiver() {

//    @Inject
//    lateinit var notificationManager: com.rahim.broadcast.NotificationManager

    override fun onReceive(p0: Context?, intent: Intent?) {
        if (intent?.action != Constants.ACTION_SEND_NOTIFICATION) return
        val reminderName = intent.getStringExtra(KEY_LAUNCH_NAME)
        val reminderId = intent.getIntExtra(KEY_LAUNCH_ID, 0)
        Timber.tag("yadinoBroadcast").d("WakeupActivity reminderName->$reminderName")
        Timber.tag("yadinoBroadcast").d("WakeupActivity reminderId->$reminderId")
        p0?.let {
//            notificationManager.createFullNotification(
//                context = it,
//                routineName = reminderName ?: "",
//                routineIdAlarm = reminderId.toLong(),
//                routineExplanation = ""
//            )
        }
    }
}