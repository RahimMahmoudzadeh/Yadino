package com.rahim.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.ui.theme.Porcelain
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.Zircon
import com.rahim.utils.base.view.gradientColors
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemRoutine(
    routine: Routine,
    modifier: Modifier = Modifier,
    onChecked: (Routine) -> Unit,
    openDialogEdit: (Routine) -> Unit,
    openDialogDelete: (Routine) -> Unit,
) {
    val checkBox = rememberSaveable { mutableStateOf(routine.isChecked) }
    val delete = SwipeAction(
        icon = painterResource(id = R.drawable.delete),
        background = MaterialTheme.colorScheme.background,
        onSwipe = {
            openDialogDelete(routine)
        }
    )

    val edit = SwipeAction(
        icon = painterResource(id = R.drawable.edit),
        background = MaterialTheme.colorScheme.background,
        isUndo = true,
        onSwipe = {
            openDialogEdit(routine)
        },
    )
    SwipeableActionsBox(
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.background,
        startActions = listOf(delete),
        endActions = listOf(edit)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            border = if (checkBox.value) BorderStroke(1.dp, color = Porcelain) else BorderStroke(
                1.dp,
                Brush.verticalGradient(gradientColors)
            ),
            onClick = {
                checkBox.value = !routine.isChecked
                onChecked(routine.apply {
                    isChecked = !isChecked
                })
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
                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    Checkbox(
                        checked = checkBox.value,
                        onCheckedChange = {
                            onChecked(routine.apply { isChecked = it })
                        },
                        colors = CheckboxDefaults.colors(
                            uncheckedColor = CornflowerBlueLight,
                            checkedColor = MaterialTheme.colorScheme.background,
                            )
                    )
                    Row(modifier = Modifier.padding(top = 22.dp, start = 12.dp)) {
                        Text(
                            text = routine.timeHours.toString() + " ",
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.remmeber),
                            style = TextStyle(
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = routine.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Row(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = if (routine.explanation.isNullOrEmpty()) stringResource(id = R.string.empty) + " " else routine.explanation.toString() + " ",
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                        Text(
                            text = stringResource(id = R.string.note),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                }
            }
        }
    }
}
