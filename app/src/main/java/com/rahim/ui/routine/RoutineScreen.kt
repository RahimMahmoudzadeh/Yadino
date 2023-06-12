package com.rahim.ui.routine

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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.data.TimeData
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.dialog.DialogDelete
import com.rahim.ui.home.ItemRoutine
import com.rahim.ui.theme.Zircon
import com.rahim.utils.Constants.YYYY_MM_DD
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.base.view.gradientColors
import com.rahim.utils.enums.WeekName
import com.rahim.utils.extention.calculateMonthName
import com.rahim.utils.extention.calculateTimeFormat
import com.rahim.utils.resours.Resource
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.launch

@Composable
fun RoutineScreen(
    modifier: Modifier = Modifier,
    viewModel: RoutineViewModel,
    onClickAdd: Boolean,
    isOpenDialog: (Boolean) -> Unit
) {
    val currentYer = viewModel.currentYer
    val currentMonth = viewModel.currentMonth

    val routines by viewModel.flowRoutines.collectAsStateWithLifecycle()
    val currentNameDay by viewModel.flowNameDay.collectAsStateWithLifecycle()
    val monthDay by viewModel.getCurrentMonthDay(currentMonth, currentYer)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val routineDeleteDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineUpdateDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    var dayChecked by rememberSaveable { mutableStateOf("0") }
    var index by rememberSaveable { mutableStateOf(0) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val currentIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    Scaffold(
        modifier = modifier.background(Zircon), topBar = {
            TopBarCenterAlign(
                modifier, stringResource(id = R.string.list_routine)
            )
        }, backgroundColor = Color.White
    ) { padding ->
        checkToday(monthDay) {
            if (dayChecked == "0") {
                dayChecked = it
                index += calculateIndex(it, index)
                coroutineScope.launch {
                    listState.animateScrollToItem(index)
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

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
            GetRoutines(routines,
                routineUpdateDialog = { routineUpdateDialog.value = it },
                routineChecked = { viewModel.updateRoutine(it) },
                routineDeleteDialog = { routineDeleteDialog.value = it })
        }
    }
    routineDeleteDialog.value?.let { routineFromDialog ->
        DialogDelete(isOpen = true, openDialog = {
            routineDeleteDialog.value = null
            if (it) {
                viewModel.deleteRoutine(routineFromDialog)
            }
        })
    }
    DialogAddRoutine(
        isOpen = onClickAdd || routineUpdateDialog.value != null,
        isShowDay = false,
        dayChecked = currentNameDay,
        openDialog = {
            routineUpdateDialog.value = null
            isOpenDialog(it)
        },
        routineUpdate = routineUpdateDialog.value,
        routine = {
            if (routineUpdateDialog.value != null) {
                viewModel.updateRoutine(it)
            } else {
                viewModel.addRoutine(it)
            }
            viewModel.getRoutines(it.monthNumber ?: 0, it.dayNumber ?: 0, it.yerNumber ?: 0)
        },
        currentNumberDay = dayChecked.toInt(),
        currentNumberMonth = currentMonth,
        currentNumberYer = currentYer
    )
}

fun calculateCurrentIndex(currentIndex: Int, previousIndex: Int): Int {
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


fun calculateIndex(currentDay: String, index: Int): Int {
    var currentIndex = index
    var currentIndexPlus = currentIndex + 7

    while (true) {
        currentIndex += 7
        currentIndexPlus += 7
        if (currentIndex >= currentDay.toInt() || currentDay.toInt() in currentIndex..currentIndexPlus) {
            break
        }
    }
    return currentIndex
}

@Composable
fun GetRoutines(
    routines: Resource<List<Routine>>,
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
                    SetItemsRoutine(it, checkedRoutine = {
                        routineChecked(it)
                    }, updateRoutine = {
                        routineUpdateDialog(it)
                    }, deleteRoutine = {
                        routineDeleteDialog(it)
                    })
                }
            }
        }

        is Resource.Error -> {}
        else -> {}
    }
}

@Composable
fun SetItemsRoutine(
    routines: List<Routine>,
    checkedRoutine: (Routine) -> Unit,
    updateRoutine: (Routine) -> Unit,
    deleteRoutine: (Routine) -> Unit,
) {
    ItemsRoutine(routines, checkedRoutine, updateRoutine, deleteRoutine)
}

@Composable
private fun EmptyRoutine() {
    Image(
        modifier = Modifier
            .padding(top = 40.dp)
            .sizeIn(minHeight = 320.dp)
            .fillMaxWidth(),
        painter = painterResource(id = R.drawable.routine_empty),
        contentDescription = "empty list home"
    )
    Text(
        text = stringResource(id = R.string.not_routine),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp),
        textAlign = TextAlign.Center,
        fontSize = 18.sp
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
    Text(modifier = Modifier.padding(top = 28.dp), text = "$currentYer $currentMonth")
    Row(modifier = Modifier.padding(top = 16.dp)) {
        Text(
            modifier = Modifier.padding(end = 4.dp),
            fontSize = 10.sp,
            text = WeekName.FRIDAY.nameDay
        )
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 0.dp),
            fontSize = 10.sp,
            text = WeekName.THURSDAY.nameDay
        )
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 4.dp),
            fontSize = 10.sp,
            text = WeekName.WEDNESDAY.nameDay
        )
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 4.dp),
            fontSize = 10.sp,
            text = WeekName.TUESDAY.nameDay
        )
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 4.dp),
            fontSize = 10.sp,
            text = WeekName.MONDAY.nameDay
        )
        Text(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp),
            fontSize = 10.sp,
            text = WeekName.SUNDAY.nameDay
        )
        Text(
            modifier = Modifier.padding(start = 14.dp, end = 0.dp),
            fontSize = 10.sp,
            text = WeekName.SATURDAY.nameDay
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
                painterResource(id = R.drawable.less_then), contentDescription = "less then sign"
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
        IconButton(modifier = Modifier.padding(top = 10.dp, start = 10.dp), onClick = {
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
                contentDescription = "greater then sign"
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
fun DayItems(
    timeData: TimeData,
    dayChecked: String,
    dayCheckedNumber: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(
                top = 12.dp, end = 9.dp
            )
            .size(32.dp)
            .clip(CircleShape)
            .background(
                brush = if (dayChecked == timeData.dayNumber.toString()) {
                    Brush.verticalGradient(
                        gradientColors
                    )
                } else Brush.horizontalGradient(
                    listOf(
                        Color.White, Color.White
                    )
                )
            )
    ) {
        ClickableText(
            modifier = Modifier.padding(
                top = 4.dp, start = if (dayChecked.length == 1) 11.dp else 7.dp
            ),
            onClick = { dayCheckedNumber(timeData.dayNumber.toString()) },
            text = AnnotatedString(if (timeData.dayNumber != 0) timeData.dayNumber.toString() else ""),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (dayChecked == timeData.dayNumber.toString()) (Color.White)
                else Color.Black
            )
        )
    }
}

fun checkToday(timeData: List<TimeData>, dayChecked: (String) -> Unit) {
    timeData.forEach {
        if (it.isToday) {
            dayChecked(it.dayNumber.toString())
        }
    }
}