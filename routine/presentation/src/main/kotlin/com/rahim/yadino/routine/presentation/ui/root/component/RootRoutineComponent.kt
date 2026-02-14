package com.rahim.yadino.routine.presentation.ui.root.component

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.routine.presentation.model.ErrorDialogRemoveRoutineUiModel
import com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.routine.presentation.model.IncreaseDecrease
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import com.rahim.yadino.routine.presentation.model.TimeDateUiModel
import com.rahim.yadino.routine.presentation.ui.addRoutineDialog.component.AddRoutineDialogComponent
import com.rahim.yadino.routine.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.routine.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

interface RootRoutineComponent : UnidirectionalComponent<RootRoutineComponent.Event, RootRoutineComponent.State, Unit> {

  val addRoutineDialogScreen: Value<ChildSlot<DialogSlotComponent.AddRoutineDialog, AddRoutineDialogComponent>>
  val updateRoutineDialogScreen: Value<ChildSlot<DialogSlotComponent.UpdateRoutineDialog, UpdateRoutineDialogComponent>>
  val errorDialogRemoveRoutineScreen: Value<ChildSlot<DialogSlotComponent.ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>>
  val errorDialogScreen: Value<ChildSlot<DialogSlotComponent.ErrorDialog, ErrorDialogComponent>>

  @Immutable
  sealed class Event {
    data class CheckedRoutine(val routine: RoutineUiModel) : Event()
    data class ShowUpdateDialog(val routine: RoutineUiModel) : Event()
    data class ShowErrorRemoveRoutineDialog(val errorDialogRemoveRoutineUiModel: ErrorDialogRemoveRoutineUiModel) : Event()
    data class ShowErrorDialog(val errorDialogUiModel: ErrorDialogUiModel) : Event()
    data class SearchRoutineByName(val routineName: String) : Event()
    data class GetRoutines(val timeDate: TimeDateUiModel) : Event()
    data class WeekChange(val increaseDecrease: IncreaseDecrease) : Event()
    data class MonthChange(val increaseDecrease: IncreaseDecrease) : Event()
    data object GetAllTimes : Event()
    data object ShowAddRoutineDialog : Event()
  }

  @Immutable
  data class State(
    val routines: LoadableData<PersistentList<RoutineUiModel>> = LoadableData.Initial,
    val index: Int = 0,
    val currentYear: Int = 0,
    val currentMonth: Int = 0,
    val currentDay: Int = 0,
    val errorMessage: ErrorMessage? = null,
    val times: PersistentList<TimeDateUiModel> = persistentListOf(),
    val timesMonth: PersistentList<TimeDateUiModel> = persistentListOf(),
  )
}
