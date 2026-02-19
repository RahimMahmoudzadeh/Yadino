package com.rahim.yadino.core.timeDate.useCase

import com.rahim.yadino.core.timeDate.repo.DateTimeRepository

class CalculateTodayUseCase(private val dateTimeRepository: DateTimeRepository) {
  suspend operator fun invoke() {
    dateTimeRepository.calculateToday()
  }
}
