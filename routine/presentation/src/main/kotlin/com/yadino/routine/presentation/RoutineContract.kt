package com.yadino.routine.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalViewModel
import com.yadino.routine.presentation.model.IncreaseDecrease
import com.yadino.routine.presentation.model.RoutinePresentationLayer
import com.yadino.routine.presentation.model.TimeDateRoutinePresentationLayer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

interface RoutineContract : UnidirectionalViewModel<RoutineContract.Event, RoutineContract.State> {
  @Immutable
  sealed class Event {
    data class AddRoutine(val routine: RoutinePresentationLayer) : Event()
    data class CheckedRoutine(val routine: RoutinePresentationLayer) : Event()
    data class UpdateRoutine(val routine: RoutinePresentationLayer) : Event()
    data class DeleteRoutine(val routine: RoutinePresentationLayer) : Event()
    data class SearchRoutineByName(val routineName: String) : Event()
    data class GetRoutines(val timeDate: TimeDateRoutinePresentationLayer) : Event()
    data class WeekChange(val increaseDecrease: IncreaseDecrease) : Event()
    data class MonthChange(val yearNumber: Int, val monthNumber: Int,val increaseDecrease: IncreaseDecrease) : Event()
    data class JustMonthIncrease(val yearNumber: Int, val monthNumber: Int) : Event()
    data class JustMonthDecrease(val yearNumber: Int, val monthNumber: Int) : Event()
    data object GetAllTimes : Event()
  }

  @Stable
  data class State(
    val routines: LoadableData<PersistentList<RoutinePresentationLayer>> = LoadableData.Initial,
    val index: Int = 0,
    val currentYear: Int = 0,
    val currentMonth: Int = 0,
    val currentDay: Int = 0,
    val times: PersistentList<TimeDateRoutinePresentationLayer> = persistentListOf(),
    val timesMonth: PersistentList<TimeDateRoutinePresentationLayer> = persistentListOf(),
  )
}
