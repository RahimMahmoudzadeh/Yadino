package com.rahim.home.domain.useCase

import com.rahim.home.domain.model.RoutineHomeDomainLayer
import com.rahim.home.domain.repo.HomeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SearchRoutineUseCase @Inject constructor(
  private val routineRepository: HomeRepository,
) {
  operator fun invoke(name: String): Flow<List<RoutineHomeDomainLayer>> = routineRepository.searchTodayRoutine(name)
}
