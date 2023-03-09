package com.rahim.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.dialog.DialogDelete
import com.rahim.ui.navigation.ShowDialog
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.ui.theme.Porcelain
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.Zircon
import com.rahim.utils.base.view.gradientColors
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemHome(
    routine: Routine,
    modifier: Modifier = Modifier,
    onChecked: (Routine) -> Unit,
    routineName: (String) -> Unit,
) {
    var openDialogEdit by remember { mutableStateOf(false) }
    var openDialogDelete by remember { mutableStateOf(false) }
    val checkedState = remember { mutableStateOf(false) }
    var dayChecked by rememberSaveable { mutableStateOf("شنبه") }

    val delete = SwipeAction(
        icon = painterResource(id = R.drawable.delete),
        background = Color.White,
        onSwipe = {
            openDialogDelete = true
        }
    )

    val edit = SwipeAction(
        icon = painterResource(id = R.drawable.edit),
        background = Color.White,
        isUndo = true,
        onSwipe = {
            openDialogEdit = true
        },
    )
    SwipeableActionsBox(
        backgroundUntilSwipeThreshold = Color.White,
        startActions = listOf(delete),
        endActions = listOf(edit)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Zircon),
            border = if (routine.isChecked) BorderStroke(1.dp, color = Porcelain) else BorderStroke(
                1.dp,
                Brush.verticalGradient(gradientColors)
            ),
            onClick = {
                onChecked(routine.apply { isChecked = !isChecked })
            },
            modifier = modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 120.dp)
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Checkbox(
                    checked = routine.isChecked,
                    onCheckedChange = {
                        onChecked(routine.apply { isChecked = it })
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = CornflowerBlueLight,
                        checkedColor = Purple
                    )
                )
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(text = routine.name, style = TextStyle(fontWeight = FontWeight.Bold))
                }
            }

        }
    }
    ShowDialogEdit(
        isOpenDialog = openDialogEdit,
        routineName = routine.name,
        click = { openDialogEdit = it },
        routine = { routineName(it) }, dayChecked = dayChecked, dayCheckedName = {
            dayChecked = it
        }, checkedAllItemsState = {
            checkedState.value = it
        }, checkedAllDay = checkedState.value
    )
    ShowDialogDelete(isOpenDialog = openDialogDelete, click = { openDialogDelete = it })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDialogEdit(
    modifier: Modifier = Modifier,
    isOpenDialog: Boolean,
    routineName: String,
    click: (Boolean) -> Unit,
    routine: (String) -> Unit,
    dayChecked: String,
    dayCheckedName: (String) -> Unit,
    checkedAllDay: Boolean,
    checkedAllItemsState: (Boolean) -> Unit,
) {
    DialogAddRoutine(modifier, isOpenDialog, routineName = routineName, routine = {
        routine(it)
    }, openDialog = {
        click(it)
    }, dayChecked = dayChecked, dayCheckedName = {
        dayCheckedName(it)
    }, checkedAllDay = checkedAllDay, checkedAllItemsState = {
        checkedAllItemsState(it)
    })
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDialogDelete(
    modifier: Modifier = Modifier,
    isOpenDialog: Boolean,
    click: (Boolean) -> Unit,
) {
    DialogDelete(modifier, isOpenDialog, openDialog = {
        click(it)
    })
}