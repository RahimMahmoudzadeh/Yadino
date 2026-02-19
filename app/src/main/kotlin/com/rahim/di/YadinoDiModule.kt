package com.rahim.di

import android.app.AlarmManager
import android.content.Context
import com.rahim.data.reminder.notification.NotificationManager
import com.rahim.data.reminder.ReminderSchedulerImpl
import com.rahim.data.reminder.alarm.ControlAlarm
import com.rahim.data.reminder.alarm.ControlAlarmImplementation
import com.rahim.yadino.base.reminder.ReminderScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val YadinoDiModule = module {
  single<ControlAlarm> { ControlAlarmImplementation() }
  single<ReminderScheduler> { ReminderSchedulerImpl(alarmManager = get(), context = androidContext()) }
//  single<MainComponent> { MainComponentImpl() }
  single { androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
  single { NotificationManager(get()) }
}
