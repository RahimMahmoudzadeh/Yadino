package com.rahim.yadino.routine.useCase

import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.routine.RepositoryRoutine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
) {
  suspend operator fun invoke(monthNumber: Int, numberDay: Int, yerNumber: Int): Flow<List<RoutineModel>> = routineRepository.getRoutines(monthNumber, numberDay, yerNumber)
}
