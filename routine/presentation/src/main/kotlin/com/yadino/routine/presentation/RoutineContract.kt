package com.yadino.routine.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.yadino.routine.domain.model.RoutineModelDomainLayer
import com.yadino.routine.presentation.model.TimeDateRoutinePresentationLayer

interface RoutineContract : UnidirectionalViewModel<RoutineContract.RoutineEvent, RoutineContract.RoutineState> {
  @Immutable
  sealed class RoutineEvent {
    data class AddRoutine(val routine: RoutineModelDomainLayer) : RoutineEvent()
    data class CheckedRoutine(val routine: RoutineModelDomainLayer) : RoutineEvent()
    data class UpdateRoutine(val routine: RoutineModelDomainLayer) : RoutineEvent()
    data class DeleteRoutine(val routine: RoutineModelDomainLayer) : RoutineEvent()
    data class SearchRoutine(val routineName: String) : RoutineEvent()
    data class GetRoutines(val timeDate: TimeDateRoutinePresentationLayer) : RoutineEvent()
    data class MonthIncrease(val yearNumber: Int, val monthNumber: Int) : RoutineEvent()
    data class MonthDecrease(val yearNumber: Int, val monthNumber: Int) : RoutineEvent()
    data class JustMonthIncrease(val yearNumber: Int, val monthNumber: Int) : RoutineEvent()
    data class JustMonthDecrease(val yearNumber: Int, val monthNumber: Int) : RoutineEvent()
    data object GetAllTimes : RoutineEvent()
    data object WeekIncrease : RoutineEvent()
    data object WeekDecrease : RoutineEvent()
  }

  @Stable
  data class RoutineState(
      val routineLoading: Boolean = true,
      val routines: List<RoutineModelDomainLayer> = emptyList(),
      val searchRoutines: List<RoutineModelDomainLayer> = emptyList(),
      val index: Int = 0,
      val currentYear: Int = 0,
      val currentMonth: Int = 0,
      val currentDay: Int = 0,
      val times: List<TimeDateRoutinePresentationLayer> = emptyList(),
      val timesMonth: List<TimeDateRoutinePresentationLayer> = emptyList(),
      val errorMessage: ErrorMessageCode? = null,
  )
}
