package com.rahim.yadino.routine.domain.useCase

import com.rahim.yadino.routine.domain.model.Routine
import com.rahim.yadino.routine.domain.repo.RoutineRepository

class GetAllRoutineUseCase(
    private val routineRepository: RoutineRepository,
) {
  suspend operator fun invoke(): List<Routine> = routineRepository.getAllRoutine()
}
