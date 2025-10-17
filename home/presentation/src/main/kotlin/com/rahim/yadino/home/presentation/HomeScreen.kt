package com.rahim.yadino.home.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rahim.yadino.base.LoadableComponent
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.enums.RoutineExplanation
import com.rahim.yadino.toStringResource
import com.rahim.yadino.home.presentation.component.DialogAddRoutine
import com.rahim.yadino.home.presentation.component.ListRoutines
import com.rahim.yadino.home.presentation.model.CurrentDateUiModel
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.persianLocate
import com.rahim.yadino.showToastShort
import kotlinx.collections.immutable.PersistentList

@Composable
internal fun HomeRoute(
  modifier: Modifier = Modifier,
  openDialog: Boolean,
  clickSearch: Boolean,
  onOpenDialog: (isOpen: Boolean) -> Unit,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val (state, event) = use(viewModel = viewModel)

  HomeScreen(
    modifier = modifier,
    homeState = state,
    openDialog = openDialog,
    clickSearch = clickSearch,
    onCheckedRoutine = {
      event.invoke(HomeContract.HomeEvent.CheckedRoutine(it))
    },
    onDeleteRoutine = {
      event.invoke(HomeContract.HomeEvent.DeleteRoutine(it))
    },
    onUpdateRoutine = {
      event.invoke(HomeContract.HomeEvent.UpdateRoutine(it))
    },
    onAddRoutine = {
      event.invoke(HomeContract.HomeEvent.AddRoutine(it))
    },
    onOpenDialog = onOpenDialog,
    onSearchText = {
      event.invoke(HomeContract.HomeEvent.SearchRoutine(it))
    },
  )
}

@Composable
private fun HomeScreen(
  modifier: Modifier = Modifier,
  homeState: HomeContract.HomeState,
  openDialog: Boolean,
  clickSearch: Boolean,
  onCheckedRoutine: (RoutineUiModel) -> Unit,
  onDeleteRoutine: (RoutineUiModel) -> Unit,
  onUpdateRoutine: (RoutineUiModel) -> Unit,
  onAddRoutine: (RoutineUiModel) -> Unit,
  onOpenDialog: (isOpen: Boolean) -> Unit,
  onSearchText: (searchText: String) -> Unit,
) {
  val context = LocalContext.current
  val routineModelDeleteDialog = rememberSaveable { mutableStateOf<RoutineUiModel?>(null) }
  val routineModelUpdateDialog = rememberSaveable { mutableStateOf<RoutineUiModel?>(null) }
  var searchText by rememberSaveable { mutableStateOf("") }

  homeState.errorMessage?.let { errorMessage ->
    context.showToastShort(errorMessage.toStringResource())
  }
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    modifier = modifier.fillMaxSize(),
  ) {
    ShowSearchBar(clickSearch, searchText = searchText) { search ->
      searchText = search
      onSearchText(searchText)
    }
    LoadableComponent(
      loadableData = homeState.routines,
      loading = {},
      loaded = { routines ->
        if (routines.isEmpty()) {
          EmptyMessage(
            messageEmpty = if (searchText.isNotEmpty()) R.string.search_empty_routine else R.string.not_work_for_day,
          )
        } else {
          ItemsHome(
            currentTime = homeState.currentDate,
            routineModels = routines,
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
              routineModelUpdateDialog.value = routineUpdate
              onOpenDialog(true)
            },
            { deleteRoutine ->
              routineModelDeleteDialog.value = deleteRoutine
            },
          )
        }
      },
      error = { errorMessageCode ->
        context.showToastShort(errorMessageCode.toStringResource())
      },
    )
  }
  when {
    routineModelDeleteDialog.value != null -> {
      ErrorDialog(
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
    }

    openDialog -> {
      DialogAddRoutine(
        onCloseDialog = {
          onOpenDialog(false)
          routineModelUpdateDialog.value = null
        },
        onRoutineCreated = { routine ->
          if (routineModelUpdateDialog.value != null) {
            onUpdateRoutine(routine)
          } else {
            onAddRoutine(routine)
          }
          onOpenDialog(false)
        },
        updateRoutine = routineModelUpdateDialog.value?.copy(
          explanation = routineModelUpdateDialog.value?.explanation?.let {
            when (it) {
              RoutineExplanation.ROUTINE_RIGHT_SAMPLE.explanation -> {
                stringResource(id = R.string.routine_right_sample)
              }

              RoutineExplanation.ROUTINE_LEFT_SAMPLE.explanation -> {
                stringResource(id = R.string.routine_left_sample)
              }

              else -> {
                it
              }
            }
          } ?: run {
            routineModelUpdateDialog.value?.explanation ?: ""
          },
        ),
      )
    }
  }
}

@Composable
fun ItemsHome(
  currentTime: CurrentDateUiModel?,
  routineModels: PersistentList<RoutineUiModel>,
  checkedRoutine: (RoutineUiModel) -> Unit,
  updateRoutine: (RoutineUiModel) -> Unit,
  deleteRoutine: (RoutineUiModel) -> Unit,
) {
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .padding(horizontal = 28.dp, vertical = 25.dp)
      .fillMaxWidth(),
  ) {
    currentTime?.date?.let { currentTime ->
      Text(
        text = currentTime.persianLocate(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
      )
    }
    Text(
      text = stringResource(id = com.rahim.yadino.home.presentation.R.string.list_work_day),
      fontSize = 18.sp,
      color = MaterialTheme.colorScheme.primary,
    )
  }
  ListRoutines(modifier = Modifier.fillMaxWidth(), routines = routineModels, checkedRoutine = checkedRoutine, deleteRoutine = deleteRoutine, updateRoutine = updateRoutine)
}

@Preview
@Composable
fun HomeScreenWrapper() {
  YadinoTheme {
    HomeRoute(openDialog = false, clickSearch = false, onOpenDialog = {})
  }
}
