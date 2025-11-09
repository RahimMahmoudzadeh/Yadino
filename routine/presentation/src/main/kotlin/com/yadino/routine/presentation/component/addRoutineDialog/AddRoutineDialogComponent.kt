package com.yadino.routine.presentation.component.addRoutineDialog

import androidx.compose.runtime.Immutable
import com.rahim.yadino.base.UnidirectionalComponent

interface AddRoutineDialogComponent : UnidirectionalComponent<AddRoutineDialogComponent.Event, AddRoutineDialogComponent.State> {

  @Immutable
  sealed class Event {

  }

  @Immutable
  data class State(val date: String = "")

}
