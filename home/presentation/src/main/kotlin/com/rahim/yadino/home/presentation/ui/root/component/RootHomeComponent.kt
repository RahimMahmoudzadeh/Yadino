package com.rahim.yadino.home.presentation.ui.root.component

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.home.presentation.model.CurrentDateUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogRemoveUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import kotlinx.collections.immutable.PersistentList

interface RootHomeComponent : UnidirectionalComponent<RootHomeComponent.Event, Unit, RootHomeComponent.Effect> {

  val addRoutineDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.AddRoutineDialog, AddRoutineDialogComponent>>
  val updateRoutineDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.UpdateRoutineDialog, UpdateRoutineDialogComponent>>
  val errorDialogRemoveRoutineScreen: Value<ChildSlot<DialogSlotHomeComponent.ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>>
  val errorDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.ErrorDialog, ErrorDialogComponent>>

  @Immutable
  sealed class Event {
    data class ShowErrorDialogRemoveRoutine(val errorDialogRemoveUiModel: ErrorDialogRemoveUiModel) : Event()
    data class ShowErrorDialog(val errorDialogUiModel: ErrorDialogUiModel) : Event()
    data class ShowUpdateRoutineDialog(val routine: RoutineUiModel) : Event()
    data object ShowAddRoutineDialog : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val message: MessageUi) : Effect()
    data class ShowSnackBar(val message: MessageUi) : Effect()
  }
}
