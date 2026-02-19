package com.rahim.yadino.routine.domain.useCase

import com.rahim.yadino.routine.domain.repo.RoutineRepository

class AddSampleRoutineUseCase(private val routineRepository: RoutineRepository) {
  suspend operator fun invoke() {
    routineRepository.addSampleRoutine()
  }
}
