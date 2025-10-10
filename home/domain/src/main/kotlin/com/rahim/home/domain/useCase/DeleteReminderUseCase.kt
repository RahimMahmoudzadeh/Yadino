package com.rahim.home.domain.useCase

import com.rahim.home.domain.model.RoutineHomeModel
import com.rahim.home.domain.repo.HomeRepository
import com.rahim.yadino.base.reminder.ReminderScheduler
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
    private val routineRepository: HomeRepository,
    private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineHomeModel) {
    reminderScheduler.cancelReminder(routineModel.idAlarm ?: 0)
    routineRepository.removeRoutine(routineModel)
  }
}
