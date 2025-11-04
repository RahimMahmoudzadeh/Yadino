package com.rahim.component

import kotlinx.serialization.Serializable

@Serializable
sealed interface ConfigChildComponent {
  @Serializable
  data object Home : ConfigChildComponent
}
