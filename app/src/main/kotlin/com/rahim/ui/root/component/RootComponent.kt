package com.rahim.ui.root.component

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.value.Value
import com.rahim.data.distributionActions.StateOfClickItemDrawable
import com.rahim.yadino.base.StateEventComponent
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponent
import com.rahim.yadino.navigation.component.DrawerItemType
import com.rahim.yadino.note.presentation.ui.root.component.RootNoteComponent
import com.rahim.yadino.onboarding.presentation.ui.component.OnBoardingComponent
import com.rahim.yadino.routine.presentation.ui.alarmHistory.component.HistoryRoutineComponent
import com.rahim.yadino.routine.presentation.ui.root.component.RootRoutineComponent
import kotlinx.serialization.Serializable

interface RootComponent: StateEventComponent<RootComponent.Event, RootComponent.State> {
  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  fun onTabClick(tab: ChildConfig)

  fun showHistoryRoutine()
  fun navigateUp()

  sealed class ChildStack {
    class HomeStack(val component: RootHomeComponent) : ChildStack()
    class OnBoarding(val component: OnBoardingComponent) : ChildStack()
    class Routine(val component: RootRoutineComponent) : ChildStack()
    class Note(val component: RootNoteComponent) : ChildStack()
    class HistoryRoutine(val component: HistoryRoutineComponent) : ChildStack()
  }
  @Serializable
  sealed interface ChildConfig {
    @Serializable
    data object Home : ChildConfig

    @Serializable
    data object OnBoarding : ChildConfig

    @Serializable
    data object Routine : ChildConfig

    @Serializable
    data object HistoryRoutine : ChildConfig

    @Serializable
    data object Note : ChildConfig
  }

  @Immutable
  sealed interface Event {
    data class ClickDrawer(val drawerItemType: DrawerItemType) : Event
  }

  @Immutable
  data class State(
    val isLoading: Boolean = false,
    val isDarkTheme: Boolean? = false,
    val haveAlarm: Boolean = false,
    val isShowWelcomeScreen: Boolean = true,
    val stateOfClickItemDrawable: StateOfClickItemDrawable? = null,
  )
}
