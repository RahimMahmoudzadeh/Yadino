package com.rahim.yadino.home.presentation.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.home.presentation.model.CurrentDateUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.collections.immutable.PersistentList

interface HomeComponent : UnidirectionalComponent<HomeComponent.Event, HomeComponent.State, HomeComponent.Effect> {
  @Immutable
  sealed class Event {
    data class CheckedRoutine(val routine: RoutineUiModel) : Event()
    data class UpdateRoutine(val routine: RoutineUiModel) : Event()
    data class DeleteRoutine(val routine: RoutineUiModel) : Event()
    data class SearchRoutine(val routineName: String) : Event()
    data class OnShowUpdateRoutineDialog(val routine: RoutineUiModel) : Event()
    data object GetRoutines : Event()
  }

  @Immutable
  sealed class Effect{
    data class ShowToast(val message: MessageUi) : Effect()
    data class ShowSnackBar(val message: MessageUi) : Effect()
  }

  @Immutable
  data class State(
    val routines: LoadableData<PersistentList<RoutineUiModel>> = LoadableData.Initial,
    val currentDate: CurrentDateUiModel? = null,
  )
}
