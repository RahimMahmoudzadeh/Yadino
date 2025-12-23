package com.rahim.yadino.home.domain.useCase

import com.rahim.yadino.home.domain.model.Routine
import com.rahim.yadino.home.domain.repo.HomeRepository
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.reminder.ReminderScheduler
import com.rahim.yadino.base.reminder.ReminderState
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.enums.message.success.SuccessMessage
import timber.log.Timber

class AddReminderUseCase(
    private val routineRepository: HomeRepository,
    private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: Routine): Resource<SuccessMessage, ErrorMessage> {
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
        return Resource.Error(ErrorMessage.EQUAL_ROUTINE_MESSAGE)
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
          Resource.Error(error = reminderState.errorMessage?: ErrorMessage.SAVE_PROSES)
        }

        is ReminderState.PermissionsState -> {
          when {
            reminderState.reminderPermission && !reminderState.notificationPermission -> {
              Resource.Error(
                error = ErrorMessage.NOTIFICATION_PERMISSION,
              )
            }

            !reminderState.reminderPermission && reminderState.notificationPermission -> {
              Resource.Error(
                error = ErrorMessage.REMINDER_PERMISSION,
              )
            }

            else -> {
              Resource.Error(
                error = ErrorMessage.NOTIFICATION_AND_REMINDER_PERMISSION,
              )
            }
          }
        }
      }
    } catch (e: Exception) {
      Timber.tag("addRoutine").d("error->${e.message}")
      return Resource.Error(error = ErrorMessage.SAVE_PROSES)
    }
  }
}
