package com.yadino.routine.domain.useCase

import com.rahim.home.domain.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
    private val routineRepository: HomeRepository,
) {
  operator fun invoke(monthNumber: Int, numberDay: Int, yearNumber: Int, coroutineScope: CoroutineScope) = routineRepository.getRoutines(monthNumber, numberDay, yearNumber).stateIn(coroutineScope, started = SharingStarted.Eagerly, emptyList())
}
