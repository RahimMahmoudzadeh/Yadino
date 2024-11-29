package com.rahim.yadino.routine.useCase

import com.rahim.yadino.routine.model.RoutineModel
import com.rahim.yadino.routine.RepositoryRoutine
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRoutineUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
) {
  suspend operator fun invoke(): List<RoutineModel> = routineRepository.getAllRoutine()
}
