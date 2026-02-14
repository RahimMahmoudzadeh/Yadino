package com.rahim.yadino.routine.presentation.ui.root.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.rahim.yadino.Constants.DAY_MIN
import com.rahim.yadino.Constants.MONTH_MAX
import com.rahim.yadino.Constants.MONTH_MIN
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.core.timeDate.repo.DateTimeRepository
import com.rahim.yadino.routine.domain.useCase.AddReminderUseCase
import com.rahim.yadino.routine.domain.useCase.CancelReminderUseCase
import com.rahim.yadino.routine.domain.useCase.DeleteReminderUseCase
import com.rahim.yadino.routine.domain.useCase.GetCurrentTimeUseCase
import com.rahim.yadino.routine.domain.useCase.GetRemindersUseCase
import com.rahim.yadino.routine.domain.useCase.GetTimesMonthUseCase
import com.rahim.yadino.routine.domain.useCase.SearchRoutineUseCase
import com.rahim.yadino.routine.domain.useCase.UpdateReminderUseCase
import com.rahim.yadino.routine.presentation.mapper.toRoutine
import com.rahim.yadino.routine.presentation.mapper.toRoutineUiModel
import com.rahim.yadino.routine.presentation.mapper.toTimeDateUiModel
import com.rahim.yadino.routine.presentation.model.ErrorDialogRemoveRoutineUiModel
import com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.routine.presentation.model.IncreaseDecrease
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import com.rahim.yadino.routine.presentation.ui.addRoutineDialog.component.AddRoutineDialogComponent
import com.rahim.yadino.routine.presentation.ui.addRoutineDialog.component.AddRoutineDialogComponentImpl
import com.rahim.yadino.routine.presentation.ui.errorDialog.component.ErrorDialogComponent
import com.rahim.yadino.routine.presentation.ui.errorDialog.component.ErrorDialogComponentImpl
import com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponentImpl
import com.rahim.yadino.routine.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponent
import com.rahim.yadino.routine.presentation.ui.updateDialogRoutine.component.UpdateRoutineDialogComponentImpl
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class RootRoutineComponentImpl(
  componentContext: ComponentContext,
  mainContext: CoroutineContext,
  ioContext: CoroutineContext,
  private val addReminderUseCase: AddReminderUseCase,
  private val getTimesMonthUseCase: GetTimesMonthUseCase,
  private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
  private val updateReminderUseCase: UpdateReminderUseCase,
  private val deleteReminderUseCase: DeleteReminderUseCase,
  private val cancelReminderUseCase: CancelReminderUseCase,
  private val getRemindersUseCase: GetRemindersUseCase,
  private val searchRoutineUseCase: SearchRoutineUseCase,
  private val dateTimeRepository: DateTimeRepository,
) : RootRoutineComponent, ComponentContext by componentContext {

  private val addRoutineDialogNavigationSlot =
    SlotNavigation<DialogSlotComponent.AddRoutineDialog>()

  private val updateRoutineDialogNavigationSlot =
    SlotNavigation<DialogSlotComponent.UpdateRoutineDialog>()

  private val errorDialogRemoveRoutineNavigationSlot =
    SlotNavigation<DialogSlotComponent.ErrorDialogRemoveRoutine>()

  private val errorDialogNavigationSlot =
    SlotNavigation<DialogSlotComponent.ErrorDialog>()

  private val scope: CoroutineScope = coroutineScope(mainContext + SupervisorJob())
  private val ioScope: CoroutineScope = coroutineScope(ioContext + SupervisorJob())

  private var lastYearNumber = dateTimeRepository.currentTimeYear
  private var lastMonthNumber = dateTimeRepository.currentTimeMonth
  private var lastDayNumber = dateTimeRepository.currentTimeDay

  private var _state = MutableValue(RootRoutineComponent.State())
  override val state: Value<RootRoutineComponent.State> = _state

  override val effects: Flow<Unit>
    get() = Channel<Unit>(BUFFERED).consumeAsFlow()

  init {
    lifecycle.doOnCreate {
      setCurrentTime()
      getTimesMonth()
      getRoutines()
      getTimes()
    }
  }

  private var searchNameRoutine = ""

  override val addRoutineDialogScreen: Value<ChildSlot<DialogSlotComponent.AddRoutineDialog, AddRoutineDialogComponent>> =
    childSlot(
      source = addRoutineDialogNavigationSlot,
      serializer = DialogSlotComponent.AddRoutineDialog.serializer(),
      handleBackButton = true,
      key = "addRoutineDialogNavigationSlot",
    ) { config, childComponentContext ->
      AddRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        addReminderUseCase = addReminderUseCase,
        getTimesMonthUseCase = getTimesMonthUseCase,
        getCurrentTimeUseCase = getCurrentTimeUseCase,
        onDismissed = addRoutineDialogNavigationSlot::dismiss,
      )
    }

  override val updateRoutineDialogScreen: Value<ChildSlot<DialogSlotComponent.UpdateRoutineDialog, UpdateRoutineDialogComponent>> =
    childSlot(
      source = updateRoutineDialogNavigationSlot,
      serializer = DialogSlotComponent.UpdateRoutineDialog.serializer(),
      handleBackButton = true,
      key = "updateRoutineDialogScreen",
    ) { config, childComponentContext ->
      UpdateRoutineDialogComponentImpl(
        componentContext = childComponentContext,
        mainDispatcher = Dispatchers.Main,
        ioDispatcher = Dispatchers.IO,
        updateReminderUseCase = updateReminderUseCase,
        updateRoutine = config.updateRoutine,
        getTimesMonthUseCase = getTimesMonthUseCase,
        getCurrentTimeUseCase = getCurrentTimeUseCase,
        onDismissed = updateRoutineDialogNavigationSlot::dismiss,
      )
    }

  override val errorDialogRemoveRoutineScreen: Value<ChildSlot<DialogSlotComponent.ErrorDialogRemoveRoutine, ErrorDialogRemoveRoutineComponent>> =
    childSlot(
      source = errorDialogRemoveRoutineNavigationSlot,
      serializer = DialogSlotComponent.ErrorDialogRemoveRoutine.serializer(),
      handleBackButton = true,
      key = "errorDialogRemoveRoutineNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogRemoveRoutineComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        deleteReminderUseCase = deleteReminderUseCase,
        errorDialogRemoveRoutineUiModel = config.errorDialogRemoveRoutineUiModel,
        onDismissed = errorDialogRemoveRoutineNavigationSlot::dismiss,
      )
    }

  override val errorDialogScreen: Value<ChildSlot<DialogSlotComponent.ErrorDialog, ErrorDialogComponent>> =
    childSlot(
      source = errorDialogNavigationSlot,
      serializer = DialogSlotComponent.ErrorDialog.serializer(),
      handleBackButton = true,
      key = "errorDialogNavigationSlot",
    ) { config, childComponentContext ->
      ErrorDialogComponentImpl(
        componentContext = childComponentContext,
        mainContext = Dispatchers.Main,
        errorDialogUiModel = config.errorDialogUiModel,
        onDismissed = errorDialogNavigationSlot::dismiss,
      )
    }

  override fun onEvent(event: RootRoutineComponent.Event) {
    when (event) {
      is RootRoutineComponent.Event.CheckedRoutine -> checkedRoutine(event.routine)
      is RootRoutineComponent.Event.ShowErrorRemoveRoutineDialog -> showErrorDialogRemoveRoutine(event.errorDialogRemoveRoutineUiModel)
      is RootRoutineComponent.Event.GetRoutines -> {
        event.run {
          updateLastTime(timeDate.yearNumber, timeDate.monthNumber, timeDate.dayNumber)
          updateDayChecked(timeDate.yearNumber, timeDate.monthNumber, timeDate.dayNumber)
          getRoutines(timeDate.yearNumber, timeDate.monthNumber, timeDate.dayNumber, searchText = searchNameRoutine)
        }
      }

      is RootRoutineComponent.Event.SearchRoutineByName -> {
        getRoutines(searchText = event.routineName)
      }

      is RootRoutineComponent.Event.ShowUpdateDialog -> showUpdateDialog(event.routine)
      is RootRoutineComponent.Event.GetAllTimes -> getTimes()
      is RootRoutineComponent.Event.MonthChange -> checkMonthIncreaseOrDecrease(event.increaseDecrease)
      is RootRoutineComponent.Event.WeekChange -> checkWeekIncreaseOrDecrease(event.increaseDecrease)
      is RootRoutineComponent.Event.ShowErrorDialog -> showErrorDialog(event.errorDialogUiModel)
      RootRoutineComponent.Event.ShowAddRoutineDialog -> showAddDialog()
    }
  }


  private fun checkWeekIncreaseOrDecrease(increaseDecrease: IncreaseDecrease) {
    when (increaseDecrease) {
      IncreaseDecrease.INCREASE -> weekIncrease()
      IncreaseDecrease.DECREASE -> weekDecrease()
    }
  }

  private fun checkMonthIncreaseOrDecrease(increaseDecrease: IncreaseDecrease) {
    when (increaseDecrease) {
      IncreaseDecrease.INCREASE -> {
        monthIncrease(month = state.value.currentMonth, year = state.value.currentYear) { year, month ->
          getTimesMonth(year, month)
          updateDayChecked(year, month)
          updateIndex(month, year)
        }
      }

      IncreaseDecrease.DECREASE -> {
        monthDecrease(month = state.value.currentMonth, year = state.value.currentYear) { year, month ->
          getTimesMonth(year, month)
          updateDayChecked(year, month)
          updateIndex(month, year)
        }
      }
    }
  }

  private fun updateLastTime(yearNumber: Int, monthNumber: Int, dayNumber: Int) {
    lastYearNumber = yearNumber
    lastMonthNumber = monthNumber
    lastDayNumber = dayNumber
  }

  private fun weekIncrease() {
    val times = state.value.times
    val index = state.value.index
    val updateIndex = if (times.size <= index + 7) {
      times.size
    } else if (index == -1) {
      index + 8
    } else {
      index + 7
    }
    _state.update {
      it.copy(index = updateIndex)
    }
  }

  private fun weekDecrease() {
    val index = state.value.index
    val updateIndex = if (index <= 6) {
      0
    } else {
      index - 7
    }
    _state.update {
      it.copy(index = updateIndex)
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

  private fun updateIndex(month: Int, year: Int, day: Int = DAY_MIN) {
    ioScope.launch {
      val times = ArrayList(state.value.times)
      times.indexOfFirst { it.monthNumber == month && it.yearNumber == year && it.dayNumber == day }.let { index ->
        _state.update {
          it.copy(index = calculateIndexDay(index))
        }
      }
    }
  }

  private fun setCurrentTime() {
    _state.update {
      it.copy(
        currentDay = dateTimeRepository.currentTimeDay,
        currentMonth = dateTimeRepository.currentTimeMonth,
        currentYear = dateTimeRepository.currentTimeYear,
      )
    }
  }

  private fun getRoutines(yearNumber: Int = lastYearNumber, monthNumber: Int = lastMonthNumber, numberDay: Int = lastDayNumber, searchText: String = "") {
    scope.launch {
      searchNameRoutine = searchText
      if (searchText.isNotBlank()) {
        Timber.tag("routineSearch").d("getRoutines->$searchNameRoutine")
        searchRoutine(searchNameRoutine, this, yearNumber, monthNumber, numberDay)
      } else {
        getNormalRoutines(this, yearNumber, monthNumber, numberDay)
      }
    }
  }

  private suspend fun getNormalRoutines(scope: CoroutineScope, yearNumber: Int, monthNumber: Int, numberDay: Int) {
    _state.update {
      it.copy(routines = LoadableData.Loading)
    }
    getRemindersUseCase.invoke(monthNumber, numberDay, yearNumber, scope).collectLatest { routines ->
      _state.update {
        it.copy(
          routines = LoadableData.Loaded(
            routines.map { it.toRoutineUiModel() }.sortedBy {
              it.timeHours?.replace(":", "")?.toInt()
            }.toPersistentList(),
          ),
        )
      }
    }
  }

  private suspend fun searchRoutine(searchText: String, scope: CoroutineScope, yearNumber: Int, monthNumber: Int, numberDay: Int) {
    _state.update {
      it.copy(routines = LoadableData.Loading)
    }
    searchRoutineUseCase.invoke(searchText, yearNumber, monthNumber, numberDay, scope).collectLatest { searchItems ->
      Timber.tag("routineSearch").d("searchRoutine->$searchText")
      Timber.tag("routineSearch").d("searchItems->$searchItems")
      _state.update {
        it.copy(
          routines = LoadableData.Loaded(
            searchItems.map { it.toRoutineUiModel() }.sortedBy {
              it.timeHours?.replace(":", "")?.toInt()
            }.toPersistentList(),
          ),
        )
      }
    }
  }

  private fun showErrorDialogRemoveRoutine(errorDialogRemoveRoutineUiModel: ErrorDialogRemoveRoutineUiModel) {
    errorDialogRemoveRoutineNavigationSlot.activate(DialogSlotComponent.ErrorDialogRemoveRoutine(errorDialogRemoveRoutineUiModel))
  }

  private fun showUpdateDialog(routine: RoutineUiModel) {
    updateRoutineDialogNavigationSlot.activate(DialogSlotComponent.UpdateRoutineDialog(routine))
  }

  private fun showAddDialog() {
    addRoutineDialogNavigationSlot.activate(DialogSlotComponent.AddRoutineDialog)
  }

  private fun showErrorDialog(errorDialogUiModel: ErrorDialogUiModel) {
    errorDialogNavigationSlot.activate(DialogSlotComponent.ErrorDialog(errorDialogUiModel))
  }

  private fun checkedRoutine(routine: RoutineUiModel) {
    scope.launch {
      cancelReminderUseCase(routine = routine.toRoutine())
    }
  }

  private fun getTimes() {
    scope.launch {
      dateTimeRepository.getTimes().catch {}.collect { times ->
        _state.update {
          it.copy(
            times = times.map { it.toTimeDateUiModel() }.toPersistentList(),
          )
        }
        times.find { it.isChecked }?.let { currentTime ->
          updateIndex(currentTime.monthNumber, currentTime.yearNumber, currentTime.dayNumber)
        }
      }
    }
  }

  private fun getTimesMonth(yearNumber: Int = dateTimeRepository.currentTimeYear, monthNumber: Int = dateTimeRepository.currentTimeMonth) {
    ioScope.launch {
      val times = dateTimeRepository.getTimesMonth(yearNumber, monthNumber).toCollection(ArrayList())
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

  private fun calculateIndexDay(index: Int) = index.minus(index % 7)

  private fun updateDayChecked(yearNumber: Int, monthNumber: Int, day: Int = DAY_MIN) {
    scope.launch {
      dateTimeRepository.updateDayToToday(day, yearNumber, monthNumber)
      _state.update {
        it.copy(currentMonth = monthNumber, currentYear = yearNumber, currentDay = day)
      }
    }
  }
}
