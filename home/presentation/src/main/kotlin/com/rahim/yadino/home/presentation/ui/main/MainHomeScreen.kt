package com.rahim.yadino.home.presentation.ui.main

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.rahim.yadino.base.LoadableComponent
import com.rahim.yadino.base.LoadableData
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.component.requestNotificationPermission
import com.rahim.yadino.designsystem.utils.size.FontDimensions
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.size.SpaceDimensions
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.home.presentation.model.CurrentDateUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogRemoveUiModel
import com.rahim.yadino.home.presentation.model.ErrorDialogUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.home.presentation.ui.component.ListRoutines
import com.rahim.yadino.home.presentation.ui.main.component.MainHomeComponent
import com.rahim.yadino.home.presentation.ui.root.component.RootHomeComponent
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.showToastShort
import com.rahim.yadino.toPersianDigits
import com.rahim.yadino.toStringResource
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeMainScreen(
  modifier: Modifier = Modifier,
  clickSearch: Boolean,
  component: MainHomeComponent,
) {
  val (state, effect, event) = use(component = component)
  val context = LocalContext.current

  val snackBarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
  val title = stringResource(com.rahim.yadino.core.base.R.string.permission_notification)
  val submitTextButton = stringResource(R.string.setting)

  LaunchedEffect(effect) {
    effect.collect { effect ->
      when (effect) {
        is MainHomeComponent.Effect.ShowSnackBar -> {
          val messageSnackBar = context.getString(effect.message.toStringResource())
          scope.launch {
            snackBarHostState.showSnackbar(
              message = messageSnackBar,
              duration = SnackbarDuration.Short,
            )
          }
        }

        is MainHomeComponent.Effect.ShowToast -> {
          context.showToastShort(effect.message.toStringResource())
        }
      }
    }
  }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        containerColor = CornflowerBlueLight,
        contentColor = Color.White,
        onClick = {
          val onPermissionGranted = {
            event(MainHomeComponent.Event.ShowAddRoutineDialog)
          }

          val onPermissionDenied = {
            event(
              MainHomeComponent.Event.ShowErrorDialog(
                ErrorDialogUiModel(
                  title = title,
                  submitTextButton = submitTextButton,
                ),
              ),
            )
          }

          notificationPermissionState.requestNotificationPermission(
            onGranted = { onPermissionGranted() },
            onShowRationale = { onPermissionDenied() },
          )
        },
      ) {
        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_add), "add item")
      }
    },
  ) { innerPadding ->
    HomeScreen(
      modifier = modifier.padding(innerPadding),
      state = state,
      clickSearch = clickSearch,
      onCheckedRoutine = {
        event.invoke(MainHomeComponent.Event.CheckedRoutine(it))
      },
      onShowErrorDialog = { deleteUiModel ->
        event.invoke(MainHomeComponent.Event.ShowErrorDialogRemoveRoutine(errorDialogModel = deleteUiModel))
      },
      onUpdateRoutine = {
        event.invoke(MainHomeComponent.Event.ShowUpdateRoutineDialog(it))
      },
      onSearchText = {
        event.invoke(MainHomeComponent.Event.SearchRoutine(it))
      },
    )
  }
}

@OptIn(FlowPreview::class)
@Composable
private fun HomeScreen(
  modifier: Modifier = Modifier,
  state: MainHomeComponent.State,
  clickSearch: Boolean,
  onCheckedRoutine: (RoutineUiModel) -> Unit,
  onShowErrorDialog: (errorDialogRemoveUiModel: ErrorDialogRemoveUiModel) -> Unit,
  onUpdateRoutine: (RoutineUiModel) -> Unit,
  onSearchText: (searchText: String) -> Unit,
) {
  val context = LocalContext.current
  val space = LocalSpacing.current
  val size = LocalSize.current
  val fontSize = LocalFontSize.current
  val title = stringResource(R.string.can_you_delete)
  val submitTextButton = stringResource(R.string.ok)

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
            deleteRoutine = { deleteRoutine ->
              onShowErrorDialog(ErrorDialogRemoveUiModel(title = title, submitTextButton = submitTextButton, routineUiModel = deleteRoutine))
            },
          )
        }
      },
    )
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
      state = MainHomeComponent.State(
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
      onShowErrorDialog = { _ -> },
      onUpdateRoutine = {},
      onSearchText = {},
    )
  }
}
