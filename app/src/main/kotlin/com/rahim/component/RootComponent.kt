package com.rahim.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.component.config.AddRoutineDialogRoutineScreen
import com.rahim.component.config.ConfigChildComponent
import com.rahim.component.config.ErrorDialogRoutine
import com.rahim.component.config.UpdateRoutineDialogRoutineScreen
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponent
import com.rahim.yadino.note.presentation.ui.root.component.RootNoteComponent
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponent
import com.rahim.yadino.routine.presentation.ui.addRoutineDialog.component.AddRoutineDialogComponent
import com.rahim.yadino.routine.presentation.ui.alarmHistory.component.HistoryRoutineComponent
import com.rahim.yadino.routine.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.routine.presentation.ui.root.component.RootRoutineComponent
import com.rahim.yadino.routine.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent

interface RootComponent {
  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>


  val addRoutineDialogRoutineScreen: Value<ChildSlot<AddRoutineDialogRoutineScreen, AddRoutineDialogComponent>>
  val updateRoutineDialogRoutineScreen: Value<ChildSlot<UpdateRoutineDialogRoutineScreen, UpdateRoutineDialogComponent>>
  val errorDialogRoutineScreen: Value<ChildSlot<ErrorDialogRoutine, ErrorDialogComponent>>

  fun onTabClick(tab: ConfigChildComponent)
  fun onShowAddDialogRoutineRoutineScreen(dialog: AddRoutineDialogRoutineScreen)

  fun showHistoryRoutine()
  fun navigateUp()

  sealed class ChildStack {
    class HomeStack(val component: RootHomeComponent) : ChildStack()
    class OnBoarding(val component: OnBoardingComponent) : ChildStack()
    class Routine(val component: RootRoutineComponent) : ChildStack()
    class Note(val component: RootNoteComponent) : ChildStack()
    class HistoryRoutine(val component: HistoryRoutineComponent) : ChildStack()
  }
}
