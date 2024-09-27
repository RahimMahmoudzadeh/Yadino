package com.rahim.yadino.routine.useCase

import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.model.RoutineModel
import com.rahim.yadino.routine.RepositoryRoutine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
) {
  suspend operator fun invoke(monthNumber: Int, numberDay: Int, yerNumber: Int): List<RoutineModel> = routineRepository.getRoutines(monthNumber, numberDay, yerNumber)
}
