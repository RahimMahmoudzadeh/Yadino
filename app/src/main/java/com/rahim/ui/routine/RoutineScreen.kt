package com.rahim.ui.routine


import android.Manifest
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.rahim.R
import com.rahim.data.alarm.AlarmManagement
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.data.TimeDate
import com.rahim.data.modle.data.TimeDataMonthAndYear
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.utils.base.view.ItemRoutine
import com.rahim.utils.Constants.YYYY_MM_DD
import com.rahim.utils.base.view.ProcessRoutineAdded
import com.rahim.utils.base.view.ShowSearchBar
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.base.view.goSettingPermission
import com.rahim.utils.base.view.gradientColors
import com.rahim.utils.base.view.requestPermissionNotification
import com.rahim.utils.enums.HalfWeekName
import com.rahim.utils.extention.calculateMonthName
import com.rahim.utils.extention.calculateTimeFormat
import com.rahim.utils.resours.Resource
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RoutineScreen(
    modifier: Modifier = Modifier,
    viewModel: RoutineViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val alarmManagement = AlarmManagement()

    val searchItems = ArrayList<Routine>()

    val routines by viewModel.flowRoutines.collectAsStateWithLifecycle(
        initialValue = Resource.Success(
            emptyList()
        )
    )

    val times by viewModel.getTimes()
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val routineDeleteDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineUpdateDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineChecked = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineForAdd = rememberSaveable { mutableStateOf<Routine?>(null) }
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var errorClick by rememberSaveable { mutableStateOf(false) }
    var dayChecked by rememberSaveable { mutableIntStateOf(0) }
    var dayMonthChecked by rememberSaveable { mutableIntStateOf(0) }
    var dayYerChecked by rememberSaveable { mutableIntStateOf(0) }
    var dayMonthCheckedDialog by rememberSaveable { mutableIntStateOf(viewModel.currentMonth) }
    var dayYerCheckedDialog by rememberSaveable { mutableIntStateOf(viewModel.currentYer) }
    var dayCheckedDialog by rememberSaveable { mutableIntStateOf(viewModel.currentDay) }
    var timesSize by rememberSaveable { mutableIntStateOf(0) }
    var indexDay by rememberSaveable { mutableIntStateOf(-1) }
    val timeMonth by viewModel.getTimesMonth(dayYerCheckedDialog, dayMonthCheckedDialog)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val listStateDay = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val currentDayIndex by remember {
        derivedStateOf {
            listStateDay.firstVisibleItemIndex
        }
    }
    val notificationPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )
    var searchText by rememberSaveable { mutableStateOf("") }
    var clickSearch by rememberSaveable { mutableStateOf(false) }

    val addRoutine by viewModel.addRoutine.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    if (timesSize != times.size) {
        timesSize = times.size
        val currentTime = times.find { it.isToday }
        var indexCurrentDay = times.indexOf(currentTime)
        val currentDay = currentTime?.dayNumber ?: 0
        val currentYerNumber = currentTime?.yerNumber ?: 0
        val monthNumber = currentTime?.monthNumber ?: 0
        coroutineScope.launch(Dispatchers.IO) {
            val calculateDayIndex = calculateIndexDay(indexCurrentDay)
            coroutineScope.launch(Dispatchers.Main) {
                if (calculateDayIndex >= 0) {
                    dayChecked = currentDay
                    dayYerChecked = currentYerNumber
                    dayMonthChecked = monthNumber
                    if (calculateDayIndex > 0) {
                        indexDay = calculateDayIndex
                        listStateDay.scrollToItem(calculateDayIndex)
                    }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopBarCenterAlign(
                modifier, stringResource(id = R.string.list_routine)
            ) {
                clickSearch = !clickSearch
            }
        }, containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            if (!routines.data.isNullOrEmpty()) {
                ShowSearchBar(clickSearch = clickSearch, searchText = searchText) { search ->
                    searchText = search
                    coroutineScope.launch(Dispatchers.IO) {
                        if (search.isNotEmpty()) {
                            routines.data?.filter {
                                it.name.contains(search)
                            }?.let {
                                searchItems.clear()
                                searchItems.addAll(it)
                            }
                        } else {
                            searchItems.clear()
                        }
                    }
                }
            }

            ItemTimeDate(
                times,
                dayChecked.toString(),
                dayYerChecked,
                dayMonthChecked,
                listStateDay = listStateDay,
                indexDay = indexDay,
                dayCheckedNumber = { day, yer, month ->
                    dayChecked = day.toInt()
                    dayMonthChecked = month
                    dayYerChecked = yer
                    viewModel.getCurrentNameDay(
                        String().calculateTimeFormat(yer, month, day),
                        YYYY_MM_DD
                    )
                    viewModel.getRoutines(month, day.toInt(), yer)
                },
                currentIndexDay = currentDayIndex,
                indexScrollDay = {
                    if (it != times.size) {
                        indexDay = it
                        coroutineScope.launch {
                            listStateDay.animateScrollToItem(indexDay)
                        }
                    }
                },
            )
            GetRoutines(
                routines,
                searchItems,
                searchText,
                routineUpdateDialog = {
                    if (it.isChecked) {
                        Toast.makeText(
                            context,
                            R.string.not_update_checked_routine,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@GetRoutines
                    }
                    if (it.isSample)
                        viewModel.showSampleRoutine(true)

                    routineUpdateDialog.value = it
                },
                routineChecked = {
                    routineChecked.value = it
                    Timber.tag("routineGetNameDay").d("GetRoutines routineChecked->$it")
                },
                routineDeleteDialog = {
                    if (it.isChecked) {
                        Toast.makeText(
                            context,
                            R.string.not_removed_checked_routine,
                            Toast.LENGTH_SHORT
                        ).show()
                        return@GetRoutines
                    }
                    if (it.isSample)
                        viewModel.showSampleRoutine(true)

                    routineDeleteDialog.value = it
                })
        }
        FloatingActionButton(
            containerColor = CornflowerBlueLight,
            contentColor = Color.White,
            modifier = modifier
                .padding(padding)
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
            isOpen = true, isClickOk = {
                if (it) {
                    routineDeleteDialog.value?.let {
                        viewModel.deleteRoutine(it)
                        alarmManagement.cancelAlarm(
                            context,
                            if (it.idAlarm == null) it.id?.toLong() else it.idAlarm
                        )
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
    routineChecked.value?.let {
        viewModel.updateRoutine(it)
        alarmManagement.cancelAlarm(
            context,
            it.idAlarm ?: it.id?.toLong()
        )
        routineChecked.value = null
    }
    DialogAddRoutine(
        isShowDay = false,
        isOpen = openDialog || routineUpdateDialog.value != null,
        openDialog = {
            routineUpdateDialog.value = null
            routineForAdd.value = null
            openDialog = it
        },
        routineUpdate = routineUpdateDialog.value,
        routine = { routine ->
            if (routineUpdateDialog.value != null) {
                viewModel.updateRoutine(routine)
                alarmManagement.updateAlarm(
                    context,
                    routine
                )
            } else {
                coroutineScope.launch {
                    viewModel.addRoutine(routine)
                    delay(200)
                    routineForAdd.value = routine
                }
            }
            routineUpdateDialog.value = null
        },
        currentNumberDay = dayChecked,
        currentNumberMonth = dayMonthChecked,
        currentNumberYer = dayYerChecked,
        times = timeMonth,
        dayCheckedNumber = { day, yer, month ->
            if (day == 0 && yer == 0 && month == 0) {
                dayMonthCheckedDialog = viewModel.currentMonth
                dayYerCheckedDialog = viewModel.currentYer
                dayCheckedDialog = viewModel.currentDay
            } else {
                dayMonthCheckedDialog = month
                dayYerCheckedDialog = yer
                dayCheckedDialog = day
            }
        }
    )

    if (routineForAdd.value != null)
        ProcessRoutineAdded(addRoutine, context) {
            if (!it) {
                openDialog = false
                addRoutine.data?.let {
                    alarmManagement.setAlarm(context, it)
                }
                routineForAdd.value = null
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


private fun calculateCurrentIndexDay(currentIndex: Int, previousIndex: Int): Int {
    return if (currentIndex > previousIndex) {
        previousIndex + 7
    } else {
        if (previousIndex - 7 < 0) {
            previousIndex
        } else {
            previousIndex - 7
        }
    }
}

private fun calculateIndexDay(index: Int): Int {
    var indexPosition = 0
    while (true) {
        indexPosition += 7
        if (indexPosition > index) {
            indexPosition -= 7
            break
        }
    }
    return indexPosition
}

@Composable
private fun GetRoutines(
    routines: Resource<List<Routine>>,
    searchItems: List<Routine>,
    searchText: String,
    routineUpdateDialog: (Routine) -> Unit,
    routineChecked: (Routine) -> Unit,
    routineDeleteDialog: (Routine) -> Unit
) {
    when (routines) {
        is Resource.Loading -> {}
        is Resource.Success -> {
            routines.data?.let {
                if (it.isEmpty()) {
                    EmptyRoutine()
                } else {
                    if (searchItems.isEmpty() && searchText.isNotEmpty()) {
                        EmptyRoutine(messageEmpty = R.string.search_empty_routine)
                    } else {
                        ItemsRoutine(
                            searchItems.ifEmpty { it },
                            checkedRoutine = {
                                Timber.tag("routineGetNameDay")
                                    .d("ItemsRoutine checkedRoutine->$it")
                                routineChecked(it)
                            },
                            updateRoutine = {
                                routineUpdateDialog(it)
                            },
                            deleteRoutine = {
                                routineDeleteDialog(it)
                            }
                        )
                    }
                }
            }
        }

        is Resource.Error -> {}
    }
}

@Composable
private fun EmptyRoutine(
    modifier: Modifier = Modifier,
    @StringRes messageEmpty: Int = R.string.not_routine
) {
    Image(
        modifier = modifier
            .padding(top = 40.dp)
            .sizeIn(minHeight = 320.dp)
            .fillMaxWidth(),
        painter = painterResource(id = R.drawable.routine_empty),
        contentDescription = "empty list home"
    )
    Text(
        text = stringResource(id = messageEmpty),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp),
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.primary
    )
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
private fun ItemTimeDate(
    times: List<TimeDate>,
    dayChecked: String,
    dayYerChecked: Int,
    dayMonthChecked: Int,
    listStateDay: LazyListState,
    indexDay: Int,
    currentIndexDay: Int,
    dayCheckedNumber: (day: String, yer: Int, month: Int) -> Unit,
    indexScrollDay: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.padding(top = 28.dp),
    ) {
        IconButton(onClick = {
            var month = dayMonthChecked.plus(1)
            var year = dayYerChecked
            if (month > 12) {
                month = 1
                year = dayYerChecked.plus(1)
            }
            val time =
                times.find { it.monthNumber == month && it.yerNumber == year && it.dayNumber == 1 }
            if (time != null) {
                dayCheckedNumber("1", year, month)
                val index = times.indexOf(time)
                indexScrollDay(
                    calculateIndexDay(index)
                )
            }
        }) {
            Icon(
                tint = MaterialTheme.colorScheme.primary,
                painter = painterResource(id = R.drawable.less_then),
                contentDescription = "less then sign"
            )
        }
        Text(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(0.3f),
            text = "$dayYerChecked ${dayMonthChecked.calculateMonthName()}",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        IconButton(onClick = {
            var month = dayMonthChecked.minus(1)
            var year = dayYerChecked
            if (month < 1) {
                month = 12
                year = dayYerChecked.minus(1)
            }
            val time =
                times.find { it.monthNumber == month && it.yerNumber == year && it.dayNumber == 1 }
            if (time != null) {
                dayCheckedNumber("1", year, month)
                val index = times.indexOf(time)
                indexScrollDay(
                    calculateIndexDay(index)
                )
            }
        }) {
            Icon(
                painterResource(id = R.drawable.greater_then),
                contentDescription = "greater then sign",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    Row(
        modifier = Modifier
            .padding(top = 18.dp, end = 50.dp, start = 50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 13.dp),
            fontSize = 14.sp,
            text = HalfWeekName.FRIDAY.nameDay,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            fontSize = 14.sp,
            text = HalfWeekName.THURSDAY.nameDay,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            fontSize = 14.sp,
            text = HalfWeekName.WEDNESDAY.nameDay,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            fontSize = 14.sp,
            text = HalfWeekName.TUESDAY.nameDay,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            fontSize = 14.sp,
            text = HalfWeekName.MONDAY.nameDay,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            fontSize = 14.sp,
            text = HalfWeekName.SUNDAY.nameDay,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(end = 12.dp, top = 3.dp),
            fontSize = 12.sp,
            text = HalfWeekName.SATURDAY.nameDay,
            color = MaterialTheme.colorScheme.primary
        )
    }

    Row() {
        IconButton(modifier = Modifier.padding(top = 6.dp), onClick = {
            indexScrollDay(
                if (times.size <= indexDay + 7) {
                    times.size
                } else if (indexDay == -1) {
                    indexDay + 8
                } else {
                    indexDay + 7
                }
            )
        }) {
            Icon(
                tint = MaterialTheme.colorScheme.primary,
                painter = painterResource(id = R.drawable.less_then),
                contentDescription = "less then sign"
            )
        }
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

            LazyRow(
                userScrollEnabled = false,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 6.dp),
                state = listStateDay,
                flingBehavior = rememberSnapperFlingBehavior(
                    lazyListState = listStateDay,
                    snapIndex = { _, start, targetIndex ->
                        indexScrollDay(calculateCurrentIndexDay(currentIndexDay, indexDay))
                        indexDay
                    }),
            ) {
                items(items = times, itemContent = {
                    DayItems(
                        it,
                        dayChecked,
                        dayYerChecked,
                        dayMonthChecked,
                        dayCheckedNumber = { day, yer, month ->
                            dayCheckedNumber(day, yer, month)
                        })
                })
            }
        }
        IconButton(modifier = Modifier.padding(top = 6.dp), onClick = {
            indexScrollDay(
                if (indexDay <= 6) {
                    0
                } else {
                    indexDay - 7
                }
            )
        }) {
            Icon(
                painterResource(id = R.drawable.greater_then),
                contentDescription = "greater then sign",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ItemsRoutine(
    routines: List<Routine>,
    checkedRoutine: (Routine) -> Unit,
    updateRoutine: (Routine) -> Unit,
    deleteRoutine: (Routine) -> Unit,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, start = 16.dp),
        contentPadding = PaddingValues(top = 8.dp)
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

@Composable
private fun DayItems(
    timeDate: TimeDate,
    dayChecked: String,
    dayYerChecked: Int,
    dayMonthChecked: Int,
    dayCheckedNumber: (day: String, yer: Int, month: Int) -> Unit,
) {
    val configuration = LocalConfiguration.current
    Timber.tag("timeClicked").d("dayChecked->$dayChecked")
    Timber.tag("timeClicked").d("dayYerChecked->$dayYerChecked")
    Timber.tag("timeClicked").d("dayMonthChecked->$dayMonthChecked")
    Timber.tag("timeClicked").d("timeData->$timeDate")
    val screenWidth = configuration.screenWidthDp
    ClickableText(
        modifier = Modifier
            .padding(
                top = 4.dp,
                start = if (dayChecked.length == 1) if (screenWidth <= 420) 5.dp else 7.dp else 6.dp
            )
            .size(if (screenWidth <= 400) 36.dp else if (screenWidth in 400..420) 39.dp else 43.dp)
            .clip(CircleShape)
            .background(
                brush = if (dayChecked == timeDate.dayNumber.toString() && dayMonthChecked == timeDate.monthNumber && dayYerChecked == timeDate.yerNumber) {
                    Brush.verticalGradient(
                        gradientColors
                    )
                } else Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(
                top = if (screenWidth <= 400) 8.dp else if (screenWidth in 400..420) 9.dp else 10.dp
            ),
        onClick = {
            if (timeDate.dayNumber > 0)
                dayCheckedNumber(
                    timeDate.dayNumber.toString(),
                    timeDate.yerNumber,
                    timeDate.monthNumber
                )
        },
        text = AnnotatedString(if (timeDate.dayNumber > 0) timeDate.dayNumber.toString() else ""),
        style = TextStyle(
            fontSize = if (screenWidth <= 420) 16.sp else 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (dayChecked == timeDate.dayNumber.toString() && dayMonthChecked == timeDate.monthNumber && dayYerChecked == timeDate.yerNumber) (Color.White) else MaterialTheme.colorScheme.primary
        )
    )
}