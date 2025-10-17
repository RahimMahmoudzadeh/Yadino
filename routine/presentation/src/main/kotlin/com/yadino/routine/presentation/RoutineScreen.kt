package com.yadino.routine.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rahim.yadino.base.LoadableComponent
import com.rahim.yadino.base.use
import com.rahim.yadino.calculateMonthName
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.utils.theme.font_medium
import com.rahim.yadino.enums.RoutineExplanation
import com.rahim.yadino.toStringResource
import com.rahim.yadino.persianLocate
import com.rahim.yadino.routine.presentation.R
import com.rahim.yadino.showToastShort
import com.yadino.routine.presentation.component.DialogAddRoutine
import com.yadino.routine.presentation.component.ListRoutines
import com.yadino.routine.presentation.model.IncreaseDecrease
import com.yadino.routine.presentation.model.RoutineUiModel
import com.yadino.routine.presentation.model.TimeDateUiModel
import kotlinx.collections.immutable.PersistentList
import timber.log.Timber

@Composable
fun RoutineRoute(
  modifier: Modifier = Modifier,
  viewModel: RoutineScreenViewModel = hiltViewModel(),
  openDialogAddRoutine: Boolean,
  showSearchBar: Boolean,
  onOpenDialogAddRoutine: (isOpen: Boolean) -> Unit,
) {
  val (state, event) = use(viewModel)

  RoutineScreen(
    modifier = modifier,
    state = state,
    openDialogAddRoutine = openDialogAddRoutine,
    showSearchBar = showSearchBar,
    onOpenDialogAddRoutine = onOpenDialogAddRoutine,
    onUpdateRoutine = {
      event.invoke(RoutineContract.Event.UpdateRoutine(it))
    },
    onAddRoutine = {
      event(RoutineContract.Event.AddRoutine(it))
    },
    onDeleteRoutine = {
      event.invoke(RoutineContract.Event.DeleteRoutine(it))
    },
    onSearchText = {
      event.invoke(RoutineContract.Event.SearchRoutineByName(it))
    },
    checkedRoutine = {
      event.invoke(RoutineContract.Event.CheckedRoutine(it))
    },
    dayCheckedNumber = { timeDate ->
      event.invoke(RoutineContract.Event.GetRoutines(timeDate))
    },
    dialogMonthChange = { year, month, increaseDecrease ->
      event.invoke(RoutineContract.Event.DialogMonthChange(yearNumber = year, monthNumber = month, increaseDecrease = increaseDecrease))
    },
    monthChange = { year, month, increaseDecrease ->
      event.invoke(RoutineContract.Event.MonthChange(yearNumber = year, monthNumber = month, increaseDecrease = increaseDecrease))
    },
    weekChange = { increaseDecrease ->
      event.invoke(RoutineContract.Event.WeekChange(increaseDecrease = increaseDecrease))
    },
  )
}

@Composable
private fun RoutineScreen(
    modifier: Modifier,
    state: RoutineContract.State,
    openDialogAddRoutine: Boolean,
    onOpenDialogAddRoutine: (isOpen: Boolean) -> Unit,
    showSearchBar: Boolean,
    checkedRoutine: (RoutineUiModel) -> Unit,
    dayCheckedNumber: (timeDate: TimeDateUiModel) -> Unit,
    onUpdateRoutine: (RoutineUiModel) -> Unit,
    onAddRoutine: (RoutineUiModel) -> Unit,
    onDeleteRoutine: (RoutineUiModel) -> Unit,
    onSearchText: (String) -> Unit,
    monthChange: (year: Int, month: Int, increaseDecrease: IncreaseDecrease) -> Unit,
    dialogMonthChange: (year: Int, month: Int, increaseDecrease: IncreaseDecrease) -> Unit,
    weekChange: (IncreaseDecrease) -> Unit,
) {
  val context = LocalContext.current

  val routineDeleteDialog = rememberSaveable { mutableStateOf<RoutineUiModel?>(null) }
  val routineUpdateDialog = rememberSaveable { mutableStateOf<RoutineUiModel?>(null) }

  var errorClick by rememberSaveable { mutableStateOf(false) }
  var searchText by rememberSaveable { mutableStateOf("") }

  LaunchedEffect(state.errorMessageCode) {
    state.errorMessageCode?.let { errorMessageCode ->
      context.showToastShort(stringId = errorMessageCode.toStringResource())
    }
  }
  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    ShowSearchBar(showSearchBar, searchText = searchText) { search ->
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
      monthChange = { increaseDecrease ->
        monthChange(state.currentYear, state.currentMonth, increaseDecrease)
      },
      weekChange = weekChange,
    )
    GetRoutines(
      state = state,
      searchText = searchText,
      routineUpdateDialog = {
        if (it.isChecked) {
          Toast.makeText(
            context,
            com.rahim.yadino.library.designsystem.R.string.not_update_checked_routine,
            Toast.LENGTH_SHORT,
          ).show()
          return@GetRoutines
        }
        onOpenDialogAddRoutine(true)
        routineUpdateDialog.value = it
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
        routineDeleteDialog.value = it
      },
    )
  }
  when{
    routineDeleteDialog.value != null ->{
      ErrorDialog(
        isClickOk = {
          if (it) {
            routineDeleteDialog.value?.let {
              onDeleteRoutine(it)
            }
          }
          routineDeleteDialog.value = null
        },
        message = stringResource(id = com.rahim.yadino.library.designsystem.R.string.can_you_delete),
        okMessage = stringResource(
          id = com.rahim.yadino.library.designsystem.R.string.ok,
        ),
      )
    }
    openDialogAddRoutine ->{
      DialogAddRoutine(
        onCloseDialog = {
          onOpenDialogAddRoutine(false)
          routineUpdateDialog.value = null
        },
        updateRoutine = routineUpdateDialog.value?.copy(
          explanation = routineUpdateDialog.value?.explanation?.let {
            if (it == RoutineExplanation.ROUTINE_RIGHT_SAMPLE.explanation) {
              stringResource(id = com.rahim.yadino.library.designsystem.R.string.routine_right_sample)
            } else if (it == RoutineExplanation.ROUTINE_LEFT_SAMPLE.explanation) {
              stringResource(id = com.rahim.yadino.library.designsystem.R.string.routine_left_sample)
            } else {
              it
            }
          } ?: run {
            routineUpdateDialog.value?.explanation ?: ""
          },
        ),
        onRoutineCreated = { routine ->
          if (routineUpdateDialog.value != null) {
            onUpdateRoutine(routine)
          } else {
            onAddRoutine(routine)
          }
          onOpenDialogAddRoutine(false)
        },
        currentNumberDay = state.currentDay,
        currentNumberMonth = state.currentMonth,
        currentNumberYear = state.currentYear,
        timesMonth = state.timesMonth,
        monthDecrease = { year, month ->
          dialogMonthChange(year, month, IncreaseDecrease.DECREASE)
        },
        monthIncrease = { year, month ->
          dialogMonthChange(year, month, IncreaseDecrease.INCREASE)
        },
      )
    }
  }
}

@Composable
private fun GetRoutines(
  state: RoutineContract.State,
  searchText: String,
  routineUpdateDialog: (RoutineUiModel) -> Unit,
  routineChecked: (RoutineUiModel) -> Unit,
  routineDeleteDialog: (RoutineUiModel) -> Unit,
) {
  val context = LocalContext.current
  LoadableComponent(
    loadableData = state.routines,
    loaded = { routines ->
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

    },
    loading = {},
    error = { errorCode ->
      context.showToastShort(stringId = errorCode.toStringResource())
    },
  )
}

@Composable
private fun ItemTimeDate(
    times: PersistentList<TimeDateUiModel>,
    yearChecked: Int,
    monthChecked: Int,
    indexDay: Int,
    screenWidth: Int,
    dayCheckedNumber: (timeDate: TimeDateUiModel) -> Unit,
    monthChange: (IncreaseDecrease) -> Unit,
    weekChange: (IncreaseDecrease) -> Unit,
) {
  val arrayString = stringArrayResource(id = com.rahim.yadino.library.designsystem.R.array.half_week_name)

  Row(
    modifier = Modifier.padding(top = 28.dp),
  ) {
    IconButton(
      onClick = {
        monthChange(IncreaseDecrease.INCREASE)
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
        monthChange(IncreaseDecrease.DECREASE)
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
        weekChange(IncreaseDecrease.INCREASE)
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
        weekChange(IncreaseDecrease.DECREASE)
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
    times: PersistentList<TimeDateUiModel>,
    screenWidth: Int,
    dayCheckedNumber: (timeDate: TimeDateUiModel) -> Unit,
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
    timeDate: TimeDateUiModel,
    screenWidth: Int,
    dayCheckedNumber: (timeDate: TimeDateUiModel) -> Unit,
) {
  Text(
    modifier = Modifier
      .padding(
        top = 4.dp,
        start = if (timeDate.isChecked) if (screenWidth <= 420) 5.dp else 7.dp else 6.dp,
      )
      .clickable(
        onClick = {
          if (timeDate.dayNumber > 0) {
            dayCheckedNumber(
              timeDate,
            )
          }
        },
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
