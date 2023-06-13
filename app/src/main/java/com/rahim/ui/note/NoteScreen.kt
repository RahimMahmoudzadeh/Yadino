package com.rahim.ui.note

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.modle.dialog.StateOpenDialog
import com.rahim.data.modle.note.NoteModel
import com.rahim.ui.dialog.DialogAddNote
import com.rahim.ui.dialog.DialogDelete
import com.rahim.ui.home.HomeViewModel
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.Zircon
import com.rahim.utils.Constants.YYYY_MM_DD
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.extention.calculateTimeFormat
import com.rahim.utils.resours.Resource

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel,
    onClickAdd: Boolean,
    isOpenDialog: (Boolean) -> Unit
) {

    val noteDeleteDialog = rememberSaveable { mutableStateOf<NoteModel?>(null) }
    val noteUpdateDialog = rememberSaveable { mutableStateOf<NoteModel?>(null) }
    val currentYer = viewModel.currentYer
    val currentMonth = viewModel.currentMonth
    val currentDay = viewModel.currentDay
    val currentNameDay by viewModel.flowNameDay.collectAsStateWithLifecycle()

    val notes by viewModel.getNotes().collectAsStateWithLifecycle(
        initialValue = Resource.Success(
            emptyList()
        )
    )

    viewModel.getCurrentNameDay(
        String().calculateTimeFormat(currentYer, currentMonth, currentDay.toString()),
        YYYY_MM_DD
    )

    Scaffold(
        modifier = modifier.background(Zircon),
        topBar = {
            TopBarCenterAlign(
                modifier, stringResource(id = R.string.notes)
            )
        }, backgroundColor = Color.White
    ) {
        when (notes) {
            is Resource.Loading -> {

            }

            is Resource.Success -> {
                if (notes.data?.isEmpty() == true) {
                    EmptyNote(it)
                } else {
                    ItemsNote(it, notes.data as List<NoteModel>, checkedNote = {
                        viewModel.updateNote(it)
                    }, updateNote = {
                        noteUpdateDialog.value = it
                    }, deleteNote = {
                        noteDeleteDialog.value = it
                    })
                }
            }

            is Resource.Error -> {

            }
        }
    }
    DialogAddNote(
        noteUpdate = if (noteUpdateDialog.value != null) noteUpdateDialog.value else null,
        isOpen = onClickAdd || noteUpdateDialog.value != null,
        note = {
            if (noteUpdateDialog.value != null) {
                viewModel.updateNote(it)
            } else {
                viewModel.addNote(it)
            }
        },
        currentYer = currentYer,
        currentMonth = currentMonth,
        currentDay = currentDay,
        currentDayName = currentNameDay,
        openDialog = {
            noteUpdateDialog.value = null
            isOpenDialog(it)
        })

    noteDeleteDialog.value?.let { noteDelete ->
        ShowDialogDelete(click = {
            noteDeleteDialog.value = null
            if (it) {
                viewModel.delete(noteDelete)
            }
        }, isOpenDialog = noteDeleteDialog.value != null)
    }
}

@Composable
fun EmptyNote(paddingValues: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(end = 16.dp, start = 16.dp, top = 25.dp)
    ) {

        Image(
            modifier = Modifier
                .sizeIn(minHeight = 320.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.empty_note),
            contentDescription = "empty list home"
        )
        Text(
            text = stringResource(id = R.string.not_note),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ItemsNote(
    paddingValues: PaddingValues, notes: List<NoteModel>,
    checkedNote: (NoteModel) -> Unit,
    updateNote: (NoteModel) -> Unit,
    deleteNote: (NoteModel) -> Unit
) {
    var note = rememberSaveable { mutableStateOf<NoteModel?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(end = 16.dp, start = 16.dp, top = 25.dp),
        contentPadding = PaddingValues(top = 25.dp)
    ) {
        items(
            items = notes, itemContent = {
                ItemListNote(noteModel = it, onChecked = {
                    checkedNote(it)
                }, note = {
                    note.value = it
                }, openDialogDelete = {
                    deleteNote(it)
                }, openDialogEdit = {
                    updateNote(it)
                })
            }
        )
    }
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