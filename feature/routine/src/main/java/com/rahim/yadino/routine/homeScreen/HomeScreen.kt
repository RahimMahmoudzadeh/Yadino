package com.rahim.yadino.routine.homeScreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahim.yadino.Resource
import com.rahim.yadino.base.use
import com.rahim.yadino.persianLocate
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ItemRoutine
import com.rahim.yadino.designsystem.component.ListRoutines
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.component.ShowToastShort
import com.rahim.yadino.designsystem.dialog.DialogAddRoutine
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.theme.YadinoTheme
import com.rahim.yadino.errorMessage
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.model.RoutineModel

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
    onShowSampleRoutine = {
      event.invoke(HomeContract.HomeEvent.ShowSampleRoutines)
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
  onCheckedRoutine: (RoutineModel) -> Unit,
  onShowSampleRoutine: () -> Unit,
  onDeleteRoutine: (RoutineModel) -> Unit,
  onUpdateRoutine: (RoutineModel) -> Unit,
  onAddRoutine: (RoutineModel) -> Unit,
  onOpenDialog: (isOpen: Boolean) -> Unit,
  onSearchText: (searchText: String) -> Unit,
) {
  val context = LocalContext.current
  val routineModelDeleteDialog = rememberSaveable { mutableStateOf<RoutineModel?>(null) }
  val routineModelUpdateDialog = rememberSaveable { mutableStateOf<RoutineModel?>(null) }
  var searchText by rememberSaveable { mutableStateOf("") }

  homeState.errorMessage?.let { errorMessage ->
    ShowToastShort(errorMessage.errorMessage(), context)
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
    if (homeState.routineLoading) {

    }
    if (homeState.routines.isEmpty()) {
      EmptyMessage(
        messageEmpty = if (searchText.isNotEmpty()) R.string.search_empty_routine else R.string.not_work_for_day,
      )
    } else {
      ItemsHome(
        homeState.currentYer, homeState.currentMonth, homeState.currentDay,
        homeState.routines,
        { checkedRoutine ->
          onCheckedRoutine(checkedRoutine)
        },
        { routineUpdate ->
          if (routineUpdate.isChecked) {
            Toast.makeText(
              context,
              R.string.not_update_checked_routine,
              Toast.LENGTH_SHORT,
            ).show()
            return@ItemsHome
          }
          if (routineUpdate.isSample) onShowSampleRoutine()
          routineModelUpdateDialog.value = routineUpdate
          onOpenDialog(true)
        },
        { deleteRoutine ->
          if (deleteRoutine.isSample) onShowSampleRoutine()
          routineModelDeleteDialog.value = deleteRoutine
        },
      )
    }
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
      routineModelUpdateDialog.value = null
    },
    routineItems = { routineName, routineExplanation, routineTime, dayChecked, monthChecked, yearChecked, dayName ->
      onShowSampleRoutine()
      if (routineModelUpdateDialog.value != null) {
        val updatedRoutine = routineModelUpdateDialog.value?.copy(
          name = routineName,
          explanation = routineExplanation,
          timeHours = routineTime,
          dayNumber = dayChecked,
          monthNumber = monthChecked,
          yerNumber = yearChecked,
          dayName = dayName,
        )
        routineModelUpdateDialog.value = updatedRoutine
        routineModelUpdateDialog.value?.let(onUpdateRoutine)
      } else {
        val routineModel = RoutineModel(
          name = routineName,
          dayName = dayName,
          dayNumber = dayChecked,
          monthNumber = monthChecked,
          yerNumber = yearChecked,
          explanation = routineExplanation,
          timeHours = routineTime,
          colorTask = null,
        )
        onAddRoutine(routineModel)
      }
      onOpenDialog(false)
    },
    updateRoutineExplanation = routineModelUpdateDialog.value?.explanation ?: "",
    updateRoutineDay = routineModelUpdateDialog.value?.dayNumber,
    updateRoutineMonth = routineModelUpdateDialog.value?.monthNumber,
    updateRoutineYear = routineModelUpdateDialog.value?.yerNumber,
    updateRoutineName = routineModelUpdateDialog.value?.name ?: "",
    updateRoutineTime = routineModelUpdateDialog.value?.timeHours ?: "",
    currentNumberDay = homeState.currentDay,
    currentNumberMonth = homeState.currentMonth,
    currentNumberYear = homeState.currentYer,
    monthChange = { year: Int, month: Int -> },
  )
}

@Composable
fun ItemsHome(
  currentDay: Int,
  currentMonth: Int,
  currentYer: Int,
  routineModels: List<RoutineModel>,
  checkedRoutine: (RoutineModel) -> Unit,
  updateRoutine: (RoutineModel) -> Unit,
  deleteRoutine: (RoutineModel) -> Unit,
) {
  val data = "$currentDay/$currentMonth/$currentYer"
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .padding(horizontal = 28.dp, vertical = 25.dp)
      .fillMaxWidth(),
  ) {
    Text(
      text = data.persianLocate(),
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.primary,
    )
    Text(
      text = stringResource(id = com.rahim.yadino.feature.routine.R.string.list_work_day),
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
