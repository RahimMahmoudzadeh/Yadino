package com.rahim.yadino.home.domain.useCase

import com.rahim.yadino.home.domain.model.Routine
import com.rahim.yadino.home.domain.repo.HomeRepository
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.reminder.ReminderScheduler
import com.rahim.yadino.base.reminder.ReminderState
import com.rahim.yadino.enums.message.MessageCode
import timber.log.Timber

class AddReminderUseCase(
    private val routineRepository: HomeRepository,
    private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: Routine): Resource<SuccessMessage, MessageCode> {
    try {
      val routine = routineModel.copy(
        idAlarm = routineRepository.getRoutineAlarmId(),
        colorTask = 0,
        timeInMillisecond = routineRepository.convertDateToMilSecond(
          routineModel.yearNumber,
          routineModel.monthNumber,
          routineModel.dayNumber,
          routineModel.timeHours,
        ),
      )
      val equalRoutine = routineRepository.checkEqualRoutine(routine)
      if (equalRoutine != null) {
        return Resource.Error(MessageCode.EQUAL_ROUTINE_MESSAGE)
      }
      val reminderState = reminderScheduler.setReminder(
        reminderName = routine.name,
        reminderExplanation = routine.explanation?:"",
        reminderId = routine.id ?: 0,
        reminderTime = routine.timeInMillisecond ?: 0,
        reminderIdAlarm = routine.idAlarm ?: 0,
      )
      return when (reminderState) {
        ReminderState.SetSuccessfully -> {
          routineRepository.addRoutine(routine)
          Resource.Success(SuccessMessage.SAVE_REMINDER)
        }

        is ReminderState.NotSet -> {
          Resource.Error(error = reminderState.errorMessage?: MessageCode.ERROR_SAVE_PROSES)
        }

        is ReminderState.PermissionsState -> {
          when {
            reminderState.reminderPermission && !reminderState.notificationPermission -> {
              Resource.Error(
                error = MessageCode.ERROR_NOTIFICATION_PERMISSION,
              )
            }

            !reminderState.reminderPermission && reminderState.notificationPermission -> {
              Resource.Error(
                error = MessageCode.ERROR_REMINDER_PERMISSION,
              )
            }

            else -> {
              Resource.Error(
                error = MessageCode.ERROR_NOTIFICATION_AND_REMINDER_PERMISSION,
              )
            }
          }
        }
      }
    } catch (e: Exception) {
      Timber.tag("addRoutine").d("error->${e.message}")
      return Resource.Error(error = MessageCode.ERROR_SAVE_PROSES)
    }
  }
}
