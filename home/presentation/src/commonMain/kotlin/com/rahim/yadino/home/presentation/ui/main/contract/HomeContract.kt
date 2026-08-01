package com.rahim.yadino.home.presentation.ui.main.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.rahim.yadino.base.UnidirectionalViewModel

interface HomeContract: UnidirectionalViewModel<HomeContract.Event, HomeContract.State, HomeContract.Effect> {

  @Immutable
  sealed interface Effect{}
  @Immutable
  sealed interface Event {}

  @Stable
  data class State(val data: String = "")
}
