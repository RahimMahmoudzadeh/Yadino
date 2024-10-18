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
    data class GetTimesMonth(val yearNumber: Int,val monthNumber: Int) : RoutineEvent()
    data object GetAllTimes : RoutineEvent()
    data class GetRoutines(val timeDate: TimeDate) : RoutineEvent()
    data object MonthIncrease : RoutineEvent()
    data object MonthDecrease : RoutineEvent()
    data object WeekIncrease : RoutineEvent()
    data object WeekDecrease : RoutineEvent()
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
    val currentYear: Int = 0,
    val currentMonth: Int = 0,
    val currentDay: Int = 0,
    val times: List<TimeDate> = emptyList(),
    val timesMonth: List<TimeDate> = emptyList(),
    val errorMessage: ErrorMessageCode? = null,
  )
}
