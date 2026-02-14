package com.rahim.yadino.routine.presentation.ui.addRoutineDialog.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.Constants.DAY_MIN
import com.rahim.yadino.Constants.MONTH_MAX
import com.rahim.yadino.Constants.MONTH_MIN
import com.rahim.yadino.base.toMessageUi
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.routine.domain.useCase.AddReminderUseCase
import com.rahim.yadino.routine.domain.useCase.GetCurrentTimeUseCase
import com.rahim.yadino.routine.domain.useCase.GetTimesMonthUseCase
import com.rahim.yadino.routine.presentation.mapper.toCurrentTimeUiModel
import com.rahim.yadino.routine.presentation.mapper.toRoutine
import com.rahim.yadino.routine.presentation.mapper.toTimeDateUiModel
import com.rahim.yadino.routine.presentation.model.IncreaseDecrease
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AddRoutineDialogComponentImpl(
  componentContext: ComponentContext,
  mainDispatcher: CoroutineContext,
  ioDispatcher: CoroutineContext,
  private val addReminderUseCase: AddReminderUseCase,
  private val getTimesMonthUseCase: GetTimesMonthUseCase,
  private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
  private val onDismissed: () -> Unit,
) : AddRoutineDialogComponent, ComponentContext by componentContext {

  private val scope = coroutineScope(mainDispatcher + SupervisorJob())
  private val ioScope = coroutineScope(ioDispatcher + SupervisorJob())

  private val _state = MutableValue(AddRoutineDialogComponent.State())
  override val state: Value<AddRoutineDialogComponent.State> = _state

  private val _effect = Channel<AddRoutineDialogComponent.Effect>(Channel.BUFFERED)
  override val effects: Flow<AddRoutineDialogComponent.Effect> = _effect.consumeAsFlow()

  init {
    lifecycle.doOnCreate {
      getTimesCurrentMonth()
    }
  }

  override fun onEvent(event: AddRoutineDialogComponent.Event) = when (event) {
    AddRoutineDialogComponent.Event.Dismiss -> onDismissed()
    is AddRoutineDialogComponent.Event.CreateRoutine -> addRoutine(event.routine)
    is AddRoutineDialogComponent.Event.MonthChange -> checkDialogMonthChange(monthNumber = event.monthNumber, yearNumber = event.yearNumber, increaseDecrease = event.increaseDecrease)
  }

  private fun addRoutine(routine: RoutineUiModel) {
    scope.launch {
      runCatching {
        addReminderUseCase.invoke(routine.toRoutine())
      }.onSuccess { resource ->
        _effect.send(AddRoutineDialogComponent.Effect.ShowToast(resource.toMessageUi(onDismissed)))
      }.onFailure {
        _effect.send(AddRoutineDialogComponent.Effect.ShowToast(MessageUi.ERROR_SAVE_REMINDER))
      }
    }
  }

  private fun checkDialogMonthChange(monthNumber: Int, yearNumber: Int, increaseDecrease: IncreaseDecrease) {
    when (increaseDecrease) {
      IncreaseDecrease.INCREASE -> monthIncrease(monthNumber, yearNumber) { year, month ->
        getTimes(year, month)
      }

      IncreaseDecrease.DECREASE ->
        monthDecrease(monthNumber, yearNumber) { year, month ->
          getTimes(year, month)
        }
    }
  }

  private fun getTimes(yearNumber: Int, monthNumber: Int) {
    ioScope.launch {
      val times = getTimesMonthUseCase(yearNumber, monthNumber).toCollection(ArrayList())
      val isCheckedTime = times.find { it.isChecked || it.isToday }
      if (isCheckedTime == null) {
        val time = times.indexOfFirst { it.dayNumber == DAY_MIN }
        times[time] = times.first { it.dayNumber == DAY_MIN }.copy(isChecked = true)
      }
      _state.update {
        it.copy(timesMonth = times.map { it.toTimeDateUiModel() }.toPersistentList())
      }
    }
  }

  private fun getTimesCurrentMonth() {
    val currentTime = getCurrentTimeUseCase().toCurrentTimeUiModel()

    ioScope.launch {
      val times = getTimesMonthUseCase(currentTime.currentYear, currentTime.currentMonth).toCollection(ArrayList())
      val isCheckedTime = times.find { it.isChecked || it.isToday }
      if (isCheckedTime == null) {
        val time = times.indexOfFirst { it.dayNumber == DAY_MIN }
        times[time] = times.first { it.dayNumber == DAY_MIN }.copy(isChecked = true)
      }
      _state.update {
        it.copy(timesMonth = times.map { it.toTimeDateUiModel() }.toPersistentList(), currentTime = currentTime)
      }
    }
  }

  private fun monthDecrease(month: Int, year: Int, time: (year: Int, month: Int) -> Unit) {
    ioScope.launch {
      var month = month.minus(MONTH_MIN)
      var year = year
      if (month < MONTH_MIN) {
        month = MONTH_MAX
        year = year.minus(MONTH_MIN)
      }
      time(year, month)
    }
  }

  private fun monthIncrease(month: Int, year: Int, time: (year: Int, month: Int) -> Unit) {
    ioScope.launch {
      var month = month.plus(MONTH_MIN)
      var year = year
      if (month > MONTH_MAX) {
        month = MONTH_MIN
        year = year.plus(MONTH_MIN)
      }
      time(year, month)
    }
  }
}
