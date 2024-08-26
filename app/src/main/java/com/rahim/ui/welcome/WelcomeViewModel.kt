package com.rahim.ui.welcome

import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class WelcomeViewModel @Inject constructor(
  val sharedPreferencesRepository: SharedPreferencesRepository,
  baseRepository: BaseRepository,
) : BaseViewModel(sharedPreferencesRepository, baseRepository) {
  fun saveShowWelcome(isShow: Boolean) {
    sharedPreferencesRepository.saveShowWelcome(isShow)
  }
}
