package com.yadino.routine.domain.useCase

import com.yadino.routine.domain.repo.RoutineRepository
import com.yadino.routine.domain.model.RoutineModel
import javax.inject.Inject

class GetAllRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
) {
  suspend operator fun invoke(): List<RoutineModel> = routineRepository.getAllRoutine()
}
