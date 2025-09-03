package com.rahim.yadino.home.presentation

import androidx.compose.runtime.Immutable
import com.rahim.home.domain.model.RoutineModel
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
interface HomeContract : UnidirectionalViewModel<HomeContract.HomeEvent, HomeContract.HomeState> {
  @Immutable
  sealed class HomeEvent {
    data class AddRoutine(val routine: RoutineModel) : HomeEvent()
    data class CheckedRoutine(val routine: RoutineModel) : HomeEvent()
    data class UpdateRoutine(val routine: RoutineModel) : HomeEvent()
    data class DeleteRoutine(val routine: RoutineModel) : HomeEvent()
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
    val routines: List<RoutineModel> = emptyList(),
    val searchRoutines: List<RoutineModel> = emptyList(),
    val currentYear: Int = 0,
    val currentMonth: Int = 0,
    val currentDay: Int = 0,
    val errorMessage: ErrorMessageCode? = null,
  )
}
