package com.rahim.yadino.home

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
import com.rahim.R
import com.rahim.data.alarm.AlarmManagement
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.base.view.EmptyMessage
import com.rahim.utils.base.view.ItemRoutine
import com.rahim.utils.base.view.ProcessRoutineAdded
import com.rahim.utils.base.view.ShowSearchBar
import com.rahim.utils.resours.Resource
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    openDialog: Boolean,
    clickSearch: Boolean,
    onOpenDialog: (isOpen: Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {

    val currentYer = viewModel.currentYear
    val currentMonth = viewModel.currentMonth
    val currentDay = viewModel.currentDay

    val routines by viewModel.flowRoutines.collectAsStateWithLifecycle()
    val addRoutine by viewModel.addRoutine.collectAsStateWithLifecycle()
    val updateRoutine by viewModel.updateRoutine.collectAsStateWithLifecycle()
    Timber.tag("routineGetNameDay").d("recomposition HomeRoute->${routines.data}")
    HomeScreen(
        modifier = modifier,
        routines = routines,
        addRoutine = addRoutine,
        updateRoutine = updateRoutine,
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
    routines: Resource<List<Routine>>,
    addRoutine: Resource<Routine?>?,
    updateRoutine: Resource<Routine?>?,
    currentYer: Int,
    currentMonth: Int,
    currentDay: Int,
    openDialog: Boolean,
    clickSearch: Boolean,
    onCheckedRoutine: (Routine) -> Unit,
    onShowSampleRoutine: () -> Unit,
    onDeleteRoutine: (Routine) -> Unit,
    onUpdateRoutine: (Routine) -> Unit,
    onAddRoutine: (Routine) -> Unit,
    onClearAddRoutine: () -> Unit,
    onClearUpdateRoutine: () -> Unit,
    onOpenDialog: (isOpen: Boolean) -> Unit,
    onSearchText: (searchText: String) -> Unit,
) {
    val context = LocalContext.current
    val alarmManagement = AlarmManagement()
    val routineDeleteDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineUpdateDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    var searchText by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
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
                                messageEmpty = R.string.search_empty_routine
                            )
                        } else {
                            EmptyMessage()
                        }
                    } else {
                        ItemsHome(currentYer, currentMonth, currentDay,
                            it,
                            { checkedRoutine ->
                                onCheckedRoutine(checkedRoutine)
                                coroutineScope.launch {
                                    alarmManagement.cancelAlarm(
                                        context,
                                        checkedRoutine.idAlarm
                                            ?: checkedRoutine.id?.toLong()
                                    )
                                }
                            },
                            { routineUpdate ->
                                if (routineUpdate.isChecked) {
                                    Toast.makeText(
                                        context,
                                        R.string.not_update_checked_routine,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@ItemsHome
                                }
                                if (routineUpdate.isSample)
                                    onShowSampleRoutine()
                                routineUpdateDialog.value = routineUpdate
                                onOpenDialog(true)
                            },
                            { deleteRoutine ->
                                if (deleteRoutine.isSample)
                                    onShowSampleRoutine()
                                routineDeleteDialog.value = deleteRoutine
                            })
                    }
                }
            }

            is Resource.Error -> {}
        }
    }
    ErrorDialog(
        isOpen = routineDeleteDialog.value != null,
        isClickOk = {
            if (it) {
                routineDeleteDialog.value?.let {
                    onDeleteRoutine(it)
                    coroutineScope.launch {
                        alarmManagement.cancelAlarm(
                            context,
                            if (it.idAlarm == null) it.id?.toLong() else it.idAlarm
                        )
                    }
                }
            }
            routineDeleteDialog.value = null
        },
        message = stringResource(id = R.string.can_you_delete),
        okMessage = stringResource(
            id = R.string.ok
        )
    )
    DialogAddRoutine(
        isShowDay = false,
        isOpen = openDialog,
        openDialog = {
            onOpenDialog(false)
            routineUpdateDialog.value = null
        },
        routineUpdate = routineUpdateDialog.value,
        routine = { routine ->
            onShowSampleRoutine()
            if (routineUpdateDialog.value != null) {
                onUpdateRoutine(routine)
            } else {
                onAddRoutine(routine)
            }
        },
        currentNumberDay = currentDay,
        currentNumberMonth = currentMonth,
        currentNumberYear = currentYer,
        times = null, monthChange = { year: Int, month: Int ->  }
    )
    ProcessRoutineAdded(addRoutine, context) {
        it?.let {
            onOpenDialog(false)
            alarmManagement.setAlarm(context, it)
            onClearAddRoutine()
        }
    }
    ProcessRoutineAdded(updateRoutine, context) {
        it?.let {
            onOpenDialog(false)
            coroutineScope.launch {
                alarmManagement.updateAlarm(context, it)
            }
            onClearUpdateRoutine()
            routineUpdateDialog.value = null
        }
    }
}

@Composable
fun ItemsHome(
    currentDay: Int,
    currentMonth: Int,
    currentYer: Int,
    routines: List<Routine>,
    checkedRoutine: (Routine) -> Unit,
    updateRoutine: (Routine) -> Unit,
    deleteRoutine: (Routine) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 28.dp, vertical = 25.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "$currentDay/$currentMonth/$currentYer", fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(id = R.string.list_work_day),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(top = 0.dp, start = 16.dp, end = 16.dp)
    ) {
        items(items = routines) {
            ItemRoutine(routine = it, onChecked = {
                checkedRoutine(it)
            }, openDialogDelete = {
                deleteRoutine(it)
            }, openDialogEdit = {
                updateRoutine(it)
            })
        }
    }
}

@Preview
@Composable
fun HomeScreenWrapper() {
    YadinoTheme {
        val viewModel = hiltViewModel<HomeViewModel>()
//        HomeScreen(viewModel = viewModel)
    }
}