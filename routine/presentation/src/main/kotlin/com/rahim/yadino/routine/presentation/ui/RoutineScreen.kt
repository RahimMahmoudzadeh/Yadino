package com.rahim.yadino.routine.presentation.ui

import android.content.Context
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.Child
import com.rahim.yadino.base.LoadableComponent
import com.rahim.yadino.base.use
import com.rahim.yadino.calculateMonthName
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.utils.size.FontDimensions
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.size.SizeDimensions
import com.rahim.yadino.designsystem.utils.size.SpaceDimensions
import com.rahim.yadino.designsystem.utils.theme.font_medium
import com.rahim.yadino.routine.presentation.R
import com.rahim.yadino.routine.presentation.component.RoutineComponent
import com.rahim.yadino.routine.presentation.component.addRoutineDialog.AddRoutineDialogComponent
import com.rahim.yadino.routine.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.routine.presentation.model.IncreaseDecrease
import com.rahim.yadino.routine.presentation.model.RoutineUiModel
import com.rahim.yadino.routine.presentation.model.TimeDateUiModel
import com.rahim.yadino.routine.presentation.ui.addRoutineDialog.AddRoutineDialog
import com.rahim.yadino.routine.presentation.ui.component.ListRoutines
import com.rahim.yadino.toPersianDigits
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

@Composable
fun RoutineRoute(
  modifier: Modifier = Modifier,
  showSearchBar: Boolean,
  component: RoutineComponent,
  dialogSlot: Child.Created<Any, AddRoutineDialogComponent>?,
) {
  val (state, _, event) = use(component)

  dialogSlot?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      AddRoutineDialog(
        componentComponent = dialogComponent,
      )
    }
  }
  RoutineScreen(
    modifier = modifier,
    state = state,
    showSearchBar = showSearchBar,
    onShowUpdateDialog = {
      event.invoke(RoutineComponent.Event.OnShowUpdateDialog(it))
    },
    onShowErrorDialog = {
      event.invoke(RoutineComponent.Event.OnShowErrorDialog(it))
    },
    onSearchText = {
      event.invoke(RoutineComponent.Event.SearchRoutineByName(it))
    },
    checkedRoutine = {
      event.invoke(RoutineComponent.Event.CheckedRoutine(it))
    },
    dayCheckedNumber = { timeDate ->
      event.invoke(RoutineComponent.Event.GetRoutines(timeDate))
    },
    increaseOrDecrease = { increaseDecrease ->
      event.invoke(RoutineComponent.Event.MonthChange(increaseDecrease = increaseDecrease))
    },
    weekChange = { increaseDecrease ->
      event.invoke(RoutineComponent.Event.WeekChange(increaseDecrease = increaseDecrease))
    },
  )
}

@OptIn(FlowPreview::class)
@Composable
private fun RoutineScreen(
  modifier: Modifier,
  state: RoutineComponent.State,
  showSearchBar: Boolean,
  checkedRoutine: (RoutineUiModel) -> Unit,
  dayCheckedNumber: (timeDate: TimeDateUiModel) -> Unit,
  onShowUpdateDialog: (routine: RoutineUiModel) -> Unit,
  onShowErrorDialog: (ErrorDialogUiModel) -> Unit,
  onSearchText: (String) -> Unit,
  increaseOrDecrease: (increaseDecrease: IncreaseDecrease) -> Unit,
  weekChange: (IncreaseDecrease) -> Unit,
) {
  val context = LocalContext.current

  val fontSize = LocalFontSize.current
  val space = LocalSpacing.current
  val size = LocalSize.current

  var searchQuery by remember { mutableStateOf("") }

  LaunchedEffect(Unit) {
    snapshotFlow { searchQuery }
      .debounce(300)
      .distinctUntilChanged()
      .collect { query ->
        onSearchText(query)
      }
  }

  Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
  ) {
    ShowSearchBar(showSearchBar, searchText = searchQuery) { search ->
      searchQuery = search
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
        increaseOrDecrease(increaseDecrease)
      },
      weekChange = weekChange,
      space = space,
      size = size,
      fontSize = fontSize,
    )
    GetRoutines(
      space = space,
      fontSize = fontSize,
      size = size,
      state = state,
      context = context,
      searchText = searchQuery,
      routineUpdateDialog = { routineUpdate ->
        if (routineUpdate.isChecked) {
          Toast.makeText(
            context,
            com.rahim.yadino.library.designsystem.R.string.not_update_checked_routine,
            Toast.LENGTH_SHORT,
          ).show()
          return@GetRoutines
        }
        onShowUpdateDialog(routineUpdate)
      },
      routineChecked = {
        checkedRoutine(it)
        Timber.tag("routineGetNameDay").d("GetRoutines routineChecked->$it")
      },
      routineDeleteDialog = { deletedRoutine ->
        if (deletedRoutine.isChecked) {
          Toast.makeText(
            context,
            R.string.not_removed_checked_routine,
            Toast.LENGTH_SHORT,
          ).show()
          return@GetRoutines
        }
        onShowErrorDialog(ErrorDialogUiModel(title = context.getString(com.rahim.yadino.library.designsystem.R.string.can_you_delete), submitTextButton = context.getString(com.rahim.yadino.library.designsystem.R.string.ok), routineUiModel = deletedRoutine))
      },
    )
  }
}

@Composable
private fun GetRoutines(
  state: RoutineComponent.State,
  searchText: String,
  context: Context,
  size: SizeDimensions,
  space: SpaceDimensions,
  fontSize: FontDimensions,
  routineUpdateDialog: (RoutineUiModel) -> Unit,
  routineChecked: (RoutineUiModel) -> Unit,
  routineDeleteDialog: (RoutineUiModel) -> Unit,
) {
  LoadableComponent(
    loadableData = state.routines,
    loaded = { routines ->
      if (routines.isEmpty()) {
        if (searchText.isNotEmpty()) {
          EmptyMessage(
            space = space,
            size = size,
            fontSize = fontSize,
            messageEmpty = com.rahim.yadino.library.designsystem.R.string.search_empty_routine,
            painter = R.drawable.routine_empty,
          )
        } else {
          EmptyMessage(
            space = space,
            size = size,
            fontSize = fontSize,
            messageEmpty = R.string.not_routine,
            painter = R.drawable.routine_empty,
          )
        }
      } else {
        ListRoutines(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = space.space16),
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
  )
}

@Composable
private fun ItemTimeDate(
  times: PersistentList<TimeDateUiModel>,
  space: SpaceDimensions,
  size: SizeDimensions,
  fontSize: FontDimensions,
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
    modifier = Modifier.padding(top = space.space28),
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
        .padding(top = space.space12)
        .fillMaxWidth(0.3f),
      text = "${yearChecked.toString().toPersianDigits()} ${monthChecked.calculateMonthName()}",
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
      .padding(top = space.space18, end = space.space50, start = space.space50)
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    arrayString.reversed().forEachIndexed { index, nameDay ->
      Text(
        modifier = Modifier.padding(
          start = if (index == 0) space.space13 else 0.dp,
          end = if (index == 7) space.space12 else 0.dp,
          top = if (index == 7) space.space3 else 0.dp,
        ),
        text = nameDay,
        color = MaterialTheme.colorScheme.primary,
      )
    }
  }

  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
    IconButton(
      modifier = Modifier
        .weight(1f)
        .padding(top = space.space10),
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
        .padding(top = space.space8),
      times = times,
      screenWidth = screenWidth,
      dayCheckedNumber = dayCheckedNumber,
      indexDay = indexDay,
      space = space,
      fontSize = fontSize,
      size = size,
    )
    IconButton(
      modifier = Modifier
        .weight(1f)
        .padding(top = space.space10),
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
  fontSize: FontDimensions,
  space: SpaceDimensions,
  size: SizeDimensions,
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
            fontSize = fontSize,
            space = space,
            size = size,
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
  size: SizeDimensions,
  space: SpaceDimensions,
  fontSize: FontDimensions,
  dayCheckedNumber: (timeDate: TimeDateUiModel) -> Unit,
) {
  Text(
    modifier = Modifier
      .padding(
        top = space.space4,
        start = if (timeDate.isChecked) if (screenWidth <= 420) space.space5 else space.space7 else space.space6,
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
      .size(if (screenWidth <= 400) size.size36 else if (screenWidth in 400..420) size.size39 else size.size43)
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
        top = if (screenWidth <= 400) space.space8 else if (screenWidth in 400..420) space.space9 else space.space10,
      ),
    text = AnnotatedString(
      if (timeDate.dayNumber > 0) timeDate.dayNumber.toString().toPersianDigits() else "",
    ),
    style = TextStyle(
      fontSize = if (screenWidth <= 420) fontSize.fontSize16 else fontSize.fontSize18,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      fontFamily = font_medium,
      color = if (timeDate.isChecked) (Color.White) else MaterialTheme.colorScheme.primary,
    ),
  )
}
