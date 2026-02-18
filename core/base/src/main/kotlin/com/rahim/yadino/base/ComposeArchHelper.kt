package com.rahim.yadino.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

data class Dispatch<EVENT, STATE, EFFECT>(
  val state: STATE,
  val effect: Flow<EFFECT>,
  val event: (EVENT) -> Unit,
)

data class StateDispatch<STATE>(
  val state: STATE,
)

data class EventDispatch<EVENT>(
  val event: (EVENT) -> Unit,
)

data class EffectDispatch<EFFECT>(
  val effect: Flow<EFFECT>,
)

interface EventEmitter<in EVENT> {
  fun onEvent(event: EVENT)
}

interface EffectSource<out EFFECT> {
  val effects: Flow<EFFECT>
}

interface StateSource<out STATE : Any> {
  val state: Value<STATE>
}

interface UnidirectionalComponent<in EVENT, out STATE : Any, out EFFECT> :
  EventEmitter<EVENT>, StateSource<STATE>, EffectSource<EFFECT>

@Composable
inline fun <reified EVENT, STATE : Any, EFFECT> use(component: UnidirectionalComponent<EVENT, STATE, EFFECT>): Dispatch<EVENT, STATE, EFFECT> {
  val state by component.state.subscribeAsState()

  val dispatch = remember(component) {
    { event: EVENT -> component.onEvent(event) }
  }

  return remember(state, component.effects, dispatch) {
    Dispatch(
      state = state,
      event = dispatch,
      effect = component.effects,
    )
  }
}

@Composable
inline fun <STATE : Any> use(component: StateSource<STATE>): StateDispatch<STATE> {
  val state by component.state.subscribeAsState()
  return StateDispatch(
    state = state,
  )
}

@Composable
inline fun <reified EVENT> use(component: EventEmitter<EVENT>): EventDispatch<EVENT> {
  val dispatch = remember(component) {
    { event: EVENT -> component.onEvent(event) }
  }

  return remember(dispatch) {
    EventDispatch(
      event = dispatch,
    )
  }
}

@Composable
inline fun <EFFECT> use(component: EffectSource<EFFECT>): EffectDispatch<EFFECT> {
  return remember(component.effects) {
    EffectDispatch(
      effect = component.effects,
    )
  }
}

data class StateEffectDispatch<EVENT, EFFECT, STATE>(
  val state: STATE,
  val effectFlow: Flow<EFFECT>,
  val dispatch: (EVENT) -> Unit,
)


@Composable
inline fun <reified EVENT, STATE, EFFECT> use(viewModel: UnidirectionalViewModel<EVENT, STATE, EFFECT>): Dispatch<EVENT, STATE, EFFECT> {
  val state by viewModel.state.collectAsStateWithLifecycle()
  val effect = viewModel.effect

  val dispatch: (EVENT) -> Unit = { event ->
    viewModel.event(event)
  }
  return Dispatch(
    state = state,
    event = dispatch,
    effect = effect,
  )
}

@Composable
inline fun <reified BASE_EVENT, BASE_EFFECT, BASE_STATE> useBase(viewModel: BaseUnidirectionalViewModel<BASE_EVENT, BASE_EFFECT, BASE_STATE>): StateEffectDispatch<BASE_EVENT, BASE_EFFECT, BASE_STATE> {
  val state by viewModel.baseState.collectAsStateWithLifecycle()

  val dispatch: (BASE_EVENT) -> Unit = { event ->
    viewModel.baseEvent(event)
  }
  return StateEffectDispatch(
    state = state,
    effectFlow = viewModel.baseEffect,
    dispatch = dispatch,
  )
}

interface UnidirectionalViewModel<EVENT, STATE, EFFECT> {
  val state: StateFlow<STATE>
  val effect: Flow<EFFECT>
    get() = flow {}

  fun event(event: EVENT)
}

interface BaseUnidirectionalViewModel<BASE_EVENT, BASE_EFFECT, BASE_STATE> {
  val baseState: StateFlow<BASE_STATE>
  val baseEffect: Flow<BASE_EFFECT>
  fun baseEvent(event: BASE_EVENT)
}



