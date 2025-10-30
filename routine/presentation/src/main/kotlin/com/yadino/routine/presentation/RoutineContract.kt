package com.yadino.routine.presentation

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.yadino.routine.presentation.model.IncreaseDecrease
import com.yadino.routine.presentation.model.RoutineUiModel
import com.yadino.routine.presentation.model.TimeDateUiModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

interface RoutineContract : UnidirectionalComponent<RoutineContract.Event, RoutineContract.State> {
  @Immutable
  sealed class Event {
    data class AddRoutine(val routine: RoutineUiModel) : Event()
    data class CheckedRoutine(val routine: RoutineUiModel) : Event()
    data class UpdateRoutine(val routine: RoutineUiModel) : Event()
    data class DeleteRoutine(val routine: RoutineUiModel) : Event()
    data class SearchRoutineByName(val routineName: String) : Event()
    data class GetRoutines(val timeDate: TimeDateUiModel) : Event()
    data class WeekChange(val increaseDecrease: IncreaseDecrease) : Event()
    data class MonthChange(val increaseDecrease: IncreaseDecrease) : Event()
    data class DialogMonthChange(val yearNumber: Int, val monthNumber: Int, val increaseDecrease: IncreaseDecrease) : Event()
    data object GetAllTimes : Event()
  }

  @Immutable
  data class State(
      val routines: LoadableData<PersistentList<RoutineUiModel>> = LoadableData.Initial,
      val index: Int = 0,
      val currentYear: Int = 0,
      val currentMonth: Int = 0,
      val currentDay: Int = 0,
      val errorMessageCode: ErrorMessageCode? = null,
      val times: PersistentList<TimeDateUiModel> = persistentListOf(),
      val timesMonth: PersistentList<TimeDateUiModel> = persistentListOf(),
  )
}
