package com.rahim.home.domain.useCase

import com.rahim.home.domain.model.CurrentDateModel
import com.rahim.home.domain.repo.HomeRepository
import javax.inject.Inject

class GetCurrentDateUseCase @Inject constructor(private val homeRepository: HomeRepository) {
  operator fun invoke(): CurrentDateModel = homeRepository.getCurrentDate()
}
