package com.rahim.home.domain.useCase

import com.rahim.home.domain.repo.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SearchRoutineUseCase @Inject constructor(
    private val routineRepository: HomeRepository,
) {
  operator fun invoke(name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?, coroutineScope: CoroutineScope) = routineRepository.searchRoutine(name, yearNumber, monthNumber, dayNumber)
    .stateIn(coroutineScope, started = SharingStarted.Eagerly, emptyList())
}
