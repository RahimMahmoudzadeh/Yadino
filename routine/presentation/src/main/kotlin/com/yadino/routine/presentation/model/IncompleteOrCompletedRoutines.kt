package com.yadino.routine.presentation.model

import kotlinx.collections.immutable.PersistentList

data class IncompleteOrCompletedRoutines(
  val incompleteRoutine: PersistentList<RoutinePresentationLayer>,
  val completedRoutine: PersistentList<RoutinePresentationLayer>,
)
