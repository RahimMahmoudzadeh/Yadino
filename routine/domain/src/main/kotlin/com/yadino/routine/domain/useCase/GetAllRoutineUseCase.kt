package com.yadino.routine.domain.useCase

import com.rahim.home.domain.HomeRepository
import com.rahim.yadino.routine.model.RoutineModel
import javax.inject.Inject

class GetAllRoutineUseCase @Inject constructor(
    private val routineRepository: HomeRepository,
) {
  suspend operator fun invoke(): List<RoutineModel> = routineRepository.getAllRoutine()
}
