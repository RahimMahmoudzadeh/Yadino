package com.rahim.home.domain.useCase

import com.rahim.home.domain.model.CurrentDateDomainLayer
import com.rahim.home.domain.repo.HomeRepository
import javax.inject.Inject

class GetCurrentDateUseCase @Inject constructor(private val homeRepository: HomeRepository) {
  operator fun invoke(): CurrentDateDomainLayer = homeRepository.getCurrentDate()
}
