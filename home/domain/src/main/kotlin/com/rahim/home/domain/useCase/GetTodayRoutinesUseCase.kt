package com.rahim.home.domain.useCase

import com.rahim.home.domain.model.Routine
import com.rahim.home.domain.repo.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayRoutinesUseCase @Inject constructor(
  private val routineRepository: HomeRepository,
) {
  operator fun invoke(): Flow<List<Routine>> = routineRepository.getTodayRoutines()
}
