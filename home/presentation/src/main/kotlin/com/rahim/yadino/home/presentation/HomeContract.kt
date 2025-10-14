package com.rahim.yadino.home.presentation

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.home.presentation.model.CurrentDateUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import kotlinx.collections.immutable.PersistentList

interface HomeContract : UnidirectionalViewModel<HomeContract.HomeEvent, HomeContract.HomeState> {
  @Immutable
  sealed class HomeEvent {
    data class AddRoutine(val routine: RoutineUiModel) : HomeEvent()
    data class CheckedRoutine(val routine: RoutineUiModel) : HomeEvent()
    data class UpdateRoutine(val routine: RoutineUiModel) : HomeEvent()
    data class DeleteRoutine(val routine: RoutineUiModel) : HomeEvent()
    data class SearchRoutine(val routineName: String) : HomeEvent()
    data object GetRoutines : HomeEvent()
  }

  @Immutable
  sealed class HomeEffect {
    data class OpenDialog(val isOpen: Boolean) : HomeEffect()
  }

  @Immutable
  data class HomeState(
    val routines: LoadableData<PersistentList<RoutineUiModel>> = LoadableData.Initial,
    val currentDate: CurrentDateUiModel? = null,
    val errorMessage: ErrorMessageCode? = null,
  )
}
