package com.yadino.routine.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahim.yadino.base.use
import com.rahim.yadino.calculateMonthName
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.component.ShowToastShort
import com.rahim.yadino.designsystem.component.goSettingPermission
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.theme.font_medium
import com.rahim.yadino.enums.RoutineExplanation
import com.rahim.yadino.errorMessage
import com.rahim.yadino.persianLocate
import com.rahim.yadino.routine.presentation.R
import com.yadino.routine.domain.model.RoutineModelDomainLayer
import com.yadino.routine.presentation.component.DialogAddRoutine
import com.yadino.routine.presentation.component.ListRoutines
import com.yadino.routine.presentation.model.TimeDateRoutinePresentationLayer
import timber.log.Timber

@Composable
fun RoutineRoute(
  modifier: Modifier = Modifier,
  viewModel: RoutineScreenViewModel = hiltViewModel(),
  openDialog: Boolean,
  clickSearch: Boolean,
  onOpenDialog: (isOpen: Boolean) -> Unit,
) {
  val (state, event) = use(viewModel)

  RoutineScreen(
    modifier = modifier,
    state = state,
    openDialog = openDialog,
    clickSearch = clickSearch,
    onOpenDialog = onOpenDialog,
    onUpdateRoutine = {
      event.invoke(RoutineContract.RoutineEvent.UpdateRoutine(it))
    },
    onAddRoutine = {
      event(RoutineContract.RoutineEvent.AddRoutine(it))
    },
    onDeleteRoutine = {
      event.invoke(RoutineContract.RoutineEvent.DeleteRoutine(it))
    },
    onSearchText = {
      event.invoke(RoutineContract.RoutineEvent.SearchRoutine(it))
    },
    checkedRoutine = {
      event.invoke(RoutineContract.RoutineEvent.CheckedRoutine(it))
    },
    dayCheckedNumber = { timeDate ->
      event.invoke(RoutineContract.RoutineEvent.GetRoutines(timeDate))
    },
    dialogMonthIncrease = { year, month ->
      event.invoke(RoutineContract.RoutineEvent.JustMonthIncrease(year, month))
    },
    dialogMonthDecrease = { year, month ->
      event.invoke(RoutineContract.RoutineEvent.JustMonthDecrease(year, month))
    },
    monthIncrease = { year, month ->
      event.invoke(RoutineContract.RoutineEvent.MonthIncrease(year, month))
    },
    monthDecrease = { year, month ->
      event.invoke(RoutineContract.RoutineEvent.MonthDecrease(year, month))
    },
    weekIncrease = {
      event.invoke(RoutineContract.RoutineEvent.WeekIncrease)
    },
    weekDecrease = {
      event.invoke(RoutineContract.RoutineEvent.WeekDecrease)
    },
  )
}

@Composable
private fun RoutineScreen(
  modifier: Modifier,
  state: RoutineContract.RoutineState,
  openDialog: Boolean,
  onOpenDialog: (isOpen: Boolean) -> Unit,
  clickSearch: Boolean,
  checkedRoutine: (RoutineModelDomainLayer) -> Unit,
  dayCheckedNumber: (timeDate: TimeDateRoutinePresentationLayer) -> Unit,
  onUpdateRoutine: (RoutineModelDomainLayer) -> Unit,
  onAddRoutine: (RoutineModelDomainLayer) -> Unit,
  onDeleteRoutine: (RoutineModelDomainLayer) -> Unit,
  onSearchText: (String) -> Unit,
  monthIncrease: (year: Int, month: Int) -> Unit,
  monthDecrease: (year: Int, month: Int) -> Unit,
  dialogMonthIncrease: (year: Int, month: Int) -> Unit,
  dialogMonthDecrease: (year: Int, month: Int) -> Unit,
  weekIncrease: () -> Unit,
  weekDecrease: () -> Unit,
) {
  val context = LocalContext.current

  Timber.tag("routineGetNameDay").d("recomposition RoutineScreen")
  val routineModelDomainLayerDeleteDialog = rememberSaveable { mutableStateOf<RoutineModelDomainLayer?>(null) }
  val routineModelDomainLayerUpdateDialog = rememberSaveable { mutableStateOf<RoutineModelDomainLayer?>(null) }
  var errorClick by rememberSaveable { mutableStateOf(false) }

  var searchText by rememberSaveable { mutableStateOf("") }

  state.errorMessage?.let { errorMessage ->
    ShowToastShort(errorMessage.errorMessage(), context)
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
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    ItemTimeDate(
      times = state.times,
      yearChecked = state.currentYear,
      monthChecked = state.currentMonth,
      indexDay = state.index,
      dayCheckedNumber = dayCheckedNumber,
      screenWidth = screenWidth,
      monthIncrease = {
        monthIncrease(state.currentYear, state.currentMonth)
      },
      monthDecrease = {
        monthDecrease(state.currentYear, state.currentMonth)
      },
      weekDecrease = weekDecrease,
      weekIncrease = weekIncrease,
    )
    GetRoutines(
      if (searchText.isNotBlank()) state.searchRoutines else state.routines,
      searchText,
      routineUpdateDialog = {
        if (it.isChecked) {
          Toast.makeText(
            context,
            com.rahim.yadino.library.designsystem.R.string.not_update_checked_routine,
            Toast.LENGTH_SHORT,
          ).show()
          return@GetRoutines
        }
        onOpenDialog(true)
        routineModelDomainLayerUpdateDialog.value = it
      },
      routineChecked = {
        checkedRoutine(it)
        Timber.tag("routineGetNameDay").d("GetRoutines routineChecked->$it")
      },
      routineDeleteDialog = {
        if (it.isChecked) {
          Toast.makeText(
            context,
            R.string.not_removed_checked_routine,
            Toast.LENGTH_SHORT,
          ).show()
          return@GetRoutines
        }
        routineModelDomainLayerDeleteDialog.value = it
      },
    )
  }

  ErrorDialog(
    isOpen = routineModelDomainLayerDeleteDialog.value != null,
    isClickOk = {
      if (it) {
        routineModelDomainLayerDeleteDialog.value?.let {
          onDeleteRoutine(it)
        }
      }
      routineModelDomainLayerDeleteDialog.value = null
    },
    message = stringResource(id = com.rahim.yadino.library.designsystem.R.string.can_you_delete),
    okMessage = stringResource(
      id = com.rahim.yadino.library.designsystem.R.string.ok,
    ),
  )
  if (openDialog) {
    DialogAddRoutine(
      onCloseDialog = {
        onOpenDialog(false)
        routineModelDomainLayerUpdateDialog.value = null
      },
      updateRoutine = routineModelDomainLayerUpdateDialog.value?.copy(
        explanation = routineModelDomainLayerUpdateDialog.value?.explanation?.let {
          if (it == RoutineExplanation.ROUTINE_RIGHT_SAMPLE.explanation) {
            stringResource(id = com.rahim.yadino.library.designsystem.R.string.routine_right_sample)
          } else if (it == RoutineExplanation.ROUTINE_LEFT_SAMPLE.explanation) {
            stringResource(id = com.rahim.yadino.library.designsystem.R.string.routine_left_sample)
          } else {
            it
          }
        } ?: run {
          routineModelDomainLayerUpdateDialog.value?.explanation ?: ""
        },
      ),
      onRoutineCreated = { routine ->
        if (routineModelDomainLayerUpdateDialog.value != null) {
          onUpdateRoutine(routine)
        } else {
          onAddRoutine(routine)
        }
        onOpenDialog(false)
      },
      currentNumberDay = state.currentDay,
      currentNumberMonth = state.currentMonth,
      currentNumberYear = state.currentYear,
      timesMonth = state.timesMonth,
      monthDecrease = dialogMonthDecrease,
      monthIncrease = dialogMonthIncrease,
    )
  }

  ErrorDialog(
    isOpen = errorClick,
    message = stringResource(id = com.rahim.yadino.library.designsystem.R.string.better_performance_access),
    okMessage = stringResource(id = com.rahim.yadino.library.designsystem.R.string.setting),
    isClickOk = {
      if (it) {
        goSettingPermission(context)
      }
      errorClick = false
    },
  )
}

@Composable
private fun GetRoutines(
  routines: List<RoutineModelDomainLayer>,
  searchText: String,
  routineUpdateDialog: (RoutineModelDomainLayer) -> Unit,
  routineChecked: (RoutineModelDomainLayer) -> Unit,
  routineDeleteDialog: (RoutineModelDomainLayer) -> Unit,
) {
  if (routines.isEmpty()) {
    if (searchText.isNotEmpty()) {
      EmptyMessage(
        messageEmpty = com.rahim.yadino.library.designsystem.R.string.search_empty_routine,
        painter = R.drawable.routine_empty,
      )
    } else {
      EmptyMessage(
        messageEmpty = R.string.not_routine,
        painter = R.drawable.routine_empty,
      )
    }
  } else {
    ListRoutines(
      modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp),
      routines = routines,
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

@Composable
private fun ItemTimeDate(
  times: List<TimeDateRoutinePresentationLayer>,
  yearChecked: Int,
  monthChecked: Int,
  indexDay: Int,
  screenWidth: Int,
  dayCheckedNumber: (timeDate: TimeDateRoutinePresentationLayer) -> Unit,
  monthIncrease: () -> Unit,
  monthDecrease: () -> Unit,
  weekIncrease: () -> Unit,
  weekDecrease: () -> Unit,
) {
  val arrayString = stringArrayResource(id = com.rahim.yadino.library.designsystem.R.array.half_week_name)

  Row(
    modifier = Modifier.padding(top = 28.dp),
  ) {
    IconButton(
      onClick = {
        monthIncrease()
      },
    ) {
      Icon(
        tint = MaterialTheme.colorScheme.primary,
        painter = painterResource(id = com.rahim.yadino.library.designsystem.R.drawable.less_then),
        contentDescription = "less then sign",
      )
    }
    Text(
      modifier = Modifier
          .padding(top = 12.dp)
          .fillMaxWidth(0.3f),
      text = "${yearChecked.toString().persianLocate()} ${monthChecked.calculateMonthName()}",
      color = MaterialTheme.colorScheme.primary,
      textAlign = TextAlign.Center,
    )
    IconButton(
      onClick = {
        monthDecrease()
      },
    ) {
      Icon(
        painterResource(id = com.rahim.yadino.library.designsystem.R.drawable.greater_then),
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
    arrayString.reversed().forEachIndexed { index, nameDay ->
      Text(
        modifier = Modifier.padding(
          start = if (index == 0) 13.dp else 0.dp,
          end = if (index == 7) 12.dp else 0.dp,
          top = if (index == 7) 3.dp else 0.dp,
        ),
        fontSize = 14.sp,
        text = nameDay,
        color = MaterialTheme.colorScheme.primary,
      )
    }
  }

  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
    IconButton(
      modifier = Modifier
          .weight(1f)
          .padding(top = 10.dp),
      onClick = {
        weekIncrease()
      },
    ) {
      Icon(
        tint = MaterialTheme.colorScheme.primary,
        painter = painterResource(id = com.rahim.yadino.library.designsystem.R.drawable.less_then),
        contentDescription = "less then sign",
      )
    }
    ListTimes(
      modifier = Modifier
          .weight(8f)
          .padding(top = 6.dp),
      times = times,
      screenWidth = screenWidth,
      dayCheckedNumber = dayCheckedNumber,
      indexDay = indexDay,
    )
    IconButton(
      modifier = Modifier
          .weight(1f)
          .padding(top = 10.dp),
      onClick = {
        weekDecrease()
      },
    ) {
      Icon(
        painterResource(id = com.rahim.yadino.library.designsystem.R.drawable.greater_then),
        contentDescription = "greater then sign",
        tint = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

@Composable
fun ListTimes(
  modifier: Modifier = Modifier,
  times: List<TimeDateRoutinePresentationLayer>,
  screenWidth: Int,
  dayCheckedNumber: (timeDate: TimeDateRoutinePresentationLayer) -> Unit,
  indexDay: Int,
) {
  val listStateDay = rememberLazyListState()
  val flingBehavior = rememberSnapFlingBehavior(lazyListState = listStateDay)

  LaunchedEffect(indexDay) {
    if (indexDay > 0) {
      listStateDay.scrollToItem(indexDay)
    }
  }

  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    LazyRow(
      userScrollEnabled = false,
      modifier = modifier,
      state = listStateDay,
      flingBehavior = flingBehavior,
    ) {
      items(
        items = times,
        itemContent = {
          DayItems(
            it,
            screenWidth = screenWidth,
            dayCheckedNumber = { timeDate ->
              dayCheckedNumber(timeDate)
            },
          )
        },
      )
    }
  }
}

@Composable
private fun DayItems(
  timeDate: TimeDateRoutinePresentationLayer,
  screenWidth: Int,
  dayCheckedNumber: (timeDate: TimeDateRoutinePresentationLayer) -> Unit,
) {
  ClickableText(
    modifier = Modifier
        .padding(
            top = 4.dp,
            start = if (timeDate.isChecked) if (screenWidth <= 420) 5.dp else 7.dp else 6.dp,
        )
        .size(if (screenWidth <= 400) 36.dp else if (screenWidth in 400..420) 39.dp else 43.dp)
        .clip(CircleShape)
        .background(
            brush = if (timeDate.isChecked) {
                Brush.verticalGradient(
                    gradientColors,
                )
            } else {
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                    ),
                )
            },
        )
        .padding(
            top = if (screenWidth <= 400) 8.dp else if (screenWidth in 400..420) 9.dp else 10.dp,
        ),
    onClick = {
      if (timeDate.dayNumber > 0) {
        dayCheckedNumber(
          timeDate,
        )
      }
    },
    text = AnnotatedString(
      if (timeDate.dayNumber > 0) timeDate.dayNumber.toString().persianLocate() else "",
    ),
    style = TextStyle(
      fontSize = if (screenWidth <= 420) 16.sp else 18.sp,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      fontFamily = font_medium,
      color = if (timeDate.isChecked) (Color.White) else MaterialTheme.colorScheme.primary,
    ),
  )
}
