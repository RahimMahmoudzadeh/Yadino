package com.rahim.yadino.home.domain.useCase

import com.rahim.yadino.home.domain.model.Routine
import com.rahim.yadino.home.domain.repo.HomeRepository
import com.rahim.yadino.base.reminder.ReminderScheduler

class CancelReminderUseCase(
    private val routineRepository: HomeRepository,
    private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: Routine) {
    reminderScheduler.cancelReminder(routineModel.idAlarm ?: 0)
    routineRepository.checkedRoutine(routineModel)
  }
}
