package com.rahim.yadino.routine.useCase

import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.routine.RepositoryRoutine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
) {
  operator fun invoke(monthNumber: Int, numberDay: Int, yearNumber: Int) = routineRepository.getRoutines(monthNumber, numberDay, yearNumber)
}
