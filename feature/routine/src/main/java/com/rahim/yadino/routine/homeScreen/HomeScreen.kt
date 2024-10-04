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
import com.rahim.yadino.persianLocate
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ItemRoutine
import com.rahim.yadino.designsystem.component.ProcessRoutineAdded
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.dialog.DialogAddRoutine
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.theme.YadinoTheme
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.model.RoutineModel
import timber.log.Timber

@Composable
internal fun HomeRoute(
  modifier: Modifier = Modifier,
  openDialog: Boolean,
  clickSearch: Boolean,
  onOpenDialog: (isOpen: Boolean) -> Unit,
  viewModel: HomeViewModel = hiltViewModel(),
) {

  val currentYer = viewModel.currentYear
  val currentMonth = viewModel.currentMonth
  val currentDay = viewModel.currentDay

  val routines by viewModel.flowRoutines.collectAsStateWithLifecycle(Resource.Loading())
  val addRoutine by viewModel.addRoutine.collectAsStateWithLifecycle()
  val updateRoutine by viewModel.updateRoutine.collectAsStateWithLifecycle()
  Timber.tag("routineGetNameDay").d("recomposition HomeRoute->${routines.data}")
  HomeScreen(
    modifier = modifier,
    routines = routines,
    addRoutine = addRoutine,
    updateRoutineModel = updateRoutine,
    currentYer = currentYer,
    currentMonth = currentMonth,
    currentDay = currentDay,
    openDialog = openDialog,
    clickSearch = clickSearch,
    onCheckedRoutine = viewModel::checkedRoutine,
    onShowSampleRoutine = viewModel::showSampleRoutine,
    onDeleteRoutine = viewModel::deleteRoutine,
    onUpdateRoutine = viewModel::updateRoutine,
    onAddRoutine = viewModel::addRoutine,
    onClearAddRoutine = viewModel::clearAddRoutine,
    onClearUpdateRoutine = viewModel::clearUpdateRoutine,
    onOpenDialog = onOpenDialog,
    onSearchText = viewModel::searchItems,
  )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    routines: Resource<List<RoutineModel>>,
    addRoutine: Resource<Nothing?>?,
    updateRoutineModel: Resource<Nothing?>?,
    currentYer: Int,
    currentMonth: Int,
    currentDay: Int,
    openDialog: Boolean,
    clickSearch: Boolean,
    onCheckedRoutine: (RoutineModel) -> Unit,
    onShowSampleRoutine: () -> Unit,
    onDeleteRoutine: (RoutineModel) -> Unit,
    onUpdateRoutine: (RoutineModel) -> Unit,
    onAddRoutine: (RoutineModel) -> Unit,
    onClearAddRoutine: () -> Unit,
    onClearUpdateRoutine: () -> Unit,
    onOpenDialog: (isOpen: Boolean) -> Unit,
    onSearchText: (searchText: String) -> Unit,
) {
  val context = LocalContext.current
  val routineModelDeleteDialog = rememberSaveable { mutableStateOf<RoutineModel?>(null) }
  val routineModelUpdateDialog = rememberSaveable { mutableStateOf<RoutineModel?>(null) }
  var searchText by rememberSaveable { mutableStateOf("") }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    modifier = modifier
      .fillMaxSize(),
  ) {
    ShowSearchBar(clickSearch, searchText = searchText) { search ->
      searchText = search
      onSearchText(searchText)
    }
    when (routines) {
      is Resource.Loading -> {}
      is Resource.Success -> {
        routines.data?.let {
          if (it.isEmpty()) {
            if (searchText.isNotEmpty()) {
              EmptyMessage(
                messageEmpty = R.string.search_empty_routine,
              )
            } else {
              EmptyMessage()
            }
          } else {
            ItemsHome(
              currentYer, currentMonth, currentDay,
              it,
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
                if (routineUpdate.isSample)
                  onShowSampleRoutine()
                routineModelUpdateDialog.value = routineUpdate
                onOpenDialog(true)
              },
              { deleteRoutine ->
                if (deleteRoutine.isSample)
                  onShowSampleRoutine()
                routineModelDeleteDialog.value = deleteRoutine
              },
            )
          }
        }
      }

      is Resource.Error -> {}
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
    },
    updateRoutineExplanation = routineModelUpdateDialog.value?.explanation ?: "",
    updateRoutineDay = routineModelUpdateDialog.value?.dayNumber,
    updateRoutineMonth = routineModelUpdateDialog.value?.monthNumber,
    updateRoutineYear = routineModelUpdateDialog.value?.yerNumber,
    updateRoutineName = routineModelUpdateDialog.value?.name ?: "",
    updateRoutineTime = routineModelUpdateDialog.value?.timeHours ?: "",
    currentNumberDay = currentDay,
    currentNumberMonth = currentMonth,
    currentNumberYear = currentYer,
    monthChange = { year: Int, month: Int -> },
  )
  ProcessRoutineAdded(addRoutine, context) {
    onOpenDialog(false)
    onClearAddRoutine()
  }
  ProcessRoutineAdded(updateRoutineModel, context) {
    onOpenDialog(false)
    onClearUpdateRoutine()
  }
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
  LazyColumn(
    modifier = Modifier
      .fillMaxWidth(),
    contentPadding = PaddingValues(top = 0.dp, start = 16.dp, end = 16.dp),
  ) {
    items(items = routineModels) { routine ->
      ItemRoutine(
        nameRoutine = routine.name,
        isChecked = routine.isChecked,
        timeHoursRoutine = routine.timeHours ?: "",
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
    }
  }
}

@Preview
@Composable
fun HomeScreenWrapper() {
  YadinoTheme {
    HomeRoute(openDialog = false, clickSearch = false, onOpenDialog = {})
  }
}
