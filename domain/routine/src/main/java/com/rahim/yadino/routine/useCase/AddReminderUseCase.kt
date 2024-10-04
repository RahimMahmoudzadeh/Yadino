package com.rahim.yadino.routine.useCase

import com.rahim.yadino.Resource
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.routine.ReminderScheduler
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.routine.modle.ReminderState
import com.rahim.yadino.model.RoutineModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
  private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineModel): Flow<Resource<Nothing?>> = flow {
    try {
      val routine=routineModel.copy(
        idAlarm = routineRepository.getRoutineAlarmId(),
        colorTask = 0,
        timeInMillisecond = routineRepository.convertDateToMilSecond(
          routineModel.yerNumber,
          routineModel.monthNumber,
          routineModel.dayNumber,
          routineModel.timeHours,
        ),
      )
      val equalRoutine = routineRepository.checkEqualRoutine(routine)
      if (equalRoutine != null) {
        emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
        return@flow
      }
      val reminderState = reminderScheduler.setReminder(
        routine.name,
        routine.id ?: 0,
        routine.timeInMillisecond ?: 0,
        routine.idAlarm ?: 0,
      )
      when (reminderState) {
        ReminderState.SetSuccessfully -> {
          routineRepository.addRoutine(routine)
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
