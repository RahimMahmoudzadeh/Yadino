package com.rahim.yadino.routine.routineScreen

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.rahim.yadino.model.RoutineModel
import com.rahim.yadino.model.TimeDate

interface RoutineContract : UnidirectionalViewModel<RoutineContract.RoutineEvent, RoutineContract.RoutineState> {
  @Immutable
  sealed class RoutineEvent {
    data class AddRoutine(val routine: RoutineModel) : RoutineEvent()
    data class CheckedRoutine(val routine: RoutineModel) : RoutineEvent()
    data class UpdateRoutine(val routine: RoutineModel) : RoutineEvent()
    data class DeleteRoutine(val routine: RoutineModel) : RoutineEvent()
    data class SearchRoutine(val routineName: String) : RoutineEvent()
    data class SetDayIndex(val index: Int) : RoutineEvent()
    data class GetTimesMonth(val yearNumber: Int,val monthNumber: Int) : RoutineEvent()
    data object GetAllTimes : RoutineEvent()
    data class GetRoutines(val yearNumber: Int, val monthNumber: Int, val numberDay: Int) : RoutineEvent()
    data object ShowSampleRoutines : RoutineEvent()
  }

//  @Immutable
//  sealed class HomeEffect {
//    data class OpenDialog(val isOpen: Boolean) : HomeEffect()
//  }

  @Immutable
  data class RoutineState(
    val routineLoading: Boolean = true,
    val routines: List<RoutineModel> = emptyList(),
    val index: Int = 0,
    val times: List<TimeDate> = emptyList(),
    val timesMonth: List<TimeDate> = emptyList(),
    val errorMessage: ErrorMessageCode? = null,
  )
}
