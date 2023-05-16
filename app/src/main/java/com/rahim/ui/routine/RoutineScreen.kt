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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.data.TimeData
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.dialog.DialogDelete
import com.rahim.ui.home.ItemRoutine
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.Zircon
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.base.view.gradientColors
import com.rahim.utils.extention.calculateMonthName
import com.rahim.utils.resours.Resource
import kotlinx.coroutines.launch

@Composable
fun RoutineScreen(
    modifier: Modifier = Modifier, viewModel: RoutineViewModel,
    onClickAdd: Boolean,
    isOpenDialog: (Boolean) -> Unit
) {
    val currentYer = viewModel.getCurrentTime()[0]
    val currentMonth = viewModel.getCurrentTime()[1]
    val currentDay = viewModel.getCurrentTime()[2]

    val routines by viewModel.getRoutines(currentMonth, currentDay, currentYer)
        .collectAsStateWithLifecycle(initialValue = Resource.Success(emptyList()))
    val monthDay by viewModel.getCurrentMonthDay(currentMonth, currentYer)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    val routineDeleteDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    val routineUpdateDialog = rememberSaveable { mutableStateOf<Routine?>(null) }
    var dayChecked by rememberSaveable { mutableStateOf("1") }
    var index by rememberSaveable { mutableStateOf(0) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.background(Zircon), topBar = {
            TopBarCenterAlign(
                modifier, stringResource(id = R.string.list_routine)
            )
        }, backgroundColor = Color.White
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 16.dp, start = 16.dp, top = 25.dp)
        ) {
            when (routines) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    routines.data?.let {
                        if (it.isEmpty()) {
                            EmptyRoutine(padding)
                        } else {
                            ItemsRoutine(
                                monthDay,
                                it,
                                padding,
                                dayChecked,
                                currentMonth = currentMonth.calculateMonthName(),
                                currentYer = currentYer.toString(),
                                listState = listState,
                                index = index,
                                dayCheckedNumber = {
                                    dayChecked = it
                                },
                                checkedRoutine = {
                                    routineUpdateDialog.value = it
                                },
                                updateRoutine = {
                                    routineUpdateDialog.value = it
                                },
                                deleteRoutine = {
                                    routineDeleteDialog.value = it
                                }, indexScroll = {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(it)
                                        index = it
                                    }
                                })
                        }
                    }
                }

                is Resource.Error -> {

                }
            }
        }
    }
    routineDeleteDialog.value?.let { routineFromDialog ->
        DialogDelete(isOpen = routineDeleteDialog.value != null, openDialog = {
            routineDeleteDialog.value = null
            if (it) {
                viewModel.deleteRoutine(routineFromDialog)
            }
        })
    }

    DialogAddRoutine(
        isOpen = onClickAdd || routineUpdateDialog.value != null,
        isShowDay = false,
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
        },
        currentNumberDay = currentDay,
        currentNumberMonth = currentMonth,
        currentNumberYer = currentYer
    )
}

@Composable
fun EmptyRoutine(paddingValues: PaddingValues) {
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
}

@Composable
fun ItemsRoutine(
    monthDay: List<TimeData>,
    routines: List<Routine>,
    paddingValues: PaddingValues,
    dayChecked: String,
    currentMonth: String,
    currentYer: String,
    listState: LazyListState,
    index: Int,
    dayCheckedNumber: (String) -> Unit,
    checkedRoutine: (Routine) -> Unit,
    updateRoutine: (Routine) -> Unit,
    deleteRoutine: (Routine) -> Unit,
    indexScroll: (Int) -> Unit
) {
    Text(text = "$currentYer $currentMonth")
    Row(modifier = Modifier.padding(top = 12.dp)) {
        IconButton(modifier = Modifier
            .weight(1f)
            .padding(top = 21.dp), onClick = {
            indexScroll(if (index == 6) 0 else index - 7)
        }) {
            Icon(painterResource(id = R.drawable.less_then), contentDescription = "less then sign")
        }
        Spacer(modifier = Modifier.width(12.dp))
        LazyRow(
            modifier = Modifier
                .weight(8f)
                .padding(end = 14.dp, top = 6.dp), state = listState
        ) {
            items(items = monthDay, itemContent = {
                ItemText(
                    it,
                    dayChecked,
                    checkedRoutine = { checkedRoutine(it) },
                    dayCheckedNumber = { dayCheckedNumber(it) })
            })
        }
        IconButton(modifier = Modifier
            .weight(1f)
            .padding(top = 21.dp), onClick = {
            indexScroll(if (monthDay.size == index + 7) monthDay.size - 1 else index + 7)
        }) {
            Icon(
                painterResource(id = R.drawable.greater_then),
                contentDescription = "greater then sign"
            )
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        items(items = routines, itemContent = {
            ItemRoutine(routine = it, onChecked = {
                updateRoutine(it)
            }, openDialogDelete = {
                deleteRoutine(it)
            }, openDialogEdit = {
                updateRoutine(it)
            })
        })
    }
}

@Composable
fun ItemText(
    timeData: TimeData, dayChecked: String,
    dayCheckedNumber: (String) -> Unit,
    checkedRoutine: (Routine) -> Unit,
) {
    Column() {
        Text(
            modifier = Modifier.padding(start = 12.dp),
            fontSize = 9.sp, text = timeData.nameDay.toString()
        )
        Box(
            modifier = Modifier
                .padding(
                    top = 12.dp, end = 9.dp
                )
                .size(29.dp)
                .clip(CircleShape)
                .background(
                    brush = if (dayChecked == timeData.dayNumber.toString()) {
                        Brush.verticalGradient(
                            gradientColors
                        )
                    } else Brush.horizontalGradient(
                        listOf(
                            Color.White,
                            Color.White
                        )
                    )
                )
        ) {
            ClickableText(
                modifier = Modifier.padding(
                    top = 4.dp,
                    start = if (dayChecked.length == 1) 10.dp else 6.dp
                ),
                onClick = { dayCheckedNumber(timeData.dayNumber.toString()) },
                text = AnnotatedString(timeData.dayNumber.toString()),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (dayChecked == timeData.dayNumber.toString()) (Color.White)
                    else Color.Black
                )
            )
        }
    }

}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
fun RoutineScreenPreview() {
//    YadinoTheme() {
//        RoutineScreen()
//    }
}