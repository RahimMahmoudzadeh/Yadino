package com.rahim.yadino.home.presentation

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.home.presentation.model.CurrentDatePresentationLayer
import com.rahim.yadino.home.presentation.model.RoutineHomePresentationLayer

interface HomeContract : UnidirectionalViewModel<HomeContract.HomeEvent, HomeContract.HomeState> {
  @Immutable
  sealed class HomeEvent {
    data class AddRoutine(val routine: RoutineHomePresentationLayer) : HomeEvent()
    data class CheckedRoutine(val routine: RoutineHomePresentationLayer) : HomeEvent()
    data class UpdateRoutine(val routine: RoutineHomePresentationLayer) : HomeEvent()
    data class DeleteRoutine(val routine: RoutineHomePresentationLayer) : HomeEvent()
    data class SearchRoutine(val routineName: String) : HomeEvent()
    data object GetRoutines : HomeEvent()
  }

  @Immutable
  sealed class HomeEffect {
    data class OpenDialog(val isOpen: Boolean) : HomeEffect()
  }

  @Immutable
  data class HomeState(
    val routineLoading: Boolean = true,
    val routines: List<RoutineHomePresentationLayer> = emptyList(),
    val searchRoutines: List<RoutineHomePresentationLayer> = emptyList(),
    val currentDate: CurrentDatePresentationLayer = CurrentDatePresentationLayer(""),
    val errorMessage: ErrorMessageCode? = null,
  )
}
