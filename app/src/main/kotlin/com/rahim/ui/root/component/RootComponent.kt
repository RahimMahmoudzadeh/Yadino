package com.rahim.ui.root.component

import com.arkivanov.decompose.value.Value
import com.rahim.ui.main.component.MainComponent
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponent
import com.rahim.yadino.note.presentation.ui.root.component.RootNoteComponent
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponent
import com.rahim.yadino.routine.presentation.ui.alarmHistory.component.HistoryRoutineComponent
import com.rahim.yadino.routine.presentation.ui.root.component.RootRoutineComponent
import kotlinx.serialization.Serializable

interface RootComponent {
  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  fun onTabClick(tab: ChildConfig)

  fun showHistoryRoutine()
  fun navigateUp()

  sealed class ChildStack {
    class HomeStack(val component: RootHomeComponent) : ChildStack()
    class OnBoarding(val component: OnBoardingComponent) : ChildStack()
    class Routine(val component: RootRoutineComponent) : ChildStack()
    class Main(val component: MainComponent) : ChildStack()
    class Note(val component: RootNoteComponent) : ChildStack()
    class HistoryRoutine(val component: HistoryRoutineComponent) : ChildStack()
  }
  @Serializable
  sealed interface ChildConfig {
    @Serializable
    data object Home : ChildConfig

    @Serializable
    data object Main : ChildConfig

    @Serializable
    data object OnBoarding : ChildConfig

    @Serializable
    data object Routine : ChildConfig

    @Serializable
    data object HistoryRoutine : ChildConfig

    @Serializable
    data object Note : ChildConfig
  }
}
