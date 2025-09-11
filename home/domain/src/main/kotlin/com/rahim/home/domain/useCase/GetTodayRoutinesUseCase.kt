package com.rahim.home.domain.useCase

import com.rahim.home.domain.model.RoutineHomeDomainLayer
import com.rahim.home.domain.repo.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayRoutinesUseCase @Inject constructor(
  private val routineRepository: HomeRepository,
) {
  operator fun invoke(): Flow<List<RoutineHomeDomainLayer>> = routineRepository.getTodayRoutines()
}
