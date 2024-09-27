package com.rahim.yadino.routine.routineScreen


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahim.yadino.base.Resource
import com.rahim.yadino.base.calculateMonthName
import com.rahim.yadino.base.enums.HalfWeekName
import com.rahim.yadino.base.model.TimeDate
import com.rahim.yadino.base.persianLocate
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ItemRoutine
import com.rahim.yadino.designsystem.component.ProcessRoutineAdded
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.component.goSettingPermission
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.dialog.DialogAddRoutine
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.theme.font_medium
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.base.model.RoutineModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun RoutineRoute(
  modifier: Modifier = Modifier,
  viewModel: RoutineScreenViewModel = hiltViewModel(),
  openDialog: Boolean,
  clickSearch: Boolean,
  onOpenDialog: (isOpen: Boolean) -> Unit,
) {
  val routines by viewModel.flowRoutines.collectAsStateWithLifecycle(Resource.Loading())
  val addRoutine by viewModel.addRoutineModel.collectAsStateWithLifecycle()
  val updateRoutine by viewModel.updateRoutineModel.collectAsStateWithLifecycle()
  val indexDay by viewModel.indexDay.collectAsStateWithLifecycle()
  val times by viewModel.getTimes().collectAsStateWithLifecycle(initialValue = emptyList())
  val timeMonth by viewModel.times.collectAsStateWithLifecycle()

  RoutineScreen(
    modifier = modifier,
    routines = routines,
    times = times,
    openDialog = openDialog,
    clickSearch = clickSearch,
    onOpenDialog = onOpenDialog,
    timeMonth = timeMonth,
    currentMonth = viewModel.currentMonth,
    currentYer = viewModel.currentYear,
    currentDay = viewModel.currentDay,
    dayIndex = indexDay,
    onUpdateRoutine = viewModel::updateRoutine,
    onAddRoutine = viewModel::addRoutine,
    onDeleteRoutine = viewModel::deleteRoutine,
    onSearchText = viewModel::searchItems,
    checkedRoutine = viewModel::checkedRoutine,
    showSampleRoutine = viewModel::showSampleRoutine,
    addRoutineModel = addRoutine,
    updateRoutineModel = updateRoutine,
    clearUpdateRoutine = viewModel::clearUpdateRoutine,
    clearAddRoutine = viewModel::clearAddRoutine,
    onDayIndex = viewModel::setDayIndex,
    onCheckedDay = viewModel::getRoutines,
    onMonthChecked = viewModel::getTimesMonth,
  )
}

@Composable
private fun RoutineScreen(
  modifier: Modifier,
  routines: Resource<List<RoutineModel>>,
  times: List<TimeDate>,
  timeMonth: List<TimeDate>,
  updateRoutineModel: Resource<Nothing?>?,
  addRoutineModel: Resource<Nothing?>?,
  currentMonth: Int,
  currentYer: Int,
  currentDay: Int,
  dayIndex: Int,
  openDialog: Boolean,
  onOpenDialog: (isOpen: Boolean) -> Unit,
  clickSearch: Boolean,
  checkedRoutine: (RoutineModel) -> Unit,
  onCheckedDay: (year: Int, month: Int, day: Int) -> Unit,
  onUpdateRoutine: (RoutineModel) -> Unit,
  onAddRoutine: (RoutineModel) -> Unit,
  onDeleteRoutine: (RoutineModel) -> Unit,
  showSampleRoutine: (Boolean) -> Unit,
  clearUpdateRoutine: () -> Unit,
  clearAddRoutine: () -> Unit,
  onSearchText: (String) -> Unit,
  onDayIndex: (Int) -> Unit,
  onMonthChecked: (Int, Int) -> Unit,
) {
  val context = LocalContext.current
  Timber.tag("routineGetNameDay").d("recomposition RoutineScreen")
  val routineModelDeleteDialog = rememberSaveable { mutableStateOf<RoutineModel?>(null) }
  val routineModelUpdateDialog = rememberSaveable { mutableStateOf<RoutineModel?>(null) }
  var errorClick by rememberSaveable { mutableStateOf(false) }
  var dayChecked by rememberSaveable { mutableIntStateOf(currentDay) }
  var monthChecked by rememberSaveable { mutableIntStateOf(currentMonth) }
  var yerChecked by rememberSaveable { mutableIntStateOf(currentYer) }

  var searchText by rememberSaveable { mutableStateOf("") }

  val listStateDay = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  val currentDayIndex by remember {
    derivedStateOf {
      listStateDay.firstVisibleItemIndex
    }
  }

  val configuration = LocalConfiguration.current
  val screenWidth = configuration.screenWidthDp

  coroutineScope.launch {
    if (dayIndex >= 0) {
      listStateDay.scrollToItem(dayIndex)
    }
  }
  Column(
    modifier = modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    ShowSearchBar(clickSearch, searchText = searchText) { search ->
      searchText = search
      onSearchText(searchText)
    }
    ItemTimeDate(
      times,
      dayChecked,
      yerChecked,
      monthChecked,
      listStateDay = listStateDay,
      indexDay = dayIndex,
      dayCheckedNumber = { day, year, month ->
        yerChecked = year
        dayChecked = day
        monthChecked = month
        onCheckedDay(year, month, day)
        onMonthChecked(year, month)
      },
      currentIndexDay = currentDayIndex,
      indexScrollDay = {
        if (it != times.size) {
          onDayIndex(it)
          coroutineScope.launch {
            listStateDay.animateScrollToItem(dayIndex)
          }
        }
      },
      screenWidth = screenWidth,
    )
    GetRoutines(
      routines,
      searchText,
      routineUpdateDialog = {
        if (it.isChecked) {
          Toast.makeText(
            context,
            R.string.not_update_checked_routine,
            Toast.LENGTH_SHORT,
          ).show()
          return@GetRoutines
        }
        onOpenDialog(true)
        if (it.isSample)
          showSampleRoutine(true)
        routineModelUpdateDialog.value = it
      },
      routineChecked = {
        checkedRoutine(it)
        Timber.tag("routineGetNameDay").d("GetRoutines routineChecked->$it")
      },
      routineDeleteDialog = {
        if (it.isChecked) {
          Toast.makeText(
            context,
            com.rahim.yadino.feature.routine.R.string.not_removed_checked_routine,
            Toast.LENGTH_SHORT,
          ).show()
          return@GetRoutines
        }
        if (it.isSample)
          showSampleRoutine(true)

        routineModelDeleteDialog.value = it
      },
    )
  }

  ErrorDialog(
    isOpen = routineModelDeleteDialog.value != null,
    isClickOk = {
      if (it) {
        routineModelDeleteDialog.value?.let {
          onDeleteRoutine(it)
        }
      }
      routineModelDeleteDialog.value = null
    },
    message = stringResource(id = R.string.can_you_delete),
    okMessage = stringResource(
      id = R.string.ok,
    ),
  )
  DialogAddRoutine(
    isShowDay = false,
    isOpen = openDialog,
    openDialog = {
      onOpenDialog(false)
    },
    updateRoutineName = routineModelUpdateDialog.value?.name ?: "",
    updateRoutineExplanation = routineModelUpdateDialog.value?.explanation ?: "",
    updateRoutineTime = routineModelUpdateDialog.value?.timeHours ?: "",
    updateRoutineDay = routineModelUpdateDialog.value?.dayNumber,
    updateRoutineMonth = routineModelUpdateDialog.value?.monthNumber,
    updateRoutineYear = routineModelUpdateDialog.value?.dayNumber,
    routineItems = { routineName, routineExplanation, routineTime, dayChecked, monthChecked, yearChecked, dayName ->
      showSampleRoutine(true)
      if (routineModelUpdateDialog.value != null) {
        routineModelUpdateDialog.value?.apply {
          name = routineName
          explanation = routineExplanation
          timeHours = routineTime
          dayNumber = dayChecked
          monthNumber = monthChecked
          yerNumber = yearChecked
          this.dayName = dayName
        }
        routineModelUpdateDialog.value?.let(onUpdateRoutine)
      } else {
        val routineModel = RoutineModel(
          name = routineName,
          colorTask = null,
          dayName = dayName,
          dayNumber = dayChecked,
          monthNumber = monthChecked,
          yerNumber = yearChecked,
          timeHours = routineTime,
          explanation = routineExplanation,
        )
        onAddRoutine(routineModel)
      }
    },
    currentNumberDay = dayChecked,
    currentNumberMonth = monthChecked,
    currentNumberYear = yerChecked,
    times = timeMonth,
    monthChange = onMonthChecked,
  )
  ProcessRoutineAdded(addRoutineModel, context) {
    onOpenDialog(false)
    clearAddRoutine()
  }
  ProcessRoutineAdded(updateRoutineModel, context) {
    onOpenDialog(false)
    clearUpdateRoutine()
    routineModelUpdateDialog.value = null
  }
  ErrorDialog(
    isOpen = errorClick,
    message = stringResource(id = R.string.better_performance_access),
    okMessage = stringResource(id = R.string.setting),
    isClickOk = {
      if (it) {
        goSettingPermission(context)
      }
      errorClick = false
    },
  )
}


private fun calculateCurrentIndexDay(currentIndex: Int, previousIndex: Int): Int {
  return if (currentIndex > previousIndex) {
    previousIndex + 7
  } else {
    if (previousIndex - 7 < 0) {
      previousIndex
    } else {
      previousIndex - 7
    }
  }
}

private fun calculateIndexDay(index: Int): Int {
  var indexPosition = 0
  while (true) {
    indexPosition += 7
    if (indexPosition > index) {
      indexPosition -= 7
      break
    }
  }
  return indexPosition
}

@Composable
private fun GetRoutines(
  routines: Resource<List<RoutineModel>>,
  searchText: String,
  routineUpdateDialog: (RoutineModel) -> Unit,
  routineChecked: (RoutineModel) -> Unit,
  routineDeleteDialog: (RoutineModel) -> Unit,
) {
  when (routines) {
    is Resource.Loading -> {}
    is Resource.Success -> {
      routines.data?.let {
        if (it.isEmpty()) {
          if (searchText.isNotEmpty()) {
            EmptyMessage(
              messageEmpty = R.string.search_empty_routine,
              painter = com.rahim.yadino.feature.routine.R.drawable.routine_empty,
            )
          } else {
            EmptyMessage(
              messageEmpty = com.rahim.yadino.feature.routine.R.string.not_routine,
              painter = com.rahim.yadino.feature.routine.R.drawable.routine_empty,
            )
          }
        } else {
          ItemsRoutine(
            it,
            checkedRoutine = {
              Timber.tag("routineGetNameDay")
                .d("ItemsRoutine checkedRoutine->$it")
              routineChecked(it)
            },
            updateRoutine = {
              routineUpdateDialog(it)
            },
            deleteRoutine = {
              routineDeleteDialog(it)
            },
          )
        }
      }
    }

    is Resource.Error -> {}
  }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun ItemTimeDate(
  times: List<TimeDate>,
  dayChecked: Int,
  yerChecked: Int,
  monthChecked: Int,
  listStateDay: LazyListState,
  indexDay: Int,
  currentIndexDay: Int,
  screenWidth: Int,
  dayCheckedNumber: (day: Int, yer: Int, month: Int) -> Unit,
  indexScrollDay: (Int) -> Unit,
) {
  Row(
    modifier = Modifier.padding(top = 28.dp),
  ) {
    IconButton(
      onClick = {
        var month = monthChecked.plus(1)
        var year = yerChecked
        if (month > 12) {
          month = 1
          year = yerChecked.plus(1)
        }
        val time =
          times.find { it.monthNumber == month && it.yerNumber == year && it.dayNumber == 1 }
        if (time != null) {
          dayCheckedNumber(1, year, month)
          val index = times.indexOf(time)
          indexScrollDay(
            calculateIndexDay(index),
          )
        }
      },
    ) {
      Icon(
        tint = MaterialTheme.colorScheme.primary,
        painter = painterResource(id = R.drawable.less_then),
        contentDescription = "less then sign",
      )
    }
    Text(
      modifier = Modifier
          .padding(top = 12.dp)
          .fillMaxWidth(0.3f),
      text = "${yerChecked.toString().persianLocate()} ${monthChecked.calculateMonthName()}",
      color = MaterialTheme.colorScheme.primary,
      textAlign = TextAlign.Center,
    )
    IconButton(
      onClick = {
        var month = monthChecked.minus(1)
        var year = yerChecked
        if (month < 1) {
          month = 12
          year = yerChecked.minus(1)
        }
        val time =
          times.find { it.monthNumber == month && it.yerNumber == year && it.dayNumber == 1 }
        if (time != null) {
          dayCheckedNumber(1, year, month)
          val index = times.indexOf(time)
          indexScrollDay(
            calculateIndexDay(index),
          )
        }
      },
    ) {
      Icon(
        painterResource(id = R.drawable.greater_then),
        contentDescription = "greater then sign",
        tint = MaterialTheme.colorScheme.primary,
      )
    }
  }

  Row(
    modifier = Modifier
        .padding(top = 18.dp, end = 50.dp, start = 50.dp)
        .fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(
      modifier = Modifier.padding(start = 13.dp),
      fontSize = 14.sp,
      text = HalfWeekName.FRIDAY.nameDay,
      color = MaterialTheme.colorScheme.primary,
    )
    Text(
      fontSize = 14.sp,
      text = HalfWeekName.THURSDAY.nameDay,
      color = MaterialTheme.colorScheme.primary,
    )
    Text(
      fontSize = 14.sp,
      text = HalfWeekName.WEDNESDAY.nameDay,
      color = MaterialTheme.colorScheme.primary,
    )
    Text(
      fontSize = 14.sp,
      text = HalfWeekName.TUESDAY.nameDay,
      color = MaterialTheme.colorScheme.primary,
    )
    Text(
      fontSize = 14.sp,
      text = HalfWeekName.MONDAY.nameDay,
      color = MaterialTheme.colorScheme.primary,
    )
    Text(
      fontSize = 14.sp,
      text = HalfWeekName.SUNDAY.nameDay,
      color = MaterialTheme.colorScheme.primary,
    )
    Text(
      modifier = Modifier.padding(end = 12.dp, top = 3.dp),
      fontSize = 12.sp,
      text = HalfWeekName.SATURDAY.nameDay,
      color = MaterialTheme.colorScheme.primary,
    )
  }

  Row() {
    IconButton(
      modifier = Modifier.padding(top = 6.dp),
      onClick = {
        indexScrollDay(
          if (times.size <= indexDay + 7) {
            times.size
          } else if (indexDay == -1) {
            indexDay + 8
          } else {
            indexDay + 7
          },
        )
      },
    ) {
      Icon(
        tint = MaterialTheme.colorScheme.primary,
        painter = painterResource(id = R.drawable.less_then),
        contentDescription = "less then sign",
      )
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
      LazyRow(
        userScrollEnabled = false,
        modifier = Modifier
            .weight(1f)
            .padding(top = 6.dp),
        state = listStateDay,
        flingBehavior = rememberSnapperFlingBehavior(
          lazyListState = listStateDay,
          snapIndex = { _, _, _ ->
            indexScrollDay(calculateCurrentIndexDay(currentIndexDay, indexDay))
            indexDay
          },
        ),
      ) {
        items(
          items = times,
          itemContent = {
            DayItems(
              it,
              dayChecked,
              yerChecked,
              monthChecked,
              screenWidth = screenWidth,
              dayCheckedNumber = { day, yer, month ->
                dayCheckedNumber(day, yer, month)
              },
            )
          },
        )
      }
    }
    IconButton(
      modifier = Modifier.padding(top = 6.dp),
      onClick = {
        indexScrollDay(
          if (indexDay <= 6) {
            0
          } else {
            indexDay - 7
          },
        )
      },
    ) {
      Icon(
        painterResource(id = R.drawable.greater_then),
        contentDescription = "greater then sign",
        tint = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

@Composable
private fun ItemsRoutine(
  routineModels: List<RoutineModel>,
  checkedRoutine: (RoutineModel) -> Unit,
  updateRoutine: (RoutineModel) -> Unit,
  deleteRoutine: (RoutineModel) -> Unit,
) {

  LazyColumn(
    modifier = Modifier
        .fillMaxWidth()
        .padding(end = 16.dp, start = 16.dp),
    contentPadding = PaddingValues(top = 8.dp),
  ) {
    items(
      items = routineModels,
      itemContent = { routine ->
        ItemRoutine(
          isChecked = routine.isChecked,
          timeHoursRoutine = routine.timeHours ?: "",
          nameRoutine = routine.name,
          explanationRoutine = routine.explanation ?: "",
          onChecked = {
            checkedRoutine(routine.apply { isChecked = it })
          },
          openDialogDelete = {
            deleteRoutine(routine)
          },
          openDialogEdit = {
            updateRoutine(routine)
          },
        )
      },
    )
  }
}

@Composable
private fun DayItems(
  timeDate: TimeDate,
  dayChecked: Int,
  dayYerChecked: Int,
  dayMonthChecked: Int,
  screenWidth: Int,
  dayCheckedNumber: (day: Int, yer: Int, month: Int) -> Unit,
) {

  Timber.tag("timeClicked").d("dayChecked->$dayChecked")
  Timber.tag("timeClicked").d("dayYerChecked->$dayYerChecked")
  Timber.tag("timeClicked").d("dayMonthChecked->$dayMonthChecked")
  Timber.tag("timeClicked").d("timeData->$timeDate")
  ClickableText(
    modifier = Modifier
        .padding(
            top = 4.dp,
            start = if (dayChecked == 1) if (screenWidth <= 420) 5.dp else 7.dp else 6.dp,
        )
        .size(if (screenWidth <= 400) 36.dp else if (screenWidth in 400..420) 39.dp else 43.dp)
        .clip(CircleShape)
        .background(
            brush = if (dayChecked == timeDate.dayNumber && dayMonthChecked == timeDate.monthNumber && dayYerChecked == timeDate.yerNumber) {
                Brush.verticalGradient(
                    gradientColors,
                )
            } else Brush.horizontalGradient(
                listOf(
                    MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.background,
                ),
            ),
        )
        .padding(
            top = if (screenWidth <= 400) 8.dp else if (screenWidth in 400..420) 9.dp else 10.dp,
        ),
    onClick = {
      if (timeDate.dayNumber > 0)
        dayCheckedNumber(
          timeDate.dayNumber,
          timeDate.yerNumber,
          timeDate.monthNumber,
        )
    },
    text = AnnotatedString(
      if (timeDate.dayNumber > 0) timeDate.dayNumber.toString().persianLocate() else "",
    ),
    style = TextStyle(
      fontSize = if (screenWidth <= 420) 16.sp else 18.sp,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      fontFamily = font_medium,
      color = if (dayChecked == timeDate.dayNumber && dayMonthChecked == timeDate.monthNumber && dayYerChecked == timeDate.yerNumber) (Color.White) else MaterialTheme.colorScheme.primary,
    ),
  )
}
