package com.yadino.routine.domain.useCase

import com.yadino.routine.domain.model.Routine
import com.yadino.routine.domain.repo.RoutineRepository

class GetAllRoutineUseCase(
    private val routineRepository: RoutineRepository,
) {
  suspend operator fun invoke(): List<Routine> = routineRepository.getAllRoutine()
}
