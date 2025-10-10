package com.rahim.home.domain.useCase

import com.rahim.home.domain.model.RoutineHomeModel
import com.rahim.home.domain.repo.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRoutineUseCase @Inject constructor(
  private val routineRepository: HomeRepository,
) {
  operator fun invoke(name: String): Flow<List<RoutineHomeModel>> = routineRepository.searchTodayRoutine(name)
}
