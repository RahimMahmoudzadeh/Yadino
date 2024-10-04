package com.rahim.yadino.routine.useCase

import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.routine.RepositoryRoutine
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
) {
  suspend operator fun invoke(monthNumber: Int, numberDay: Int, yerNumber: Int): List<RoutineModel> = routineRepository.getRoutines(monthNumber, numberDay, yerNumber)
}
