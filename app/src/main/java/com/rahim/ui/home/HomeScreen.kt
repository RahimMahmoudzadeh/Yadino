package com.rahim.ui.home

import android.Manifest
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.google.accompanist.permissions.rememberPermissionState
import com.rahim.R
import com.rahim.data.alarm.AlarmManagement
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.base.view.ItemRoutine
import com.rahim.utils.base.view.ProcessRoutineAdded
import com.rahim.utils.base.view.ShowSearchBar
import com.rahim.utils.base.view.ShowStatusBar
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.base.view.goSettingPermission
import com.rahim.utils.base.view.requestPermissionNotification
import com.rahim.utils.resours.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    ShowStatusBar(true)
    val context = LocalContext.current
    val alarmManagement = AlarmManagement()
    val currentYer = viewModel.getCurrentTime()[0]
    val currentMonth = viewModel.getCurrentTime()[1]
    val currentDay = viewModel.getCurrentTime()[2]
    val searchItems = ArrayList<Routine>()

    val routineDeleteDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineUpdateDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var clickSearch by rememberSaveable { mutableStateOf(false) }
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var errorClick by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val notificationPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )
    viewModel.getCurrentRoutines()
    val routines by viewModel.flowRoutines
        .collectAsStateWithLifecycle(initialValue = Resource.Success(emptyList()))

    val addRoutine by viewModel.addRoutine.collectAsStateWithLifecycle()
    val updateRoutine by viewModel.updateRoutine.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopBarCenterAlign(
                modifier, stringResource(id = R.string.hello_friend), onClickSearch = {
                    clickSearch = !clickSearch
                }
            )
        }, containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (routines.data?.isEmpty() == true) Arrangement.Center else Arrangement.Top,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (routines.data?.isNotEmpty() == true) {
                ShowSearchBar(clickSearch, searchText = searchText) { search ->
                    searchText = search
                    coroutineScope.launch(Dispatchers.IO) {
                        if (search.isNotEmpty()) {
                            searchItems.clear()
                            routines.data?.let {
                                searchItems.addAll(it.filter {
                                    it.name.contains(searchText)
                                })
                            }
                        } else {
                            searchItems.clear()
                        }
                    }
                }
            }
            when (routines) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    routines.data?.let {
                        if (it.isEmpty()) {
                            EmptyHome()
                        } else {
                            if (searchItems.isEmpty() && searchText.isNotEmpty()) {
                                EmptyHome(
                                    Modifier.padding(top = 70.dp),
                                    messageEmpty = R.string.search_empty_routine
                                )
                            } else {
                                ItemsHome(currentYer, currentMonth, currentDay,
                                    if (searchText.isEmpty()) it else searchItems,
                                    { checkedRoutine ->
                                        viewModel.checkedRoutine(checkedRoutine)
                                        coroutineScope.launch {
                                        alarmManagement.cancelAlarm(
                                            context,
                                            checkedRoutine.idAlarm ?: checkedRoutine.id?.toLong()
                                        )}
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
                                            viewModel.showSampleRoutine(true)

                                        routineUpdateDialog.value = routineUpdate
                                        openDialog = true
                                    },
                                    { deleteRoutine ->
                                        if (deleteRoutine.isChecked) {
                                            Toast.makeText(
                                                context,
                                                R.string.not_removed_checked_routine,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@ItemsHome
                                        }
                                        if (deleteRoutine.isSample)
                                            viewModel.showSampleRoutine(true)

                                        routineDeleteDialog.value = deleteRoutine
                                    })
                            }
                        }
                    }
                }

                is Resource.Error -> {}
            }
        }
        FloatingActionButton(
            containerColor = CornflowerBlueLight,
            contentColor = Color.White,
            modifier = modifier
                .padding(paddingValues)
                .offset(
                    x = (configuration.screenWidthDp.dp) - 70.dp,
                    y = (configuration.screenHeightDp.dp) - 190.dp
                ),
            onClick = {
                requestPermissionNotification(isGranted = {
                    if (it)
                        openDialog = true
                    else
                        errorClick = true
                }, permissionState = {
                    it.launchPermissionRequest()
                }, notificationPermission = notificationPermissionState)

            },
        ) {
            Icon(Icons.Filled.Add, "add item")
        }
    }
    routineDeleteDialog.value?.let { routineFromDialog ->
        ErrorDialog(
            isOpen = routineDeleteDialog.value != null,
            isClickOk = {
                if (it) {
                    routineDeleteDialog.value?.let {
                        viewModel.deleteRoutine(it)
                        coroutineScope.launch {
                        alarmManagement.cancelAlarm(
                            context,
                            if (it.idAlarm == null) it.id?.toLong() else it.idAlarm
                        )}
                    }
                }
                routineDeleteDialog.value = null
            },
            message = stringResource(id = R.string.can_you_delete),
            okMessage = stringResource(
                id = R.string.ok
            )
        )
    }
    DialogAddRoutine(
        isShowDay = false,
        isOpen = openDialog,
        openDialog = {
            openDialog = false
            routineUpdateDialog.value = null
        },
        routineUpdate = routineUpdateDialog.value,
        routine = { routine ->
            if (routineUpdateDialog.value != null) {
                viewModel.updateRoutine(routine)
            } else {
                coroutineScope.launch {
                    viewModel.addRoutine(routine)
                }
            }
        },
        currentNumberDay = currentDay,
        currentNumberMonth = currentMonth,
        currentNumberYer = currentYer,
        times = null,
        dayCheckedNumber = { day, yer, month -> }
    )
    ProcessRoutineAdded(addRoutine, context) {
        it?.let {
            openDialog = false
            alarmManagement.setAlarm(context, it)
            viewModel.clearAddRoutine()
        }
    }
    ProcessRoutineAdded(updateRoutine, context) {
        it?.let {
            openDialog = false
            coroutineScope.launch {
                alarmManagement.updateAlarm(context, it)
            }
            viewModel.clearUpdateRoutine()
            routineUpdateDialog.value = null
        }
    }

    if (errorClick) {
        ErrorDialog(
            isOpen = true,
            message = stringResource(id = R.string.better_performance_access),
            okMessage = stringResource(id = R.string.setting),
            isClickOk = {
                if (it) {
                    goSettingPermission(context)
                }
                errorClick = false
            }
        )
    }
}

@Composable
fun EmptyHome(
    modifier: Modifier = Modifier,
    @StringRes messageEmpty: Int = R.string.not_work_for_day
) {
    Image(
        modifier = modifier
            .sizeIn(minHeight = 320.dp)
            .fillMaxWidth(),
        painter = painterResource(id = R.drawable.empty_list_home),
        contentDescription = "empty list home"
    )
    Text(
        text = stringResource(id = messageEmpty),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.primary
    )
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
            text = stringResource(id = R.string.list_work_day), fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(top = 0.dp, start = 16.dp, end = 16.dp)
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
fun HomeScreenPreview() {
    YadinoTheme {
        val viewModel = hiltViewModel<HomeViewModel>()
//        HomeScreen(viewModel = viewModel)
    }
}