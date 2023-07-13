package com.rahim.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.rahim.R
import com.rahim.data.alarm.AlarmManagement
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.base.view.ItemRoutine
import com.rahim.utils.base.view.TopBarRightAlign
import com.rahim.utils.base.view.calculateHours
import com.rahim.utils.base.view.calculateMinute
import com.rahim.utils.resours.Resource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onClickAdd: Boolean,
    isOpenDialog: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val alarmManagement = AlarmManagement()

    val currentYer = viewModel.getCurrentTime()[0]
    val currentMonth = viewModel.getCurrentTime()[1]
    val currentDay = viewModel.getCurrentTime()[2]

    val routineDeleteDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineUpdateDialog = rememberSaveable { mutableStateOf<Routine?>(null) }

    val routines by viewModel.getCurrentRoutines()
        .collectAsStateWithLifecycle(initialValue = Resource.Success(emptyList()))

    Scaffold(
        topBar = {
            TopBarRightAlign(
                modifier, stringResource(id = R.string.hello_friend)
            )
        }, containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(end = 16.dp, start = 16.dp, top = 25.dp)) {
            setRoutine(currentYer, currentMonth, currentDay, it, routines, { checkedRoutine ->
                viewModel.updateRoutine(checkedRoutine)
            }, { routineUpdate ->
                routineUpdateDialog.value = routineUpdate
            }, { deleteRoutine ->
                routineDeleteDialog.value = deleteRoutine
            })
            routineDeleteDialog.value?.let { routineFromDialog ->
                ErrorDialog(
                    isOpen = routineDeleteDialog.value != null,
                    isClickOk = {
                        routineDeleteDialog.value = null
                        if (it) {
                            viewModel.deleteRoutine(routineFromDialog)
                        }
                    },
                    message = stringResource(id = R.string.can_you_delete),
                    okMessage = stringResource(
                        id = R.string.ok
                    )
                )
            }
        }
    }
    DialogAddRoutine(
        isOpen = onClickAdd || routineUpdateDialog.value != null,
        isShowDay = false,
        dayChecked = "",
        openDialog = {
            routineUpdateDialog.value = null
            isOpenDialog(it)
        },
        routineUpdate = routineUpdateDialog.value,
        routine = {
            alarmManagement.setAlarm(
                context,
                calculateHours(it.timeHours.toString()),
                calculateMinute(it.timeHours.toString()),
                it.yerNumber,
                it.monthNumber,
                it.dayNumber,
                it.name,
                it.explanation ?: ""
            )
            if (routineUpdateDialog.value != null) {
                viewModel.updateRoutine(it)
            } else {
                viewModel.addRoutine(it)
            }
        },
        currentNumberDay = currentDay,
        currentNumberMonth = currentMonth,
        currentNumberYer = currentYer
    )
}

@Composable
fun setRoutine(
    currentDay: Int,
    currentMonth: Int,
    currentYer: Int,
    paddingValues: PaddingValues,
    routines: Resource<List<Routine>>,
    checkedRoutine: (Routine) -> Unit,
    updateRoutine: (Routine) -> Unit,
    deleteRoutine: (Routine) -> Unit
) {
    when (routines) {
        is Resource.Loading -> {

        }

        is Resource.Success -> {
            routines.data?.let {
                if (it.isEmpty()) {
                    EmptyHome(paddingValues)
                } else {
                    ItemsHome(currentYer, currentMonth, currentDay,
                        paddingValues,
                        it,
                        { checkedRoutine(it) },
                        { updateRoutine(it) },
                        { deleteRoutine(it) })
                }
            }
        }

        is Resource.Error -> {

        }
    }
}


@Composable
fun EmptyHome(paddingValues: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        Image(
            modifier = Modifier
                .sizeIn(minHeight = 320.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.empty_list_home),
            contentDescription = "empty list home"
        )
        Text(
            text = stringResource(id = R.string.not_work_for_day),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ItemsHome(
    currentDay: Int,
    currentMonth: Int,
    currentYer: Int,
    paddingValues: PaddingValues,
    routines: List<Routine>,
    checkedRoutine: (Routine) -> Unit,
    updateRoutine: (Routine) -> Unit,
    deleteRoutine: (Routine) -> Unit
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "$currentYer/$currentMonth/$currentDay", fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = R.string.list_work_day), fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(top = 25.dp)
        ) {
            items(items = routines, itemContent = {
                ItemRoutine(routine = it, onChecked = {
                    checkedRoutine(it)
                }, openDialogDelete = {
                    deleteRoutine(it)
                }, openDialogEdit = {
                    updateRoutine(it)
                })
            })
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
fun HomeScreenPreview() {
    YadinoTheme {
        val viewModel = hiltViewModel<HomeViewModel>()
//        HomeScreen(viewModel = viewModel)
    }
}