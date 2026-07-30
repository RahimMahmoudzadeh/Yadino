package com.rahim.yadino.navigation.component.base

import kotlinx.serialization.Serializable

sealed interface NavRoute {

  sealed interface Home : NavRoute {
    @Serializable
    data object Main : Home

    @Serializable
    data object HistoryRoutine : Home
  }

  sealed interface Routine : NavRoute {
    @Serializable
    data object Main : Routine
  }

  sealed interface Note : NavRoute {
    @Serializable
    data object Main : Note
  }
}
