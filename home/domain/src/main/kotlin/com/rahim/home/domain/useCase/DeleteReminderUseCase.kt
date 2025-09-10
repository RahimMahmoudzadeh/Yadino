package com.rahim.home.domain.useCase

import com.rahim.yadino.base.reminder.ReminderScheduler
import com.rahim.home.domain.repo.HomeRepository
import com.rahim.home.domain.model.RoutineModel
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
    private val routineRepository: HomeRepository,
    private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineModel) {
    reminderScheduler.cancelReminder(routineModel.idAlarm ?: 0)
    routineRepository.removeRoutine(routineModel)
  }
}
