package com.rahim.yadino.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) : ViewModel(), OnBoardingContract {

  private val mutableState = MutableStateFlow<OnBoardingContract.WelcomeState>(OnBoardingContract.WelcomeState())
  override val state: StateFlow<OnBoardingContract.WelcomeState> = mutableState.onStart {
    isShowWelcomeScreen()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OnBoardingContract.WelcomeState())

  override fun event(event: OnBoardingContract.WelcomeEvent) = when (event) {
    is OnBoardingContract.WelcomeEvent.SaveShowWelcome -> {
      saveShowWelcome(event.isShow)
    }
  }

  private fun saveShowWelcome(isShow: Boolean) {
    viewModelScope.launch {
      sharedPreferencesRepository.saveShowWelcome(isShow)
    }
  }

  private fun isShowWelcomeScreen() = sharedPreferencesRepository.isShowWelcomeScreen()
}
