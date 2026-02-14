package com.rahim.yadino.onboarding.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.sharedPreferences.repo.SharedPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class OnBoardingComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  private val sharedPreferencesRepository: SharedPreferencesRepository,
  private val onNavigateToHome: () -> Unit,
) : OnBoardingComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(OnBoardingComponent.WelcomeState())
  override val state: Value<OnBoardingComponent.WelcomeState> = _state

  override val effects: Flow<Unit>
    get() = Channel<Unit>(BUFFERED).consumeAsFlow()

  override fun onEvent(event: OnBoardingComponent.WelcomeEvent) = when (event) {
    is OnBoardingComponent.WelcomeEvent.SaveShowWelcome -> {
      saveShowWelcome(event.isShow)
    }
  }

  private fun checkShowWelcome() {
    scope.launch {
      sharedPreferencesRepository.isShowWelcomeScreen().catch {}.collect {
        if (it) {
          onNavigateToHome()
        }
      }
    }
  }

  private fun saveShowWelcome(isShow: Boolean) {
    scope.launch {
      sharedPreferencesRepository.saveShowWelcome(isShow)
      onNavigateToHome()
    }
  }
}
