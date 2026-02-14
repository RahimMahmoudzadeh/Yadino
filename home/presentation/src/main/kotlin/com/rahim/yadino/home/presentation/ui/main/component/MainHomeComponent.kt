package com.rahim.yadino.home.presentation.ui.main.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.home.presentation.model.CurrentDateUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogRemoveUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.collections.immutable.PersistentList

interface MainHomeComponent : UnidirectionalComponent<MainHomeComponent.Event, MainHomeComponent.State, MainHomeComponent.Effect> {

  @Immutable
  sealed class Event {
    data class CheckedRoutine(val routine: RoutineUiModel) : Event()
    data class UpdateRoutine(val routine: RoutineUiModel) : Event()
    data class SearchRoutine(val routineName: String) : Event()
    data class ShowErrorDialogRemoveRoutine(val errorDialogModel: ErrorDialogRemoveUiModel) : Event()
    data class ShowUpdateRoutineDialog(val updateRoutine: RoutineUiModel) : Event()
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
