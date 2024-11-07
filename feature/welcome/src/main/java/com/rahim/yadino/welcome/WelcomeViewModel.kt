package com.rahim.yadino.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.BaseViewModel
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
  private val sharedPreferencesRepository: SharedPreferencesRepository,
) : ViewModel(), WelcomeContract {

  private val mutableState = MutableStateFlow<WelcomeContract.WelcomeState>(WelcomeContract.WelcomeState())
  override val state: StateFlow<WelcomeContract.WelcomeState> = mutableState.onStart {
    isShowWelcomeScreen()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WelcomeContract.WelcomeState())

  override fun event(event: WelcomeContract.WelcomeEvent) = when (event) {
    is WelcomeContract.WelcomeEvent.SaveShowWelcome -> {
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
