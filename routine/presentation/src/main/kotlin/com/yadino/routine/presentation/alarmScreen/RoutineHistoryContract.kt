package com.yadino.routine.presentation.alarmScreen

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.UnidirectionalViewModel
import com.rahim.yadino.enums.error.ErrorMessageCode
import com.yadino.routine.domain.model.RoutineDomainLayer
import kotlinx.collections.immutable.PersistentList

interface RoutineHistoryContract : UnidirectionalViewModel<RoutineHistoryContract.HistoryEvent, RoutineHistoryContract.HistoryState> {
  @Immutable
  sealed class HistoryEvent() {
    data object GetAllRoutine : HistoryEvent()
  }

  @Immutable
  data class HistoryState(
    val routines: LoadableData<PersistentList<RoutineDomainLayer>> = LoadableData.Initial,
    val errorMessage: ErrorMessageCode? = null,
  )
}
