package com.rahim.yadino.routine.useCase

import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.routine.RepositoryRoutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetRemindersUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
) {
  operator fun invoke(monthNumber: Int, numberDay: Int, yearNumber: Int, coroutineScope: CoroutineScope) =
    routineRepository.getRoutines(monthNumber, numberDay, yearNumber).stateIn(coroutineScope, started = SharingStarted.Eagerly, emptyList())
}
