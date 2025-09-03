package com.yadino.routine.domain.useCase

import com.rahim.yadino.Resource
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.yadino.routine.domain.ReminderScheduler
import com.yadino.routine.domain.RoutineRepository
import com.yadino.routine.domain.model.ReminderState
import com.yadino.routine.domain.model.RoutineModel
import javax.inject.Inject

class UpdateReminderUseCase @Inject constructor(
  private val routineRepository: RoutineRepository,
  private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineModel): Resource<Nothing?> {
    try {
      reminderScheduler.cancelReminder(routineModel.idAlarm ?: 0)
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
          Resource.Success(null)
        }

        is ReminderState.NotSet -> {
          Resource.Error(reminderState.errorMessage)
        }

        is ReminderState.PermissionsState -> {
          when {
            reminderState.reminderPermission && !reminderState.notificationPermission -> {
              Resource.Error(
                message = ErrorMessageCode.ERROR_NOTIFICATION_PERMISSION,
              )
            }

            !reminderState.reminderPermission && reminderState.notificationPermission -> {
              Resource.Error(
                message = ErrorMessageCode.ERROR_REMINDER_PERMISSION,
              )
            }

            else -> {
              Resource.Error(
                message = ErrorMessageCode.ERROR_NOTIFICATION_AND_REMINDER_PERMISSION,
              )
            }
          }
        }
      }
    } catch (e: Exception) {
      return Resource.Error(message = ErrorMessageCode.ERROR_SAVE_PROSES)
    }
  }
}
