package com.rahim.home.domain.useCase

import com.rahim.home.domain.model.RoutineHomeModel
import com.rahim.home.domain.repo.HomeRepository
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.reminder.ReminderScheduler
import com.rahim.yadino.base.reminder.ReminderState
import com.rahim.yadino.enums.SuccessMessage
import com.rahim.yadino.enums.error.ErrorMessageCode
import timber.log.Timber
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(
    private val routineRepository: HomeRepository,
    private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineHomeModel): Resource<SuccessMessage, ErrorMessageCode> {
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
          Resource.Error(error = reminderState.errorMessage?: ErrorMessageCode.ERROR_SAVE_PROSES)
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
      Timber.tag("addRoutine").d("error->${e.message}")
      return Resource.Error(error = ErrorMessageCode.ERROR_SAVE_PROSES)
    }
  }
}
