package com.rahim.ui.routine


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
import com.rahim.R
import com.rahim.data.alarm.AlarmManagement
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.data.TimeData
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.utils.base.view.ItemRoutine
import com.rahim.utils.Constants.YYYY_MM_DD
import com.rahim.utils.base.view.ProcessRoutineAdded
import com.rahim.utils.base.view.ShowSearchBar
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.base.view.gradientColors
import com.rahim.utils.enums.HalfWeekName
import com.rahim.utils.extention.calculateMonthName
import com.rahim.utils.extention.calculateTimeFormat
import com.rahim.utils.resours.Resource
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RoutineScreen(
    modifier: Modifier = Modifier,
    viewModel: RoutineViewModel = hiltViewModel(),
    onClickAdd: Boolean,
    isOpenDialog: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val alarmManagement = AlarmManagement()

    val currentYer = viewModel.currentYer
    val currentMonth = viewModel.currentMonth

    val searchItems = ArrayList<Routine>()

    val routines by viewModel.flowRoutines.collectAsStateWithLifecycle(
        initialValue = Resource.Success(
            emptyList()
        )
    )
    val currentNameDay by viewModel.flowNameDay.collectAsStateWithLifecycle()
    val monthDay by viewModel.getCurrentMonthDay(currentMonth, currentYer)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val routineDeleteDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineUpdateDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineForAdd = rememberSaveable { mutableStateOf<Routine?>(null) }

    var dayChecked by rememberSaveable { mutableStateOf("0") }
    var index by rememberSaveable { mutableStateOf(0) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val currentIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    var searchText by rememberSaveable { mutableStateOf("") }
    var clickSearch by rememberSaveable { mutableStateOf(false) }

    val addRoutine by viewModel.addRoutine.collectAsStateWithLifecycle()


    checkDay(monthDay, dayChecked, coroutineScope, listState, index, {
        index += it
    }, {
        dayChecked = it
    })
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
                    searchText=search
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

            ItemTimeDate(monthDay,
                dayChecked,
                currentMonth.calculateMonthName(),
                currentYer.toString(),
                listState,
                index,
                dayCheckedNumber = {
                    dayChecked = it
                    viewModel.getCurrentNameDay(
                        String().calculateTimeFormat(currentYer, currentMonth, it),
                        YYYY_MM_DD
                    )
                    viewModel.getRoutines(currentMonth, it.toInt(), currentYer)
                },
                currentIndex = currentIndex,
                indexScroll = {
                    if (it != monthDay.size) {
                        index = it
                        coroutineScope.launch {
                            listState.animateScrollToItem(index)
                        }

                    }
                })
            GetRoutines(
                routines,
                searchItems,
                searchText,
                routineUpdateDialog = {
                    if (it.isSample)
                        viewModel.showSampleRoutine(true)

                    routineUpdateDialog.value = it
                },
                routineChecked = {
                    viewModel.updateRoutine(it)
                    alarmManagement.updateAlarm(
                        context,
                        it,
                        it.idAlarm?:it.id?.toLong()
                    )
                },
                routineDeleteDialog = {
                    if (it.isSample)
                        viewModel.showSampleRoutine(true)

                    routineDeleteDialog.value = it
                })
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
    if (onClickAdd || routineUpdateDialog.value != null){
        DialogAddRoutine(
            isShowDay = false,
            openDialog = {
                routineUpdateDialog.value = null
                routineForAdd.value = null
                isOpenDialog(it)
            },
            routineUpdate = routineUpdateDialog.value,
            routine = { routine ->
                if (routineUpdateDialog.value != null) {
                    viewModel.updateRoutine(routine)
                    alarmManagement.updateAlarm(
                        context,
                        routine,
                        routine.idAlarm?:routine.id?.toLong()
                    )
                } else {
                    viewModel.addRoutine(routine)
                    routineForAdd.value = routine
                }
                routineUpdateDialog.value = null
            },
            currentNumberDay = dayChecked.toInt(),
            currentNumberMonth = currentMonth,
            currentNumberYer = currentYer
        )
    }
    if (routineForAdd.value != null)
        ProcessRoutineAdded(addRoutine, context) {
            if (!it) {
                isOpenDialog(false)
                routineForAdd.value?.let {
                    alarmManagement.setAlarm(context, it)
                }
                routineForAdd.value = null
            }
        }
}

fun checkDay(
    monthDay: List<TimeData>,
    dayChecked: String,
    coroutineScope: CoroutineScope,
    listState: LazyListState,
    index: Int,
    calculateIndex: (Int) -> Unit,
    dayCheckedIsChecked: (String) -> Unit
) {
    coroutineScope.launch(Dispatchers.IO) {
        var i = index
        var day = dayChecked
        checkToday(monthDay) {
            if (day == "0") {
                day = it
                dayCheckedIsChecked(day)
                i += calculateIndex(it, i, monthDay)
                calculateIndex(i)
                coroutineScope.launch(Dispatchers.Main) {
                    listState.animateScrollToItem(i)
                }
            }
        }
    }
}

private fun calculateCurrentIndex(currentIndex: Int, previousIndex: Int): Int {
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


private fun calculateIndex(currentDay: String, index: Int, monthDay: List<TimeData>): Int {
    var currentIndex = index
    var currentIndexPlus = currentIndex + 7
    var dayNumber = 0
    var i = 0
    val emptyDay = ArrayList<Int>()
    while (dayNumber == 0) {
        dayNumber = monthDay[i].dayNumber
        if (dayNumber == 0) {
            emptyDay.add(monthDay[i].dayNumber)
        }
        i++
    }
    val currentDayInt = currentDay.toInt().plus(emptyDay.size)
    while (true) {
        currentIndex += 7
        currentIndexPlus += 7
        if (currentIndex >= currentDayInt) {
            currentIndex -= 7
            break
        }
    }
    return currentIndex
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
                        SetItemsRoutine(
                            searchItems.ifEmpty { it },
                            checkedRoutine = {
                                routineChecked(it)
                            },
                            updateRoutine = {
                                routineUpdateDialog(it)
                            },
                            deleteRoutine = {
                                routineDeleteDialog(it)
                            })
                    }
                }
            }
        }

        is Resource.Error -> {}
    }
}

@Composable
private fun SetItemsRoutine(
    routines: List<Routine>,
    checkedRoutine: (Routine) -> Unit,
    updateRoutine: (Routine) -> Unit,
    deleteRoutine: (Routine) -> Unit,
) {
    ItemsRoutine(routines, checkedRoutine, updateRoutine, deleteRoutine)
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
    monthDay: List<TimeData>,
    dayChecked: String,
    currentMonth: String,
    currentYer: String,
    listState: LazyListState,
    index: Int,
    currentIndex: Int,
    dayCheckedNumber: (String) -> Unit,
    indexScroll: (Int) -> Unit,
) {
    Text(
        modifier = Modifier.padding(top = 28.dp),
        text = "$currentYer $currentMonth",
        color = MaterialTheme.colorScheme.primary
    )
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
        IconButton(modifier = Modifier.padding(top = 10.dp), onClick = {
            indexScroll(
                if (monthDay.size <= index + 7) {
                    monthDay.size
                } else {
                    index + 7
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
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 6.dp),
                state = listState,
                flingBehavior = rememberSnapperFlingBehavior(
                    lazyListState = listState,
                    snapIndex = { _, start, targetIndex ->
                        indexScroll(calculateCurrentIndex(currentIndex, index))
                        index
                    }),
            ) {
                items(items = monthDay, itemContent = {
                    DayItems(it, dayChecked, dayCheckedNumber = { dayCheckedNumber(it) })
                })
            }
        }
        IconButton(modifier = Modifier.padding(top = 10.dp, start = 0.dp), onClick = {
            indexScroll(
                if (index <= 6) {
                    0
                } else {
                    index - 7
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
    timeData: TimeData,
    dayChecked: String,
    dayCheckedNumber: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(
                top = 12.dp, end = 9.dp
            )
            .size(34.dp)
            .clip(CircleShape)
            .background(
                brush = if (dayChecked == timeData.dayNumber.toString()) {
                    Brush.verticalGradient(
                        gradientColors
                    )
                } else Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        ClickableText(
            modifier = Modifier.padding(
                top = 4.dp, start = if (dayChecked.length == 1) 12.dp else 8.dp
            ),
            onClick = { dayCheckedNumber(timeData.dayNumber.toString()) },
            text = AnnotatedString(if (timeData.dayNumber != 0) timeData.dayNumber.toString() else ""),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (dayChecked == timeData.dayNumber.toString()) (Color.White) else MaterialTheme.colorScheme.primary
            )
        )
    }
}

private fun checkToday(timeData: List<TimeData>, dayChecked: (String) -> Unit) {
    timeData.forEach {
        if (it.isToday) {
            dayChecked(it.dayNumber.toString())
        }
    }
}