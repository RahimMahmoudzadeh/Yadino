package com.rahim.yadino.home.presentation.ui.root.component

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.home.presentation.ui.main.component.MainHomeComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent

interface RootHomeComponent {

  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  val addRoutineDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.AddRoutineDialog, AddRoutineDialogComponent>>
  val updateRoutineDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.UpdateRoutineDialog, UpdateRoutineDialogComponent>>
  val errorDialogRemoveRoutineScreen: Value<ChildSlot<DialogSlotHomeComponent.ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>>
  val errorDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.ErrorDialog, ErrorDialogComponent>>


  sealed interface ChildStack{
    class HomeMainStack(val component: MainHomeComponent) : ChildStack
  }
}
