package com.rahim.ui.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.theme.*
import com.rahim.utils.base.view.DialogButtonBackground
import com.rahim.utils.base.view.gradientColors
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.util.*

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun DialogAddRoutine(
    modifier: Modifier = Modifier,
    dayChecked: String,
    isOpen: Boolean,
    isShowDay: Boolean,
    currentNumberDay: Int,
    currentNumberMonth: Int,
    currentNumberYer: Int,
    routineUpdate: Routine? = null,
    openDialog: (Boolean) -> Unit,
    routine: (Routine) -> Unit,
) {
    var routineName by rememberSaveable { mutableStateOf("") }
    var routineExplanation by rememberSaveable { mutableStateOf("") }
    val checkedStateAllDay = remember { mutableStateOf(false) }
    val isErrorName = remember { mutableStateOf(false) }
    val isErrorRoutine = remember { mutableStateOf(false) }
    val time = rememberSaveable { mutableStateOf("12:00") }
    val alarmDialogState = rememberMaterialDialogState()

    if (routineUpdate != null) {
        routineName = routineUpdate.name
        time.value = routineUpdate.timeHours.toString()
        routineUpdate.explanation?.let {
            routineExplanation = it
        }
    }
    val dayWeek = stringArrayResource(id = R.array.day_weeks)

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        if (isOpen) {
            AlertDialog(properties = DialogProperties(
                usePlatformDefaultWidth = false, dismissOnClickOutside = false
            ), modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .border(
                    1.dp,
                    brush = Brush.verticalGradient(gradientColors),
                    shape = RoundedCornerShape(8.dp)
                ), onDismissRequest = {
                openDialog(false)
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
                        TextField(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .fillMaxWidth()
                                .padding(top = 18.dp)
                                .height(52.dp)
                                .border(
                                    width = 1.dp,
                                    brush = Brush.verticalGradient(gradientColors),
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            value = routineName,
                            onValueChange = {
                                isErrorName.value = it.length > 22
                                routineName = it
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.name_hint_text_filed_routine),
                                    style = TextStyle(color = MaterialTheme.colorScheme.primary)
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
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
                                isErrorRoutine.value = it.length > 40
                                routineExplanation = it
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(id = R.string.routine_explanation),
                                    style = TextStyle(color = MaterialTheme.colorScheme.primary)
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        if (isErrorRoutine.value) {
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
                                            .background(
                                                brush = if (checkedStateAllDay.value || dayChecked == dayName) {
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
                            Row() {
                                Text(
                                    modifier = Modifier.padding(top = 14.dp),
                                    text = stringResource(id = R.string.set_alarms),
                                    style = TextStyle(color = MaterialTheme.colorScheme.primary)
                                )
                                Text(
                                    modifier = Modifier.padding(top = 14.dp, start = 4.dp),
                                    text = time.value,
                                    style = TextStyle(color = MaterialTheme.colorScheme.primary)
                                )
                            }
                            OutlinedButton(border = BorderStroke(
                                1.dp, Brush.horizontalGradient(gradientColors)
                            ), onClick = {
                                alarmDialogState.show()
                            }) {
                                Text(
                                    text = stringResource(id = R.string.time_change),
                                    style = TextStyle(
                                        brush = Brush.verticalGradient(
                                            gradientColors
                                        )
                                    )
                                )
                            }
                        }
                        //TODO for data
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            modifier = Modifier.padding(top = 14.dp),
//                            text = stringResource(id = R.string.set_reminder)
//                        )
//                        OutlinedButton(
//                            border = BorderStroke(
//                                1.dp,
//                                Brush.horizontalGradient(gradientColors)
//                            ), onClick = { /*TODO*/ }) {
//                            Text(
//                                text = stringResource(id = R.string.data_change),
//                                style = TextStyle(brush = Brush.verticalGradient(gradientColors))
//                            )
//                        }
//                    }
                        Spacer(modifier = Modifier.height(22.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(12.dp)
                        ) {
                            DialogButtonBackground(text = stringResource(id = R.string.confirmation),
                                gradient = Brush.verticalGradient(gradientColors),
                                modifier = Modifier,
                                textSize = 14.sp,
                                width = 0.3f,
                                height = 40.dp,
                                onClick = {
                                    if (routineName.isEmpty()) {
                                        isErrorName.value = true
                                    } else {
                                        routine(routineUpdate?.apply {
                                            name = routineName
                                            timeHours = time.value
                                            explanation = routineExplanation
                                        } ?: Routine(
                                            routineName,
                                            null,
                                            dayChecked,
                                            currentNumberDay,
                                            currentNumberMonth,
                                            currentNumberYer,
                                            time.value,
                                            explanation = routineExplanation
                                        ))
                                        routineName = ""
                                        routineExplanation = ""
                                        openDialog(false)
                                    }
                                })
                            Spacer(modifier = Modifier.width(10.dp))
                            TextButton(onClick = {
                                routineName = ""
                                routineExplanation = ""
                                time.value = "12:00"
                                openDialog(false)
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
                    }
                }
            }
        }
    }

    ShowTimePicker(alarmDialogState) {
        time.value =
            it.hour.toString() + ":" + if (it.minute.toString().length == 1) "0" + it.minute else it.minute
    }
}


@OptIn(ExperimentalTextApi::class)
@Composable
fun ShowTimePicker(dialogState: MaterialDialogState, time: (LocalTime) -> Unit) {
    MaterialDialog(properties = DialogProperties(dismissOnClickOutside = false),
        border = BorderStroke(1.dp, Brush.horizontalGradient(gradientColors)),
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
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                text = stringResource(id = R.string.cancel)
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
            title = stringResource(id = R.string.time),
            timeRange = LocalTime.MIDNIGHT..LocalTime.MAX,
            is24HourClock = true
        ) { time ->
            time(time)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun DialogAddRoutinePreview() {
    YadinoTheme() {
//        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
//            DialogAddRoutine(isOpen = true, routine = {
//
//            }, openDialog = {}, routineName = "")
//        }
    }
}