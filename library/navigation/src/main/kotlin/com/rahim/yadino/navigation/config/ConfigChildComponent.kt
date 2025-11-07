package com.rahim.yadino.navigation.config

import kotlinx.serialization.Serializable

@Serializable
sealed interface ConfigChildComponent {
  @Serializable
  data object Home : ConfigChildComponent
}
