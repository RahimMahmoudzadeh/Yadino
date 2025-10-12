package com.yadino.routine.presentation.model

import kotlinx.collections.immutable.PersistentList

data class IncompleteOrCompletedRoutinesUiModel(
  val incompleteRoutine: PersistentList<RoutineUiModel>,
  val completedRoutine: PersistentList<RoutineUiModel>,
)
