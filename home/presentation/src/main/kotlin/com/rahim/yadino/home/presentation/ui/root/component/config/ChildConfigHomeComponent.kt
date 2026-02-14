package com.rahim.yadino.home.presentation.ui.root.component.config

import kotlinx.serialization.Serializable

@Serializable
sealed interface ChildConfigHomeComponent {
  @Serializable
  data object HomeMain : ChildConfigHomeComponent
}
