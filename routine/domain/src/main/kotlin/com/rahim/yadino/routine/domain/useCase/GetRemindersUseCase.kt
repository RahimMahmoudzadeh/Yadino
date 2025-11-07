package com.rahim.yadino.routine.domain.useCase

import com.rahim.yadino.routine.domain.repo.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class GetRemindersUseCase(
    private val routineRepository: RoutineRepository,
) {
  operator fun invoke(monthNumber: Int, numberDay: Int, yearNumber: Int, coroutineScope: CoroutineScope) = routineRepository.getRoutines(monthNumber, numberDay, yearNumber).stateIn(coroutineScope, started = SharingStarted.Eagerly, emptyList())
}
