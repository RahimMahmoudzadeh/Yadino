package com.yadino.routine.domain.useCase

import com.rahim.yadino.base.reminder.ReminderScheduler
import com.yadino.routine.domain.repo.RoutineRepository
import com.yadino.routine.domain.model.RoutineModel
import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(
  private val routineRepository: RoutineRepository,
  private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routine: RoutineModel) {
    reminderScheduler.cancelReminder(routine.idAlarm ?: 0)
    routineRepository.checkedRoutine(routine)
  }
}
