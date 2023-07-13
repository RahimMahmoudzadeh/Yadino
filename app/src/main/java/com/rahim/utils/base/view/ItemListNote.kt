package com.rahim.utils.base.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahim.R
import com.rahim.data.modle.note.NoteModel
import com.rahim.ui.theme.*
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListNote(
    noteModel: NoteModel,
    modifier: Modifier = Modifier,
    onChecked: (NoteModel) -> Unit,
    openDialogEdit: (NoteModel) -> Unit,
    openDialogDelete: (NoteModel) -> Unit,
) {

    val delete = SwipeAction(
        icon = painterResource(id = R.drawable.delete),
        background = MaterialTheme.colorScheme.background,
        onSwipe = {
            openDialogDelete(noteModel)
        }
    )

    val edit = SwipeAction(
        icon = painterResource(id = R.drawable.edit),
        background = MaterialTheme.colorScheme.background,
        isUndo = true,
        onSwipe = {
            openDialogEdit(noteModel)
        },
    )
    SwipeableActionsBox(
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.background,
        startActions = listOf(delete),
        endActions = listOf(edit)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            border = if (noteModel.isChecked) BorderStroke(
                1.dp,
                color = Porcelain
            ) else BorderStroke(
                1.dp,
                Brush.verticalGradient(gradientColors)
            ),
            onClick = {
                onChecked(noteModel.apply { isChecked = !isChecked })
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
                    checked = noteModel.isChecked,
                    onCheckedChange = {
                        onChecked(noteModel.apply { isChecked = it })
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = CornflowerBlueLight,
                        checkedColor = MaterialTheme.colorScheme.background
                    )
                )
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        color = if (noteModel.state == 0) Mantis else if (noteModel.state == 1) CornflowerBlueDark else Punch,
                        text = noteModel.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    )
                    Text(
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 10.dp),
                        text = noteModel.description,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 12.dp, top = 12.dp, bottom = 12.dp),
                text = "${noteModel.yerNumber}/${noteModel.monthNumber}/${noteModel.dayNumber}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


