package com.rahim.yadino.routine.useCase

import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.routine.ReminderScheduler
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.routine.modle.ReminderState
import com.rahim.yadino.base.db.model.RoutineModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(
    private val routineRepository: RepositoryRoutine,
    private val reminderScheduler: ReminderScheduler
) {
    suspend operator fun invoke(routineModel: RoutineModel): Flow<Resource<Nothing?>> = flow {
        try {
            routineModel.apply {
                idAlarm = routineRepository.getRoutineAlarmId()
                colorTask = 0
                timeInMillisecond = routineRepository.convertDateToMilSecond(
                    yerNumber,
                    monthNumber,
                    dayNumber,
                    timeHours
                )
            }
            val equalRoutine = routineRepository.checkEqualRoutine(routineModel)
            if (equalRoutine == null) {
                val reminderState = reminderScheduler.setReminder(
                    routineModel.name,
                    routineModel.id ?: 0,
                    routineModel.timeInMillisecond ?: 0,
                    routineModel.idAlarm ?: 0
                )
                when (reminderState) {
                    ReminderState.SetSuccessfully -> {
                        routineRepository.addRoutine(routineModel).catch {
                            emit(Resource.Error(ErrorMessageCode.ERROR_SAVE_PROSES))
                        }.collect {
                            when (it) {
                                is Resource.Loading -> {
                                    emit(Resource.Loading())
                                }

                                is Resource.Success -> {
                                    emit(Resource.Success(null))
                                }

                                is Resource.Error -> {
                                    emit(Resource.Error(it.message))
                                }
                            }

                        }
                    }

                    is ReminderState.NotSet -> {
                        emit(Resource.Error(reminderState.errorMessage))
                    }

                    is ReminderState.PermissionsState -> {
                        when {
                            reminderState.reminderPermission && !reminderState.notificationPermission -> {
                                emit(
                                    Resource.Error(
                                        message = ErrorMessageCode.ERROR_NOTIFICATION_PERMISSION
                                    )
                                )
                            }

                            !reminderState.reminderPermission && reminderState.notificationPermission -> {
                                emit(
                                    Resource.Error(
                                        message = ErrorMessageCode.ERROR_REMINDER_PERMISSION
                                    )
                                )
                            }

                            !reminderState.reminderPermission && !reminderState.notificationPermission -> {
                                emit(
                                    Resource.Error(
                                        message = ErrorMessageCode.ERROR_NOTIFICATION_AND_REMINDER_PERMISSION
                                    )
                                )
                            }

                            else -> {
                                emit(
                                    Resource.Error(
                                        message = ErrorMessageCode.ERROR_NOTIFICATION_AND_REMINDER_PERMISSION
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                emit(Resource.Error(ErrorMessageCode.EQUAL_ROUTINE_MESSAGE))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = ErrorMessageCode.ERROR_SAVE_PROSES))
        }
    }
}