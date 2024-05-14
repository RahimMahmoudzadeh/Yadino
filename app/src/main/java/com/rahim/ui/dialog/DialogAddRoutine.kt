package com.rahim.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.data.TimeDate
import com.rahim.ui.theme.*
import com.rahim.utils.base.view.DialogButtonBackground
import com.rahim.utils.base.view.gradientColors
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import saman.zamani.persiandate.PersianDate
import timber.log.Timber
import java.time.LocalTime

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun DialogAddRoutine(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    isShowDay: Boolean,
    currentNumberDay: Int,
    currentNumberMonth: Int,
    currentNumberYer: Int,
    routineUpdate: Routine? = null,
    times: List<TimeDate>? = null,
    openDialog: () -> Unit,
    routine: (Routine) -> Unit,
    dayCheckedNumber: (day: Int, yer: Int, month: Int) -> Unit
) {
    val maxName = 22
    val maxExplanation = 40
    var routineName by rememberSaveable { mutableStateOf("") }
    var routineExplanation by rememberSaveable { mutableStateOf("") }
    val checkedStateAllDay = remember { mutableStateOf(false) }
    val isErrorName = remember { mutableStateOf(false) }
    val isShowDateDialog = remember { mutableStateOf(false) }
    val isErrorExplanation = remember { mutableStateOf(false) }

    var time by rememberSaveable { mutableStateOf("12:00") }
    val alarmDialogState = rememberMaterialDialogState()
    val persianData = PersianDate()
    val date = persianData.initJalaliDate(currentNumberYer, currentNumberMonth, currentNumberDay)

    val dayWeek = stringArrayResource(id = R.array.day_weeks)
    if (routineUpdate != null) {
        routineName = routineUpdate.name
        routineExplanation = routineUpdate.explanation ?: ""
        time = routineUpdate.timeHours ?: "12:00"
    }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        if (!isOpen) {
            routineName = ""
            routineExplanation = ""
            isErrorName.value = false
            isErrorExplanation.value = false
            time = "12:00"
            return@CompositionLocalProvider
        }
        BasicAlertDialog(properties = DialogProperties(
            usePlatformDefaultWidth = false, dismissOnClickOutside = false
        ), modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .border(
                2.dp,
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(8.dp)
            ), onDismissRequest = {
            openDialog()
        }) {
            Surface(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(percent = 4)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, end = 12.dp, start = 12.dp, bottom = 8.dp)
                ) {
                    Text(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(id = R.string.creat_new_work),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(brush = Brush.verticalGradient(gradientColors))
                    )
                    TextField(modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                        .height(52.dp)
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(gradientColors),
                            shape = RoundedCornerShape(4.dp)
                        ), value = routineName, onValueChange = {
                        isErrorName.value = it.length >= maxName
                        routineName = if (it.length <= maxName) it else routineName
                    }, placeholder = {
                        Text(
                            text = stringResource(id = R.string.name_hint_text_filed_routine),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }, colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                        disabledIndicatorColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background
                    )
                    )

                    if (isErrorName.value) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = if (routineName.isEmpty()) stringResource(id = R.string.emptyField) else stringResource(
                                id = R.string.length_textFiled_name_routine
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    TextField(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .padding(top = 18.dp)
                            .height(90.dp)
                            .border(
                                width = 1.dp,
                                brush = Brush.verticalGradient(gradientColors),
                                shape = RoundedCornerShape(4.dp)
                            ),
                        value = if (routineExplanation.isNullOrEmpty()) "" else routineExplanation,
                        onValueChange = {
                            isErrorExplanation.value = it.length >= maxExplanation
                            routineExplanation =
                                if (it.length <= maxExplanation) it else routineExplanation
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.routine_explanation),
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                            disabledIndicatorColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background
                        )
                    )
                    if (isErrorExplanation.value) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(id = R.string.length_textFiled_explanation_routine),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    if (isShowDay) {
                        Row(
                            modifier = Modifier.padding(top = 4.dp, end = 4.dp, start = 4.dp)
                        ) {
                            dayWeek.forEach { dayName ->
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            top = 9.dp,
                                            end = 6.dp,
                                        )
                                        .size(30.dp)
                                        .clip(CircleShape)
//                                        .background(
//                                            brush = if (checkedStateAllDay.value || dayChecked == dayName) {
//                                                Brush.verticalGradient(
//                                                    gradientColors
//                                                )
//                                            } else Brush.horizontalGradient(
//                                                listOf(
//                                                    Color.White, Color.White
//                                                )
//                                            )
//                                        )
                                ) {
//                                        ClickableText(
//                                            modifier = Modifier.padding(
//                                                top = 8.dp,
//                                                start = if (dayName in dayWeekSmale) 10.dp else 6.dp
//                                            ),
////                                            onClick = { dayChecked = dayName },
//                                            text = AnnotatedString(dayName),
//                                            style = TextStyle(
//                                                fontSize = 10.sp,
//                                                fontWeight = FontWeight.Bold,
//                                                color = if (checkedStateAllDay.value || dayChecked == dayName) (Color.White)
//                                                else Color.Black
//                                            )
//                                        )
                                }
                            }
                            ClickableText(
                                onClick = {},
                                text = AnnotatedString(stringResource(id = R.string.all)),
                                modifier = Modifier.padding(top = 14.dp)
                            )
                            Checkbox(
                                modifier = Modifier.padding(start = 10.dp),
                                checked = checkedStateAllDay.value,
                                onCheckedChange = { checkedStateAllDay.value = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Purple, uncheckedColor = CornflowerBlueLight
                                )
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = if (isShowDay) 0.dp else 10.dp,
                                start = 20.dp,
                                end = if (isShowDay) 15.dp else 0.dp
                            )
                    ) {
                        Row {
                            Text(
                                modifier = Modifier.padding(top = 14.dp),
                                text = stringResource(id = R.string.set_alarms),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                modifier = Modifier.padding(top = 14.dp, start = 4.dp),
                                text = time,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        OutlinedButton(border = BorderStroke(
                            1.dp, Brush.horizontalGradient(gradientColors)
                        ), onClick = {
                            alarmDialogState.show()
                        }) {
                            Text(
                                text = stringResource(id = R.string.time_change), style = TextStyle(
                                    brush = Brush.verticalGradient(
                                        gradientColors
                                    )
                                )
                            )
                        }
                    }
                    if (!times.isNullOrEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (isShowDay) 0.dp else 10.dp,
                                    start = 20.dp,
                                    end = if (isShowDay) 15.dp else 0.dp
                                ),
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 14.dp),
                                text = stringResource(id = R.string.set_reminder),
                                color = MaterialTheme.colorScheme.primary
                            )
                            OutlinedButton(border = BorderStroke(
                                1.dp, Brush.horizontalGradient(gradientColors)
                            ), onClick = { isShowDateDialog.value = true }) {
                                Text(
                                    text = stringResource(id = R.string.data_change),
                                    style = TextStyle(brush = Brush.verticalGradient(gradientColors))
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(12.dp)
                    ) {
                        DialogButtonBackground(text = stringResource(id = R.string.confirmation),
                            gradient = Brush.verticalGradient(gradientColors),
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .height(40.dp),
                            textSize = 14.sp,
                            onClick = {
                                Timber.tag("tagNameRoutine").d(routineName.length.toString())

                                if (routineName.isEmpty()) {
                                    isErrorName.value = true
                                } else {
                                    routine(routineUpdate?.apply {
                                        name = routineName
                                        timeHours = time
                                        explanation = routineExplanation
                                        dayName = persianData.dayName(date)
                                    } ?: Routine(
                                        routineName,
                                        null,
                                        persianData.dayName(date),
                                        currentNumberDay,
                                        currentNumberMonth,
                                        currentNumberYer,
                                        time,
                                        explanation = routineExplanation,
                                    ))
                                }
                            })
                        Spacer(modifier = Modifier.width(10.dp))
                        TextButton(onClick = {
                            openDialog()
                        }) {
                            Text(
                                fontSize = 16.sp,
                                text = stringResource(id = R.string.cancel),
                                style = TextStyle(
                                    brush = Brush.verticalGradient(
                                        gradientColors
                                    )
                                )
                            )
                        }
                    }
                    if (isShowDateDialog.value) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            DialogChoseDate(dayYerChecked = currentNumberYer,
                                times = times ?: emptyList(),
                                dayMonthChecked = currentNumberMonth,
                                dayChecked = currentNumberDay,
                                closeDialog = {
                                    if (!it) dayCheckedNumber(0, 0, 0)
                                    isShowDateDialog.value = false
                                },
                                dayCheckedNumber = { yer, month, day ->
                                    dayCheckedNumber(day, yer, month)
                                },
                                monthChange = { yer, month ->
                                    dayCheckedNumber(
                                        if (month == currentNumberMonth) currentNumberDay else 1,
                                        yer,
                                        month
                                    )
                                })
                        }
                    }
                }
            }
        }
    }

    ShowTimePicker(time, alarmDialogState) {
        time =
            it.hour.toString() + ":" + if (it.minute.toString().length == 1) "0" + it.minute else it.minute
    }
}


@OptIn(ExperimentalTextApi::class)
@Composable
fun ShowTimePicker(
    currentTime: String, dialogState: MaterialDialogState, time: (LocalTime) -> Unit
) {
    MaterialDialog(properties = DialogProperties(dismissOnClickOutside = false),
        border = BorderStroke(2.dp, Brush.horizontalGradient(gradientColors)),
        backgroundColor = MaterialTheme.colorScheme.background,
        dialogState = dialogState,
        buttons = {
            positiveButton(
                text = stringResource(id = R.string.confirmation), textStyle = TextStyle(
                    brush = Brush.verticalGradient(
                        gradientColors
                    ), fontSize = 14.sp
                )
            )
            negativeButton(
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.primary, fontSize = 14.sp
                ), text = stringResource(id = R.string.cancel)
            )
        }) {
        timepicker(
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = Onahau,
                inactiveBackgroundColor = Onahau,
                activeTextColor = Color.Black,
                borderColor = Purple,
                selectorColor = Purple,
                headerTextColor = PurpleGrey
            ),
            title = "  ",
            timeRange = LocalTime.MIDNIGHT..LocalTime.MAX,
            is24HourClock = true,
            initialTime = calculateCurrentTime(currentTime)
        ) { time ->
            time(time)
        }
    }
}

fun calculateCurrentTime(currentTime: String): LocalTime {
    val index = currentTime.indexOf(":")
    val hours = currentTime.subSequence(0, index).toString().toInt()
    val minute = currentTime.subSequence(index.plus(1), currentTime.length).toString().toInt()
    return LocalTime.of(hours, minute, 0)
}
