package com.rahim.yadino.reminder.useCase

import android.security.keystore.KeyNotYetValidException
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.reminder.ReminderScheduler
import com.rahim.yadino.reminder.state.ReminderState
import com.rahim.yadino.routine.RepositoryRoutine
import com.rahim.yadino.routine.modle.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(
    private val routineRepository: RepositoryRoutine,
    private val reminderScheduler: ReminderScheduler
) {
    suspend operator fun invoke(routine: Routine): Flow<Resource<Nothing?>> = flow {
        try {
            routineRepository.addRoutine(routine).catch {
                emit(Resource.Error(ErrorMessageCode.ERROR_SAVE_PROSES))
            }.collect {
                when (it) {
                    is Resource.Loading -> {
                        emit(Resource.Loading())
                    }

                    is Resource.Success -> {
                        it.data?.let { routine ->
                            val reminderState = reminderScheduler.setReminder(
                                routine.name,
                                routine.id ?: 0,
                                routine.timeInMillisecond ?: 0,
                                routine.idAlarm ?: 0
                            )
                            when (reminderState) {
                                ReminderState.SetSuccessfully -> {
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

                        }
                    }

                    is Resource.Error -> {
                        emit(Resource.Error(it.message))
                    }
                }

            }

        } catch (e: Exception) {
            emit(Resource.Error(message = ErrorMessageCode.ERROR_SAVE_PROSES))
        }
    }
}