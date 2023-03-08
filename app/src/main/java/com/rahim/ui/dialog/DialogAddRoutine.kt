package com.rahim.ui.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalTextApi::class,
)
@Composable
fun DialogAddRoutine(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    routineName: String?,
    openDialog: (Boolean) -> Unit,
    routine: (String) -> Unit
) {
    val checkedState = remember { mutableStateOf(false) }
    val alarmDialogState = rememberMaterialDialogState()

    val gradientColors = listOf(Purple, PurpleGrey)
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        if (isOpen) {
            AlertDialog(properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = false
            ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .border(
                        1.dp,
                        brush = Brush.verticalGradient(com.rahim.utils.base.view.gradientColors),
                        shape = RoundedCornerShape(8.dp)
                    ),
                onDismissRequest = {
                    openDialog(false)
                }) {
                Surface(
                    color = Color.White,
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
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(top = 18.dp)
                                .height(52.dp)
                                .border(
                                    width = 1.dp,
                                    brush = Brush.verticalGradient(gradientColors),
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            value = routineName.toString(),
                            onValueChange = {
                                routine(it)
                            },
                            placeholder = { Text(text = stringResource(id = R.string.name_hint_text_filed_routine)) },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        Row(modifier = Modifier.padding(top = 4.dp, start = 3.dp, end = 3.dp)) {
                            ClickableText(
                                onClick = {},
                                text = AnnotatedString(stringResource(id = R.string.saturday)),
                                modifier = Modifier
                                    .padding(top = 14.dp, start = 4.dp)
                                    .drawBehind {
                                        drawCircle(
                                            brush = Brush.verticalGradient(gradientColors),
                                            radius = 60f
                                        )
                                    },
                                style = TextStyle(color = Color.White)
                            )
                            ClickableText(
                                onClick = {},
                                text = AnnotatedString(stringResource(id = R.string.sunday)),
                                modifier = Modifier.padding(top = 14.dp, start = 17.dp)
                            )
                            ClickableText(
                                onClick = {},
                                text = AnnotatedString(stringResource(id = R.string.monday)),
                                modifier = Modifier.padding(top = 14.dp, start = 17.dp)
                            )
                            ClickableText(
                                onClick = {},
                                text = AnnotatedString(stringResource(id = R.string.tuesday)),
                                modifier = Modifier.padding(top = 14.dp, start = 17.dp)
                            )
                            ClickableText(
                                onClick = {},
                                text = AnnotatedString(stringResource(id = R.string.wednesday)),
                                modifier = Modifier.padding(top = 14.dp, start = 17.dp)
                            )
                            ClickableText(
                                onClick = {},
                                text = AnnotatedString(stringResource(id = R.string.thursday)),
                                modifier = Modifier.padding(top = 14.dp, start = 17.dp)
                            )
                            ClickableText(
                                onClick = {},
                                text = AnnotatedString(stringResource(id = R.string.friday)),
                                modifier = Modifier.padding(top = 14.dp, start = 17.dp)
                            )
                            ClickableText(
                                onClick = {},
                                text = AnnotatedString(stringResource(id = R.string.all)),
                                modifier = Modifier.padding(top = 14.dp, start = 17.dp)
                            )
                            Checkbox(
                                modifier = Modifier.padding(start = 10.dp),
                                checked = checkedState.value,
                                onCheckedChange = { checkedState.value = it },
                                colors = CheckboxDefaults.colors(checkedColor = Purple)
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 15.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 14.dp),
                                text = stringResource(id = R.string.set_alarms)
                            )
                            OutlinedButton(border = BorderStroke(
                                1.dp,
                                Brush.horizontalGradient(gradientColors)
                            ), onClick = { alarmDialogState.show() }) {
                                Text(
                                    text = stringResource(id = R.string.time_change),
                                    style = TextStyle(brush = Brush.verticalGradient(gradientColors))
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
                            DialogButtonBackground(
                                text = stringResource(id = R.string.confirmation),
                                gradient = Brush.verticalGradient(gradientColors),
                                modifier = Modifier,
                                textSize = 14.sp,
                                width = 0.3f,
                                height = 40.dp,
                                onClick = { openDialog(false) }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            TextButton(onClick = { openDialog(false) }) {
                                Text(
                                    fontSize = 16.sp,
                                    text = stringResource(id = R.string.cancel),
                                    style = TextStyle(brush = Brush.verticalGradient(gradientColors))
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    ShowTimePicker(alarmDialogState)
}

@OptIn(ExperimentalTextApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowTimePicker(dialogState: MaterialDialogState) {
    MaterialDialog(
        properties = DialogProperties(dismissOnClickOutside = false),
        border = BorderStroke(1.dp, Brush.horizontalGradient(gradientColors)),
        dialogState = dialogState,
        buttons = {
            positiveButton(
                text =
                stringResource(id = R.string.confirmation), textStyle = TextStyle(
                    brush = Brush.verticalGradient(
                        gradientColors
                    ), fontSize = 14.sp
                )
            )
            negativeButton(
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                text = stringResource(id = R.string.cancel)
            )
        }
    ) {
        timepicker(
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = Onahau,
                inactiveBackgroundColor = Onahau,
                activeTextColor = Color.Black,
                borderColor = Purple,
                selectorColor = Purple,
                headerTextColor = PurpleGrey
            ),
            initialTime = LocalTime.NOON,
            title = stringResource(id = R.string.time),
            timeRange = LocalTime.MIDNIGHT..LocalTime.NOON
        ) { time ->
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun DialogAddRoutinePreview() {
    YadinoTheme() {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            DialogAddRoutine(isOpen = true, routine = {

            }, openDialog = {}, routineName = "")
        }
    }
}