package com.rahim.home.domain.useCase

import com.rahim.home.domain.RepositoryRoutine
import com.rahim.yadino.routine.model.RoutineModel
import javax.inject.Inject

class GetAllRoutineUseCase @Inject constructor(
    private val routineRepository: RepositoryRoutine,
) {
  suspend operator fun invoke(): List<RoutineModel> = routineRepository.getAllRoutine()
}
