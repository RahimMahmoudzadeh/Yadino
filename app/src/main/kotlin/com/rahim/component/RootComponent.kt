package com.rahim.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.component.config.AddNoteDialog
import com.rahim.yadino.home.presentation.component.HomeComponent
import com.rahim.yadino.home.presentation.component.addRoutineDialog.AddRoutineDialogComponent
import com.rahim.component.config.AddRoutineDialogHomeScreen
import com.rahim.component.config.AddRoutineDialogRoutineScreen
import com.rahim.component.config.ConfigChildComponent
import com.rahim.component.config.ErrorDialog
import com.rahim.yadino.home.presentation.component.errorDialog.ErrorDialogComponent
import com.rahim.yadino.note.presentation.component.NoteComponent
import com.rahim.yadino.note.presentation.component.addNoteDialog.AddNoteDialogComponent
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponent
import com.yadino.routine.presentation.component.RoutineComponent
import com.yadino.routine.presentation.component.history.HistoryRoutineComponent

interface RootComponent {
  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  val addRoutineDialogHomeScreen: Value<ChildSlot<*, AddRoutineDialogComponent>>
  val addRoutineDialogRoutineScreen: Value<ChildSlot<AddRoutineDialogRoutineScreen, com.yadino.routine.presentation.component.addRoutineDialog.AddRoutineDialogComponent>>
  val addNoteDialog: Value<ChildSlot<AddNoteDialog, AddNoteDialogComponent>>
  val errorDialog: Value<ChildSlot<ErrorDialog, ErrorDialogComponent>>

  fun onTabClick(tab: ConfigChildComponent)
  fun onShowAddDialogRoutineHomeScreen(dialog: AddRoutineDialogHomeScreen)
  fun onShowAddDialogRoutineRoutineScreen(dialog: AddRoutineDialogRoutineScreen)
  fun onShowAddNoteDialog(dialog: AddNoteDialog)


  fun showHistoryRoutine()
  fun navigateUp()

  sealed class ChildStack {
    class HomeStack(val component: HomeComponent) : ChildStack()
    class OnBoarding(val component: OnBoardingComponent) : ChildStack()
    class Routine(val component: RoutineComponent) : ChildStack()
    class Note(val component: NoteComponent) : ChildStack()
    class HistoryRoutine(val component: HistoryRoutineComponent) : ChildStack()
  }
}
