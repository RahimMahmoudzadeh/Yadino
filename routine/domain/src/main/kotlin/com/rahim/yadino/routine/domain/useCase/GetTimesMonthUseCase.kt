package com.rahim.yadino.routine.domain.useCase

import com.rahim.yadino.core.timeDate.model.TimeDateModel
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository

class GetTimesMonthUseCase(private val dateTimeRepository: DateTimeRepository) {
  suspend operator fun invoke(yearNumber: Int, monthNumber: Int): List<TimeDateModel> {
    return dateTimeRepository.getTimesMonth(yearNumber = yearNumber, monthNumber = monthNumber)
  }
}
