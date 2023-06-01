package com.rahim.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    val delete = SwipeAction(
        icon = painterResource(id = R.drawable.delete),
        background = Color.White,
        onSwipe = {
            openDialogDelete(routine)
        }
    )

    val edit = SwipeAction(
        icon = painterResource(id = R.drawable.edit),
        background = Color.White,
        isUndo = true,
        onSwipe = {
            openDialogEdit(routine)
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
                Column(verticalArrangement = Arrangement.SpaceBetween) {
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
                    Row(modifier = Modifier.padding(top = 22.dp, start = 12.dp)) {
                        Text(
                            text = routine.timeHours.toString() + " ",
                            style = TextStyle(fontSize = 13.sp)
                        )
                        Text(
                            text = stringResource(id = R.string.remmeber),
                            style = TextStyle(fontSize = 13.sp)
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = routine.name,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Row(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = if (routine.explanation.isNullOrEmpty()) stringResource(id = R.string.empty) + " " else routine.explanation.toString() + " "
                        )
                        Text(text = stringResource(id = R.string.note))
                    }
                }
            }
        }
    }
}
