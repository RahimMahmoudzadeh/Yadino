package com.rahim.yadino.routine.homeScreen

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.model.RoutineModel

interface HomeContract : UnidirectionalViewModel<HomeContract.HomeEvent, HomeContract.HomeState> {
  @Immutable
  sealed class HomeEvent {
    data class AddRoutine(val routine: RoutineModel) : HomeEvent()
    data object GetRoutines : HomeEvent()
    data class UpdateRoutine(val routine: RoutineModel) : HomeEvent()
  }

  @Immutable
  sealed class HomeEffect {
    data class OpenDialog(val isOpen: Boolean) : HomeEffect()
  }

  @Immutable
  data class HomeState(
    val routineLoading: Boolean = true,
    val routines: List<RoutineModel> = emptyList(),
  )
}
