package com.rahim.yadino.routine.presentation.component

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalComponent
import com.rahim.yadino.enums.message.error.ErrorMessage
import com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.routine.presentation.model.IncreaseDecrease
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import com.rahim.yadino.routine.presentation.model.TimeDateUiModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

interface RoutineComponent : UnidirectionalComponent<RoutineComponent.Event, RoutineComponent.State, Nothing> {
  @Immutable
  sealed class Event {
    data class CheckedRoutine(val routine: RoutineUiModel) : Event()
    data class OnShowUpdateDialog(val routine: RoutineUiModel) : Event()
    data class OnShowErrorDialog(val errorDialogUiModel: ErrorDialogUiModel) : Event()
    data class SearchRoutineByName(val routineName: String) : Event()
    data class GetRoutines(val timeDate: TimeDateUiModel) : Event()
    data class WeekChange(val increaseDecrease: IncreaseDecrease) : Event()
    data class MonthChange(val increaseDecrease: IncreaseDecrease) : Event()
    data object GetAllTimes : Event()
  }

  @Immutable
  data class State(
    val routines: LoadableData<PersistentList<RoutineUiModel>> = LoadableData.Initial,
    val index: Int = 0,
    val currentYear: Int = 0,
    val currentMonth: Int = 0,
    val currentDay: Int = 0,
    val errorMessage: ErrorMessage? = null,
    val times: PersistentList<TimeDateUiModel> = persistentListOf(),
    val timesMonth: PersistentList<TimeDateUiModel> = persistentListOf(),
  )
}
