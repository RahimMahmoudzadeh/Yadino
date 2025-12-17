package com.rahim.yadino.home.presentation.ui

import android.widget.Toast
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.Child
import com.rahim.yadino.base.LoadableComponent
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.utils.size.FontDimensions
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.size.SpaceDimensions
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.home.presentation.component.HomeComponent
import com.rahim.yadino.home.presentation.component.addRoutineDialog.AddRoutineDialogComponent
import com.rahim.yadino.home.presentation.ui.addDialogRoutine.AddRoutineDialog
import com.rahim.yadino.home.presentation.ui.component.ListRoutines
import com.rahim.yadino.home.presentation.model.CurrentDateUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.showToastShort
import com.rahim.yadino.toPersianDigits
import com.rahim.yadino.toStringResource
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.collections.immutable.persistentListOf
import com.rahim.yadino.base.LoadableData
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
  modifier: Modifier = Modifier,
  clickSearch: Boolean,
  homeComponent: HomeComponent,
  dialogSlot: Child.Created<Any, AddRoutineDialogComponent>?,
) {

  val context = LocalContext.current
  val snackBarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val (state, effect, event) = use(component = homeComponent)
  dialogSlot?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      AddRoutineDialog(
        component = dialogComponent,
      )
    }
  }

  LaunchedEffect(true) {
    effect?.let {
      when (effect) {
        is HomeComponent.Effect.ShowSnackBar -> {
          scope.launch {
            snackBarHostState.showSnackbar(
              message = context.getString(effect.message.toStringResource()),
              duration = SnackbarDuration.Short,
            )
          }
        }

        is HomeComponent.Effect.ShowToast -> {
          context.showToastShort(effect.message.toStringResource())
        }
      }
    }
  }
  HomeScreen(
    modifier = modifier,
    state = state,
    clickSearch = clickSearch,
    onCheckedRoutine = {
      event.invoke(HomeComponent.Event.CheckedRoutine(it))
    },
    onDeleteRoutine = {
      event.invoke(HomeComponent.Event.DeleteRoutine(it))
    },
    onUpdateRoutine = {
      event.invoke(HomeComponent.Event.OnShowUpdateRoutineDialog(it))
    },
    onSearchText = {
      event.invoke(HomeComponent.Event.SearchRoutine(it))
    },
  )
}

@OptIn(FlowPreview::class)
@Composable
private fun HomeScreen(
  modifier: Modifier = Modifier,
  state: HomeComponent.State,
  clickSearch: Boolean,
  onCheckedRoutine: (RoutineUiModel) -> Unit,
  onDeleteRoutine: (RoutineUiModel) -> Unit,
  onUpdateRoutine: (RoutineUiModel) -> Unit,
  onSearchText: (searchText: String) -> Unit,
) {
  val context = LocalContext.current
  val space = LocalSpacing.current
  val size = LocalSize.current
  val fontSize = LocalFontSize.current

  val routineModelDeleteDialog = rememberSaveable { mutableStateOf<RoutineUiModel?>(null) }
  var searchText by rememberSaveable { mutableStateOf("") }

  LaunchedEffect(Unit) {
    snapshotFlow { searchText }
      .debounce(300)
      .distinctUntilChanged()
      .collect { query ->
        onSearchText(query)
      }
  }
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    modifier = modifier.fillMaxSize(),
  ) {
    ShowSearchBar(clickSearch, searchText = searchText) { search ->
      searchText = search
    }
    LoadableComponent(
      loadableData = state.routines,
      loading = {},
      loaded = { routines ->
        if (routines.isEmpty()) {
          EmptyMessage(
            space = space,
            size = size,
            fontSize = fontSize,
            messageEmpty = if (searchText.isNotEmpty()) R.string.search_empty_routine else R.string.not_work_for_day,
          )
        } else {
          ItemsHome(
            currentTime = state.currentDate,
            routineModels = routines,
            space = space,
            fontSize = fontSize,
            checkedRoutine = { checkedRoutine ->
              onCheckedRoutine(checkedRoutine)
            },
            updateRoutine = { routineUpdate ->
              if (routineUpdate.isChecked) {
                Toast.makeText(
                  context,
                  R.string.not_update_checked_routine,
                  Toast.LENGTH_SHORT,
                ).show()
                return@ItemsHome
              }
              onUpdateRoutine(routineUpdate)
            },
            { deleteRoutine ->
              routineModelDeleteDialog.value = deleteRoutine
            },
          )
        }
      },

      )
  }
  when {
    routineModelDeleteDialog.value != null -> {

    }
  }
}

@Composable
fun ItemsHome(
  currentTime: CurrentDateUiModel?,
  routineModels: PersistentList<RoutineUiModel>,
  space: SpaceDimensions,
  fontSize: FontDimensions,
  checkedRoutine: (RoutineUiModel) -> Unit,
  updateRoutine: (RoutineUiModel) -> Unit,
  deleteRoutine: (RoutineUiModel) -> Unit,
) {
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .padding(horizontal = space.space28, vertical = space.space24)
      .fillMaxWidth(),
  ) {
    currentTime?.date?.let { currentTime ->
      Text(
        text = currentTime.toPersianDigits(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
      )
    }
    Text(
      text = stringResource(id = com.rahim.yadino.home.presentation.R.string.list_work_day),
      fontSize = fontSize.fontSize18,
      color = MaterialTheme.colorScheme.primary,
    )
  }
  ListRoutines(modifier = Modifier.fillMaxWidth(), routines = routineModels, checkedRoutine = checkedRoutine, deleteRoutine = deleteRoutine, updateRoutine = updateRoutine)
}

@Preview
@Composable
private fun HomeScreenPreview() {
  YadinoTheme {
    HomeScreen(
      state = HomeComponent.State(
        routines = LoadableData.Loaded(
          persistentListOf(
            RoutineUiModel(
              name = "Task 1",
              colorTask = 0,
              dayName = "Saturday",
              dayNumber = 1,
              monthNumber = 1,
              yearNumber = 1403,
              timeHours = "10:00",
            ),
            RoutineUiModel(
              name = "Task 2",
              colorTask = 1,
              dayName = "Saturday",
              dayNumber = 1,
              monthNumber = 1,
              yearNumber = 1403,
              timeHours = "11:00",
              isChecked = true,
            ),
          ),
        ),
        currentDate = CurrentDateUiModel("شنبه ۱ فروردین"),
      ),
      clickSearch = false,
      onCheckedRoutine = {},
      onDeleteRoutine = {},
      onUpdateRoutine = {},
      onSearchText = {},
    )
  }
}
