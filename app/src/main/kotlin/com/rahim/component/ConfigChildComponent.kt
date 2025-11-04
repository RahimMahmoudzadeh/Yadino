package com.rahim.component

import kotlinx.serialization.Serializable

@Serializable
interface ConfigChildComponent {
  @Serializable
  data object Home : ConfigChildComponent
}
