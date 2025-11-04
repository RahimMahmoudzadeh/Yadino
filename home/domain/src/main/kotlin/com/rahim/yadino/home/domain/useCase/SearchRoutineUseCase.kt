package com.rahim.yadino.home.domain.useCase

import com.rahim.yadino.home.domain.model.Routine
import com.rahim.yadino.home.domain.repo.HomeRepository
import kotlinx.coroutines.flow.Flow

class SearchRoutineUseCase(
  private val routineRepository: HomeRepository,
) {
  operator fun invoke(name: String): Flow<List<Routine>> = routineRepository.searchTodayRoutine(name)
}
