package com.rahim.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.rahim.yadino.home.domain.useCase.AddReminderUseCase
import com.rahim.yadino.home.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.home.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.home.domain.useCase.GetCurrentDateUseCase
import com.rahim.yadino.home.domain.useCase.GetTodayRoutinesUseCase
import com.rahim.yadino.home.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.home.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.home.presentation.component.HomeComponent
import com.rahim.yadino.home.presentation.component.HomeComponentImpl
import com.rahim.yadino.navigation.config.ConfigChildComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class RootComponentImpl(componentContext: ComponentContext) : RootComponent, ComponentContext by componentContext, KoinComponent {
  private val navigation = StackNavigation<ConfigChildComponent>()

  private val addReminderUseCase: AddReminderUseCase = get()
  private val updateReminderUseCase: UpdateReminderUseCase = get()
  private val cancelReminderUseCase: CancelReminderUseCase = get()
  private val deleteReminderUseCase: DeleteReminderUseCase = get()
  private val getTodayRoutinesUseCase: GetTodayRoutinesUseCase = get()
  private val searchRoutineUseCase: SearchRoutineUseCase = get()
  private val getCurrentDateUseCase: GetCurrentDateUseCase = get()

  override val stack: Value<ChildStack<*, RootComponent.ChildStack>> = childStack(
    source = navigation,
    serializer = ConfigChildComponent.serializer(),
    initialConfiguration = ConfigChildComponent.Home,
    handleBackButton = true,
    childFactory = ::childComponent,
  )

  private fun homeComponent(componentContext: ComponentContext): HomeComponent = HomeComponentImpl(
    componentContext = componentContext,
    mainContext = Dispatchers.Main,
    addReminderUseCase = addReminderUseCase,
    updateReminderUseCase = updateReminderUseCase,
    cancelReminderUseCase = cancelReminderUseCase,
    deleteReminderUseCase = deleteReminderUseCase,
    getTodayRoutinesUseCase = getTodayRoutinesUseCase,
    searchRoutineUseCase = searchRoutineUseCase,
    getCurrentDateUseCase = getCurrentDateUseCase,
  )

  private fun childComponent(
    config: ConfigChildComponent,
    childComponentContext: ComponentContext,
  ): RootComponent.ChildStack = when (config) {
    is ConfigChildComponent.Home -> RootComponent.ChildStack.HomeStack(homeComponent = homeComponent(componentContext = childComponentContext))

    else -> {
      RootComponent.ChildStack.HomeStack(homeComponent = homeComponent(componentContext = childComponentContext))
    }
  }
}
