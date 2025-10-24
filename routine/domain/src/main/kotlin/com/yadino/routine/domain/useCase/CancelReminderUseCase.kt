package com.yadino.routine.domain.useCase

import com.rahim.yadino.base.reminder.ReminderScheduler
import com.yadino.routine.domain.model.Routine
import com.yadino.routine.domain.repo.RoutineRepository

class CancelReminderUseCase(
  private val routineRepository: RoutineRepository,
  private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routine: Routine) {
    reminderScheduler.cancelReminder(routine.idAlarm ?: 0)
    routineRepository.checkedRoutine(routine)
  }
}
