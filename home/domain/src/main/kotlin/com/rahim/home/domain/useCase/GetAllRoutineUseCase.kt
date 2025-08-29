package com.rahim.home.domain.useCase

import com.rahim.home.domain.HomeRepository
import com.rahim.home.domain.model.RoutineModel
import javax.inject.Inject

class GetAllRoutineUseCase @Inject constructor(
    private val routineRepository: HomeRepository,
) {
  suspend operator fun invoke(): List<RoutineModel> = routineRepository.getAllRoutine()
}
