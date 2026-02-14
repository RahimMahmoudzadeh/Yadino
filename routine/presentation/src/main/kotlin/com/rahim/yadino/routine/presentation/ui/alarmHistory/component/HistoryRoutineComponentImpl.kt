package com.rahim.yadino.routine.presentation.ui.alarmHistory.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.routine.domain.useCase.GetAllRoutineUseCase
import com.rahim.yadino.routine.presentation.mapper.toRoutineUiModel
import com.rahim.yadino.routine.presentation.model.IncompleteOrCompletedRoutinesUiModel
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HistoryRoutineComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  private val getAllRoutineUseCase: GetAllRoutineUseCase,
) : HistoryRoutineComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(HistoryRoutineComponent.HistoryState())
  override val state: Value<HistoryRoutineComponent.HistoryState> = _state

  init {
    lifecycle.doOnCreate {
      getAllRoutine()
    }
  }

  private fun getAllRoutine() {
    scope.launch {
      _state.update {
        it.copy(
          incompleteOrCompletedRoutinesUiModel = LoadableData.Loading,
        )
      }
      val routines = getAllRoutineUseCase().map { it.toRoutineUiModel() }
      val (completedRoutine, incompleteRoutine) = if (routines.isNotEmpty()) {
        routines.partition { sort -> sort.isChecked }
      } else {
        persistentListOf<RoutineUiModel>() to persistentListOf()
      }
      _state.update {
        it.copy(
          incompleteOrCompletedRoutinesUiModel = LoadableData.Loaded(
            IncompleteOrCompletedRoutinesUiModel(
              incompleteRoutine = incompleteRoutine.toPersistentList(),
              completedRoutine = completedRoutine.toPersistentList(),
            ),
          ),
        )
      }
    }
  }
}
