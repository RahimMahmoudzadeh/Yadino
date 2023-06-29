package com.rahim.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.rahim.R
import com.rahim.data.modle.note.NoteModel
import com.rahim.ui.theme.*
import com.rahim.utils.base.view.DialogButtonBackground
import com.rahim.utils.base.view.gradientColors
import com.rahim.utils.enums.StateNote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAddNote(
    modifier: Modifier = Modifier,
    noteUpdate: NoteModel?,
    isOpen: Boolean,
    currentDay:Int,
    currentMonth:Int,
    currentYer:Int,
    currentDayName:String,
    openDialog: (Boolean) -> Unit,
    note: (NoteModel) -> Unit
) {
    var state by remember { mutableStateOf(0) }
    var nameNote by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    if (noteUpdate != null) {
        state = noteUpdate.state
        nameNote = noteUpdate.name
        description = noteUpdate.description
    }

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
                        brush = Brush.verticalGradient(gradientColors),
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
                            .padding(top = 16.dp, end = 18.dp, start = 18.dp, bottom = 8.dp)
                    ) {
                        Text(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            text = stringResource(id = R.string.creat_new_note),
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
                            value = nameNote,
                            onValueChange = {
                                nameNote = it
                            },
                            placeholder = { Text(text = stringResource(id = R.string.issue)) },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        TextField(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(top = 18.dp)
                                .sizeIn(minHeight = 130.dp)
                                .border(
                                    width = 1.dp,
                                    brush = Brush.horizontalGradient(gradientColors),
                                    shape = RoundedCornerShape(4.dp)
                                ),
                            value = description,
                            onValueChange = {
                                description = it
                            },
                            placeholder = { Text(text = stringResource(id = R.string.issue_explanation)) },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                        Row(modifier = Modifier.padding(top = 20.dp, start = 12.dp, end = 10.dp)) {
                            Text(
                                fontSize = 18.sp,
                                text = stringResource(id = R.string.priority_level),
                                color = Gigas,
                            )
                            Text(
                                fontSize = 16.sp,
                                text = stringResource(id = R.string.up),
                                modifier = Modifier.padding(start = 24.dp), color = Punch
                            )
                            RadioButton(
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Punch,
                                    unselectedColor = Punch
                                ),
                                selected = state == 2,
                                onClick = { state = 2 },
                                modifier = Modifier
                                    .semantics { contentDescription = "Localized Description" }
                                    .size(20.dp)
                                    .padding(start = 8.dp)
                            )
                            Text(
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 24.dp),
                                text = stringResource(id = R.string.medium),
                                color = CornflowerBlueDark
                            )
                            RadioButton(
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = CornflowerBlueDark,
                                    unselectedColor = CornflowerBlueDark
                                ),
                                selected = state == 1,
                                onClick = { state = 1 },
                                modifier = Modifier
                                    .semantics { contentDescription = "Localized Description" }
                                    .size(20.dp)
                                    .padding(start = 8.dp)
                            )
                            Text(
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 24.dp),
                                text = stringResource(id = R.string.low), color = Mantis
                            )
                            RadioButton(
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Mantis,
                                    unselectedColor = Mantis
                                ),
                                selected = state == 0,
                                onClick = { state = 0 },
                                modifier = Modifier
                                    .semantics { contentDescription = "Localized Description" }
                                    .size(20.dp)
                                    .padding(start = 8.dp)
                            )
                        }

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
                                onClick = {
                                    note(
                                        if (noteUpdate != null) {
                                            NoteModel(
                                                id = noteUpdate.id,
                                                name = nameNote,
                                                description = description,
                                                state = state,
                                                dayName = currentDayName,
                                                yerNumber = currentYer,
                                                monthNumber = currentMonth,
                                                dayNumber = currentDay
                                            )
                                        } else {
                                            NoteModel(
                                                name = nameNote,
                                                description = description,
                                                state = state,
                                                dayName = currentDayName,
                                                yerNumber = currentYer,
                                                monthNumber = currentMonth,
                                                dayNumber = currentDay
                                            )
                                        }
                                    )
                                    openDialog(false)
                                    nameNote = ""
                                    description = ""
                                    state = 0
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            TextButton(onClick = {
                                nameNote = ""
                                description = ""
                                state = 0
                                openDialog(false)
                            }) {
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

}
