package com.rahim.ui.note

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahim.R
import com.rahim.data.modle.note.NoteModel
import com.rahim.ui.dialog.DialogAddNote
import com.rahim.ui.dialog.DialogDelete
import com.rahim.ui.theme.*
import com.rahim.utils.base.view.gradientColors
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListNote(
    noteModel: NoteModel,
    modifier: Modifier = Modifier,
    onChecked: (NoteModel) -> Unit,
    noteName: (String) -> Unit,
) {
    var openDialogEdit by remember { mutableStateOf(false) }
    var openDialogDelete by remember { mutableStateOf(false) }

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
                        checkedColor = Purple
                    )
                )
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        color = Mantis,
                        text = noteModel.name,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 10.dp),
                        text = "یادداشت : من یک یادداشت هستم برای ویرایش یا حذف من را به چپ یا راست بکشید ...",
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 12.dp, top = 12.dp, bottom = 12.dp),
                text = "1401/1/5",
                fontSize = 12.sp
            )
        }
    }
    ShowDialogEdit(
        isOpenDialog = openDialogEdit,
        noteName = noteModel.name,
        click = { openDialogEdit = it },
        note = { noteName(it) }
    )
    ShowDialogDelete(isOpenDialog = openDialogDelete, click = { openDialogDelete = it })
}

@Composable
fun ShowDialogEdit(
    modifier: Modifier = Modifier,
    isOpenDialog: Boolean,
    noteName: String,
    click: (Boolean) -> Unit,
    note: (String) -> Unit
) {
    DialogAddNote(modifier, isOpen = isOpenDialog, noteName = noteName, openDialog = {
        click(it)
    }, note = {
        note(it)
    })
}

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