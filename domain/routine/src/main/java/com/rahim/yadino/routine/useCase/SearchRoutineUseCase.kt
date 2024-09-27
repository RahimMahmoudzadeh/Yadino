package com.rahim.yadino.routine.useCase

import com.rahim.yadino.routine.RepositoryRoutine
import javax.inject.Inject

class SearchRoutineUseCase @Inject constructor(
  private val routineRepository: RepositoryRoutine,
) {
  suspend operator fun invoke(name: String, yearNumber: Int?, monthNumber: Int?, dayNumber: Int?) = routineRepository.searchRoutine(name, yearNumber, monthNumber, dayNumber)
}
