package com.rahim.yadino.routine.useCase

import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.model.RoutineModel
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.routine.ReminderScheduler
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.routine.modle.ReminderState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateReminderUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
  private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineModel): Flow<Resource<Nothing?>> = flow {
    try {
      reminderScheduler.cancelReminder(routineModel.idAlarm ?: 0)
      routineModel.apply {
        idAlarm = routineRepository.getRoutineAlarmId()
        colorTask = 0
        timeInMillisecond = routineRepository.convertDateToMilSecond(
          yerNumber,
          monthNumber,
          dayNumber,
          timeHours,
        )
      }
      val equalRoutine = routineRepository.checkEqualRoutine(routineModel)
      if (equalRoutine != null) {
        emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
        return@flow
      }
      val reminderState = reminderScheduler.setReminder(
        routineModel.name,
        routineModel.id ?: 0,
        routineModel.timeInMillisecond ?: 0,
        routineModel.idAlarm ?: 0,
      )
      when (reminderState) {
        ReminderState.SetSuccessfully -> {
          routineRepository.addRoutine(routineModel)
          emit(Resource.Success(null))
        }

        is ReminderState.NotSet -> {
          emit(Resource.Error(reminderState.errorMessage))
        }

        is ReminderState.PermissionsState -> {
          when {
            reminderState.reminderPermission && !reminderState.notificationPermission -> {
              emit(
                Resource.Error(
                  message = ErrorMessageCode.ERROR_NOTIFICATION_PERMISSION,
                ),
              )
            }

            !reminderState.reminderPermission && reminderState.notificationPermission -> {
              emit(
                Resource.Error(
                  message = ErrorMessageCode.ERROR_REMINDER_PERMISSION,
                ),
              )
            }

            else -> {
              emit(
                Resource.Error(
                  message = ErrorMessageCode.ERROR_NOTIFICATION_AND_REMINDER_PERMISSION,
                ),
              )
            }
          }
        }
      }
    } catch (e: Exception) {
      emit(Resource.Error(message = ErrorMessageCode.ERROR_SAVE_PROSES))
    }
  }
}
