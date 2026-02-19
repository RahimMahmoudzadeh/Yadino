package com.rahim.yadino.routine.domain.useCase

import com.rahim.yadino.routine.domain.repo.RoutineRepository
import kotlinx.coroutines.flow.Flow

class HaveAlarmUseCase(private val routineRepository: RoutineRepository) {
  operator fun invoke(): Flow<Boolean> {
    return routineRepository.haveAlarm()
  }
}
