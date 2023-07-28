package com.rahim.data.alarm

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Vibrator

interface AlarmSong {
    //    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//    vibrator.vibrate(4000)
//
//    Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show()
//    var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//    if (alarmUri == null) {
//        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//    }
//
//    val ringtone = RingtoneManager.getRingtone(context, alarmUri)
//
//    // play ringtone
//
//    // play ringtone
//    ringtone.play()
    fun playRingtone(context:Context,alarmId: Int?)
    fun stopRingtone(ringtone: Ringtone?,context:Context,alarmId: Int?)
}