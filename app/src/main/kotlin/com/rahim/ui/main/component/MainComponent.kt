package com.rahim.ui.main.component

import androidx.compose.runtime.Immutable
import com.rahim.data.distributionActions.StateOfClickItemDrawable
import com.rahim.yadino.base.StateEventComponent
import com.rahim.yadino.navigation.component.DrawerItemType

interface MainComponent : StateEventComponent<MainComponent.MainEvent, MainComponent.MainState> {

  @Immutable
  sealed interface MainEvent {
    data object CheckedAllRoutinePastTime : MainEvent
    data class ClickDrawer(val drawerItemType: DrawerItemType) : MainEvent
  }

  @Immutable
  data class MainState(
    val isLoading: Boolean = false,
    val isDarkTheme: Boolean? = false,
    val haveAlarm: Boolean = false,
    val isShowWelcomeScreen: Boolean = true,
    val stateOfClickItemDrawable: StateOfClickItemDrawable? = null,
  )
}
