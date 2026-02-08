package com.rahim.yadino.home.presentation.ui.root.component

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.home.presentation.model.CurrentDateUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.component.AddRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.home.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import kotlinx.collections.immutable.PersistentList

interface RootHomeComponent : UnidirectionalComponent<RootHomeComponent.Event, RootHomeComponent.State, RootHomeComponent.Effect> {


  val addRoutineDialogHomeScreen: Value<ChildSlot<DialogSlotHomeComponent.AddRoutineDialogHome, AddRoutineDialogComponent>>
  val updateRoutineDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.UpdateRoutineDialog, UpdateRoutineDialogComponent>>
  val errorDialogScreen: Value<ChildSlot<DialogSlotHomeComponent.ErrorDialog, ErrorDialogComponent>>

  @Immutable
  sealed class Event {
    data class CheckedRoutine(val routine: RoutineUiModel) : Event()
    data class UpdateRoutine(val routine: RoutineUiModel) : Event()
    data class OnShowErrorDialog(val errorDialogUiModel: ErrorDialogUiModel) : Event()
    data class SearchRoutine(val routineName: String) : Event()
    data class OnShowUpdateRoutineDialog(val routine: RoutineUiModel) : Event()
    data object GetRoutines : Event()
  }

  @Immutable
  sealed class Effect {
    data class ShowToast(val message: MessageUi) : Effect()
    data class ShowSnackBar(val message: MessageUi) : Effect()
  }

  @Immutable
  data class State(
    val routines: LoadableData<PersistentList<RoutineUiModel>> = LoadableData.Initial,
    val currentDate: CurrentDateUiModel? = null,
  )
}
