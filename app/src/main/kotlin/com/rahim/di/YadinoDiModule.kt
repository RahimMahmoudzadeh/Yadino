package com.rahim.di

import android.app.AlarmManager
import android.content.Context
import com.rahim.data.reminder.NotificationManager
import com.rahim.data.reminder.ReminderSchedulerImpl
import com.rahim.data.reminder.YadinoBroadCastReceiver
import com.rahim.data.reminder.alarm.ControlAlarm
import com.rahim.data.reminder.alarm.ControlAlarmImplementation
import com.rahim.ui.main.MainViewModel
import com.rahim.yadino.base.reminder.ReminderScheduler
import com.rahim.yadino.enums.DispatchersQualifier
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val YadinoDiModule = module {
  single<ControlAlarm> { ControlAlarmImplementation() }
  single<ReminderScheduler> { ReminderSchedulerImpl(alarmManager = get(), context = androidContext()) }
  single { androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
  factory { NotificationManager(get()) }
  factory { YadinoBroadCastReceiver(get()) }
  viewModel { MainViewModel(dateTimeRepository = get(), repositoryRoutine = get(), noteRepository = get(), appDistributionActions = get(), ioDispatcher = get((named(DispatchersQualifier.IO))), sharedPreferencesRepository = get()) }
}
