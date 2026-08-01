package com.rahim.yadino.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

data class StateDispatch<EVENT, STATE, EFFECT>(
  val state: STATE,
  val effect: Flow<EFFECT>,
  val event: (EVENT) -> Unit,
)


interface UnidirectionalViewModel<in EVENT, out STATE : Any, out EFFECT> {
  val state: StateFlow<STATE>
  val effects: Flow<EFFECT>
  fun onEvent(event: EVENT)

}

@Composable
inline fun <reified EVENT, STATE : Any, EFFECT> use(viewModel: UnidirectionalViewModel<EVENT, STATE, EFFECT>): StateDispatch<EVENT, STATE, EFFECT> {
  val state by viewModel.state.collectAsStateWithLifecycle()

  val dispatch = remember(viewModel) {
    { event: EVENT -> viewModel.onEvent(event) }
  }

  return remember(state, viewModel.effects, dispatch) {
    StateDispatch(
      state = state,
      event = dispatch,
      effect = viewModel.effects,
    )
  }
}


@Composable
inline fun <reified BASE_EVENT, BASE_EFFECT, BASE_STATE> useBase(viewModel: BaseUnidirectionalViewModel<BASE_EVENT, BASE_EFFECT, BASE_STATE>): StateDispatch<BASE_EVENT, BASE_STATE,BASE_EFFECT> {
  val state by viewModel.baseState.collectAsStateWithLifecycle()

  val event: (BASE_EVENT) -> Unit = { event ->
    viewModel.baseEvent(event)
  }
  return StateDispatch(
    state = state,
    effect = viewModel.baseEffect,
    event = event,
  )
}

interface BaseUnidirectionalViewModel<BASE_EVENT, BASE_EFFECT, BASE_STATE> {
  val baseState: StateFlow<BASE_STATE>
  val baseEffect: Flow<BASE_EFFECT>
  fun baseEvent(event: BASE_EVENT)
}



