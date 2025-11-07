package com.rahim.yadino.routine.domain.useCase

import com.rahim.yadino.routine.domain.repo.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SearchRoutineUseCase(
    private val routineRepository: RoutineRepository,
) {
  operator fun invoke(name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?, coroutineScope: CoroutineScope) = routineRepository.searchRoutine(name, yearNumber, monthNumber, dayNumber)
    .stateIn(coroutineScope, started = SharingStarted.Eagerly, emptyList())
}
