package com.rahim.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.component.config.AddNoteDialog
import com.rahim.yadino.home.presentation.component.HomeComponent
import com.rahim.yadino.home.presentation.component.addRoutineDialog.AddRoutineDialogComponent
import com.rahim.component.config.AddRoutineDialogHomeScreen
import com.rahim.component.config.AddRoutineDialogRoutineScreen
import com.rahim.component.config.ConfigChildComponent
import com.rahim.component.config.ErrorDialogHome
import com.rahim.component.config.ErrorDialogNote
import com.rahim.component.config.ErrorDialogRoutine
import com.rahim.component.config.UpdateRoutineDialogHomeScreen
import com.rahim.component.config.UpdateRoutineDialogRoutineScreen
import com.rahim.yadino.home.presentation.component.errorDialog.ErrorDialogComponent
import com.rahim.yadino.home.presentation.component.updateRoutineDialog.UpdateRoutineDialogComponent
import com.rahim.yadino.note.presentation.component.NoteComponent
import com.rahim.yadino.note.presentation.component.addNoteDialog.AddNoteDialogComponent
import com.rahim.yadino.onboarding.presentation.component.OnBoardingComponent

interface RootComponent {
  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  val addRoutineDialogHomeScreen: Value<ChildSlot<AddRoutineDialogHomeScreen, AddRoutineDialogComponent>>
  val updateRoutineDialogHomeScreen: Value<ChildSlot<UpdateRoutineDialogHomeScreen, UpdateRoutineDialogComponent>>
  val addRoutineDialogRoutineScreen: Value<ChildSlot<AddRoutineDialogRoutineScreen, com.rahim.yadino.routine.presentation.component.addRoutineDialog.AddRoutineDialogComponent>>
  val updateRoutineDialogRoutineScreen: Value<ChildSlot<UpdateRoutineDialogRoutineScreen, com.rahim.yadino.routine.presentation.component.updateRoutineDialog.UpdateRoutineDialogComponent>>
  val addNoteDialog: Value<ChildSlot<AddNoteDialog, AddNoteDialogComponent>>
  val errorDialogHomeScreen: Value<ChildSlot<ErrorDialogHome, ErrorDialogComponent>>
  val errorDialogRoutineScreen: Value<ChildSlot<ErrorDialogRoutine, com.rahim.yadino.routine.presentation.component.errorDialog.ErrorDialogComponent>>
  val errorDialogNoteScreen: Value<ChildSlot<ErrorDialogNote, com.rahim.yadino.note.presentation.component.errorDialog.ErrorDialogComponent>>

  fun onTabClick(tab: ConfigChildComponent)
  fun onShowAddDialogRoutineHomeScreen(dialog: AddRoutineDialogHomeScreen)
  fun onShowAddDialogRoutineRoutineScreen(dialog: AddRoutineDialogRoutineScreen)
  fun onShowAddNoteDialog(dialog: AddNoteDialog)


  fun showHistoryRoutine()
  fun navigateUp()

  sealed class ChildStack {
    class HomeStack(val component: HomeComponent) : ChildStack()
    class OnBoarding(val component: OnBoardingComponent) : ChildStack()
    class Routine(val component: com.rahim.yadino.routine.presentation.component.RoutineComponent) : ChildStack()
    class Note(val component: NoteComponent) : ChildStack()
    class HistoryRoutine(val component: com.rahim.yadino.routine.presentation.component.history.HistoryRoutineComponent) : ChildStack()
  }
}
