package com.rahim.yadino.welcome

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel

interface WelcomeContract : UnidirectionalViewModel<WelcomeContract.WelcomeEvent, WelcomeContract.WelcomeState> {

  @Immutable
  sealed class WelcomeEvent {
    data class SaveShowWelcome(val isShow: Boolean) : WelcomeEvent()
  }

  @Immutable
  data class WelcomeState(val isShowedWelcome: Boolean = false)
}
