package com.rahim.yadino.routine.domain.useCase

import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.routine.domain.model.CurrentTimeModel

class GetCurrentTimeUseCase(private val dateTimeRepository: DateTimeRepository) {
  operator fun invoke(): CurrentTimeModel {
    return CurrentTimeModel(currentNumberDay = dateTimeRepository.currentTimeDay, currentNumberMonth = dateTimeRepository.currentTimeMonth, currentNumberYear = dateTimeRepository.currentTimeYear)
  }
}
