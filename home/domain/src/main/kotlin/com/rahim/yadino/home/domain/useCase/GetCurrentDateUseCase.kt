package com.rahim.yadino.home.domain.useCase

import com.rahim.yadino.home.domain.model.CurrentDate
import com.rahim.yadino.home.domain.repo.HomeRepository

class GetCurrentDateUseCase(private val homeRepository: HomeRepository) {
  operator fun invoke(): CurrentDate = homeRepository.getCurrentDate()
}
