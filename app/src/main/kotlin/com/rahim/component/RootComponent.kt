package com.rahim.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.home.presentation.component.HomeComponent
import com.rahim.yadino.home.presentation.component.addRoutineDialog.AddRoutineDialogComponent
import com.rahim.yadino.navigation.config.AddRoutineDialogHomeScreen
import com.rahim.yadino.navigation.config.AddRoutineDialogRoutineScreen
import com.rahim.yadino.navigation.config.ConfigChildComponent
import com.rahim.yadino.note.presentation.component.NoteComponent
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponent
import com.yadino.routine.presentation.component.RoutineComponent
import com.yadino.routine.presentation.component.history.HistoryRoutineComponent

interface RootComponent {
  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  val addRoutineDialogHomeScreen: Value<ChildSlot<*, AddRoutineDialogComponent>>
  val addRoutineDialogRoutineScreen: Value<ChildSlot<AddRoutineDialogRoutineScreen, com.yadino.routine.presentation.component.addRoutineDialog.AddRoutineDialogComponent>>

  fun onTabClick(tab: ConfigChildComponent)

  fun onShowAddDialogRoutineHomeScreen(dialog: AddRoutineDialogHomeScreen)

  fun onShowAddDialogRoutineRoutineScreen(dialog: AddRoutineDialogRoutineScreen)
  sealed class ChildStack {
    class HomeStack(val component: HomeComponent) : ChildStack()
    class OnBoarding(val component: OnBoardingComponent) : ChildStack()
    class Routine(val component: RoutineComponent) : ChildStack()
    class Note(val component: NoteComponent) : ChildStack()
    class HistoryRoutine(val component: HistoryRoutineComponent) : ChildStack()
  }
}
