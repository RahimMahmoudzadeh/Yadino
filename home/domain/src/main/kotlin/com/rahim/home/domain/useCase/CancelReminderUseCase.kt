package com.rahim.home.domain.useCase

import com.rahim.home.domain.ReminderScheduler
import com.rahim.home.domain.HomeRepository
import com.rahim.home.domain.model.RoutineModel
import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(
    private val routineRepository: HomeRepository,
    private val reminderScheduler: ReminderScheduler,
) {
  suspend operator fun invoke(routineModel: RoutineModel) {
    reminderScheduler.cancelReminder(routineModel.idAlarm ?: 0)
    routineRepository.checkedRoutine(routineModel)
  }
}
