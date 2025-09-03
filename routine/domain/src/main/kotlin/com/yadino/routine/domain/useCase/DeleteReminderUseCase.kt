package com.yadino.routine.domain.useCase

import com.yadino.routine.domain.ReminderScheduler
import com.yadino.routine.domain.RoutineRepository
import com.yadino.routine.domain.model.RoutineModel
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
  private val routineRepository: RoutineRepository,
  private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineModel) {
    reminderScheduler.cancelReminder(routineModel.idAlarm ?: 0)
    routineRepository.removeRoutine(routineModel)
  }
}
