package com.yadino.routine.domain.useCase

import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.reminder.ReminderScheduler
import com.rahim.yadino.base.reminder.ReminderState
import com.rahim.yadino.enums.SuccessMessage
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.yadino.routine.domain.repo.RoutineRepository
import com.yadino.routine.domain.model.Routine
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(
  private val routineRepository: RoutineRepository,
  private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: Routine): Resource<SuccessMessage, ErrorMessageCode> {
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
        return Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE)
      }
      val reminderState = reminderScheduler.setReminder(
        routine.name,
        routine.id ?: 0,
        routine.timeInMillisecond ?: 0,
        routine.idAlarm ?: 0,
      )
      return when (reminderState) {
        ReminderState.SetSuccessfully -> {
          routineRepository.addRoutine(routine)
          Resource.Success(SuccessMessage.SAVE_REMINDER)
        }

        is ReminderState.NotSet -> {
          Resource.Error(reminderState.errorMessage?: ErrorMessageCode.ERROR_SAVE_PROSES)
        }

        is ReminderState.PermissionsState -> {
          when {
            reminderState.reminderPermission && !reminderState.notificationPermission -> {
              Resource.Error(
                error = ErrorMessageCode.ERROR_NOTIFICATION_PERMISSION,
              )
            }

            !reminderState.reminderPermission && reminderState.notificationPermission -> {
              Resource.Error(
                error = ErrorMessageCode.ERROR_REMINDER_PERMISSION,
              )
            }

            else -> {
              Resource.Error(
                error = ErrorMessageCode.ERROR_NOTIFICATION_AND_REMINDER_PERMISSION,
              )
            }
          }
        }
      }
    } catch (e: Exception) {
      return Resource.Error(error = ErrorMessageCode.ERROR_SAVE_PROSES)
    }
  }
}
