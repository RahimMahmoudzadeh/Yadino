package com.rahim.yadino.routine.useCase

import com.rahim.yadino.routine.RepositoryRoutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SearchRoutineUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
) {
  operator fun invoke(name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?, coroutineScope: CoroutineScope) = routineRepository.searchRoutine(name, yearNumber, monthNumber, dayNumber)
    .stateIn(coroutineScope, started = SharingStarted.Eagerly, emptyList())
}
