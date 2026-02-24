package com.rahim.yadino.onboarding.presentation.ui.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.onBoarding.domain.useCase.IsShowWelcomeScreenUseCase
import com.rahim.yadino.onBoarding.domain.useCase.SaveShowWelcomeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class OnBoardingComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  private val isShowWelcomeScreenUseCase: IsShowWelcomeScreenUseCase,
  private val saveShowWelcomeUseCase: SaveShowWelcomeUseCase,
  private val onNavigateToHome: () -> Unit,
) : OnBoardingComponent, ComponentContext by componentContext {

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())

  private val _state = MutableValue(OnBoardingComponent.WelcomeState())
  override val state: Value<OnBoardingComponent.WelcomeState> = _state

  init {
    lifecycle.doOnCreate {
      checkShowWelcome()
    }
  }

  override fun onEvent(event: OnBoardingComponent.WelcomeEvent) = when (event) {
    is OnBoardingComponent.WelcomeEvent.SaveShowWelcome -> {
      saveShowWelcome()
    }
  }

  private fun checkShowWelcome() {
    scope.launch {
      isShowWelcomeScreenUseCase().catch {}.collect {
        if (it) {
          onNavigateToHome()
        }
      }
    }
  }

  private fun saveShowWelcome() {
    scope.launch {
      saveShowWelcomeUseCase()
      onNavigateToHome()
    }
  }
}
