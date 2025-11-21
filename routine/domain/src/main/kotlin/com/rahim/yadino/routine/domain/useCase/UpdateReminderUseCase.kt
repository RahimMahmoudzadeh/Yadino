package com.rahim.yadino.routine.domain.useCase

import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.reminder.ReminderScheduler
import com.rahim.yadino.base.reminder.ReminderState
import com.rahim.yadino.enums.message.success.SuccessMessage
import com.rahim.yadino.enums.message.MessageCode
import com.rahim.yadino.routine.domain.model.Routine
import com.rahim.yadino.routine.domain.repo.RoutineRepository

class UpdateReminderUseCase(
  private val routineRepository: RoutineRepository,
  private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routine: Routine): Resource<SuccessMessage, MessageCode> {
    try {
      reminderScheduler.cancelReminder(routine.idAlarm ?: 0)
      val routine = routine.copy(
        idAlarm = routineRepository.getRoutineAlarmId(),
        colorTask = 0,
        timeInMillisecond = routineRepository.convertDateToMilSecond(
          routine.yearNumber,
          routine.monthNumber,
          routine.dayNumber,
          routine.timeHours,
        ),
      )
      val equalRoutine = routineRepository.checkEqualRoutine(routine)
      if (equalRoutine != null) {
        return Resource.Error(MessageCode.EQUAL_ROUTINE_MESSAGE)
      }
      val reminderState = reminderScheduler.setReminder(
        reminderName = routine.name,
        reminderExplanation = routine.explanation ?: "",
        reminderId = routine.id ?: 0,
        reminderTime = routine.timeInMillisecond ?: 0,
        reminderIdAlarm = routine.idAlarm ?: 0,
      )
      return when (reminderState) {
        ReminderState.SetSuccessfully -> {
          routineRepository.addRoutine(routine)
          Resource.Success(SuccessMessage.UPDATE_REMINDER)
        }

        is ReminderState.NotSet -> {
          Resource.Error(reminderState.errorMessage ?: MessageCode.ERROR_SAVE_PROSES)
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
      return Resource.Error(error = MessageCode.ERROR_SAVE_PROSES)
    }
  }
}
