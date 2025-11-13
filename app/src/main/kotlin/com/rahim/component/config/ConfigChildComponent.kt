package com.rahim.component.config

import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.serialization.Serializable

@Serializable
sealed interface ConfigChildComponent {
  @Serializable
  data object Home : ConfigChildComponent

  @Serializable
  data object OnBoarding : ConfigChildComponent

  @Serializable
  data object Routine : ConfigChildComponent

  @Serializable
  data object HistoryRoutine : ConfigChildComponent

  @Serializable
  data object Note : ConfigChildComponent
}
