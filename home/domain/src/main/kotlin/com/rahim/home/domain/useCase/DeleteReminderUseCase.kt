package com.rahim.home.domain.useCase

import com.rahim.home.domain.ReminderScheduler
import com.rahim.home.domain.RepositoryRoutine
import com.rahim.yadino.routine.model.RoutineModel
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
    private val routineRepository: RepositoryRoutine,
    private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineModel) {
    reminderScheduler.cancelReminder(routineModel.idAlarm ?: 0)
    routineRepository.removeRoutine(routineModel)
  }
}
