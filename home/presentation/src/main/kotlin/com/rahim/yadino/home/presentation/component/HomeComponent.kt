package com.rahim.yadino.home.presentation.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.home.presentation.model.CurrentDateUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.collections.immutable.PersistentList

interface HomeComponent : UnidirectionalComponent<HomeComponent.Event, HomeComponent.State> {
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
  sealed class HomeEffect {
    data class OpenDialog(val isOpen: Boolean) : HomeEffect()
  }

  @Immutable
  data class State(
    val routines: LoadableData<PersistentList<RoutineUiModel>> = LoadableData.Initial,
    val currentDate: CurrentDateUiModel? = null,
    val errorMessage: ErrorMessageCode? = null,
  )
}
