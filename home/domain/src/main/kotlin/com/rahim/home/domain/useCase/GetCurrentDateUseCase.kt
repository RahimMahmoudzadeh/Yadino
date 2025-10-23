package com.rahim.home.domain.useCase

import com.rahim.home.domain.model.CurrentDate
import com.rahim.home.domain.repo.HomeRepository

class GetCurrentDateUseCase(private val homeRepository: HomeRepository) {
  operator fun invoke(): CurrentDate = homeRepository.getCurrentDate()
}
