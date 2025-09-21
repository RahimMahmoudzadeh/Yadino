package com.yadino.routine.domain.useCase

import com.yadino.routine.domain.repo.RoutineRepository
import com.yadino.routine.domain.model.RoutineModelDomainLayer
import javax.inject.Inject

class GetAllRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository,
) {
  suspend operator fun invoke(): List<RoutineModelDomainLayer> = routineRepository.getAllRoutine()
}
