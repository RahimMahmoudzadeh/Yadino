package com.rahim.yadino.routine.useCase


import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.routine.ReminderScheduler
import com.rahim.yadino.routine.RepositoryRoutine
import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
  private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineModel) {
    reminderScheduler.cancelReminder(routineModel.idAlarm ?: 0)
    routineRepository.checkedRoutine(routineModel)
  }
}
