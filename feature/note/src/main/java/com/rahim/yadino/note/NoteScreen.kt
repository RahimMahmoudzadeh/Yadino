package com.rahim.yadino.note

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahim.yadino.Resource
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ItemListNote
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.dialog.DialogAddNote
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.model.NoteModel

@Composable
internal fun NoteRoute(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = hiltViewModel(),
    openDialog: Boolean,
    clickSearch: Boolean,
    onOpenDialog: (isOpen: Boolean) -> Unit,
) {

//    val notes by viewModel.notes.collectAsStateWithLifecycle()
    NoteScreen(
        modifier = modifier,
        currentYear = viewModel.currentYear,
        currentMonth = viewModel.currentMonth,
        currentDay = viewModel.currentDay,
        currentNameDay = viewModel.nameDay ?: "",
//        notes = notes,
        onUpdateNote = viewModel::updateNote,
        showSampleNote = viewModel::showSampleNote,
        onAddNote = viewModel::addNote,
        onDelete = viewModel::delete,
        onOpenDialog = onOpenDialog,
        onSearchText = viewModel::searchItems,
        openDialog = openDialog,
        clickSearch = clickSearch,
    )
}

@Composable
private fun NoteScreen(
    modifier: Modifier = Modifier,
    currentYear: Int,
    currentMonth: Int,
    currentDay: Int,
    currentNameDay: String,
//    notes: Resource<List<NoteModel>>,
    openDialog: Boolean,
    clickSearch: Boolean,
    onOpenDialog: (isOpen: Boolean) -> Unit,
    onUpdateNote: (NoteModel) -> Unit,
    showSampleNote: (Boolean) -> Unit,
    onAddNote: (NoteModel) -> Unit,
    onDelete: (NoteModel) -> Unit,
    onSearchText: (String) -> Unit,
) {

    val noteDeleteDialog = rememberSaveable { mutableStateOf<NoteModel?>(null) }
    val noteUpdateDialog = rememberSaveable { mutableStateOf<NoteModel?>(null) }
    var searchText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()

    ) {
        ShowSearchBar(clickSearch, searchText = searchText) { search ->
            searchText = search
            onSearchText(searchText)
        }

//        when (notes) {
//            is Resource.Success -> {
//                notes.data?.let {
//                    if (it.isEmpty()) {
//                        if (searchText.isNotEmpty()) {
//                            EmptyMessage(
//                                messageEmpty = com.rahim.yadino.feature.note.R.string.search_empty_note,
//                                painter = com.rahim.yadino.feature.note.R.drawable.empty_note
//                            )
//                        } else {
//                            EmptyMessage(
//                                messageEmpty = com.rahim.yadino.feature.note.R.string.not_note,
//                                painter = com.rahim.yadino.feature.note.R.drawable.empty_note
//                            )
//                        }
//                    } else {
//                        ItemsNote(
//                            notes.data ?: emptyList(),
//                            checkedNote = {
//                                onUpdateNote(it)
//                            },
//                            updateNote = {
//                                if (it.isChecked) {
//                                    Toast.makeText(
//                                        context,
//                                        com.rahim.yadino.feature.note.R.string.not_update_checked_note,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                    return@ItemsNote
//                                }
//                                showSampleNote(true)
//
//                                noteUpdateDialog.value = it
//                                onOpenDialog(true)
//                            },
//                            deleteNote = {
//                                if (it.isChecked) {
//                                    Toast.makeText(
//                                        context,
//                                        com.rahim.yadino.feature.note.R.string.not_removed_checked_note,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                    return@ItemsNote
//                                }
//                                showSampleNote(true)
//                                noteDeleteDialog.value = it
//                            })
//                    }
//                }
//            }
//
//            is Resource.Error -> {
//
//            }
//        }
    }
    DialogAddNote(
        updateNoteState = noteUpdateDialog.value?.state ?: 0,
        updateNoteName = noteUpdateDialog.value?.name ?: "",
        updateNoteDescription = noteUpdateDialog.value?.description ?: "",
        isOpen = openDialog,
        note = { noteName, description, stateNote, timeInMileSecond ->
            if (noteUpdateDialog.value != null) {
                onUpdateNote(noteUpdateDialog.value!!.apply {
                    name = noteName
                    this.description = description
                    state = stateNote
                    this.timeInMileSecond = timeInMileSecond
                })
            } else {
                val noteModel = NoteModel(
                    name = noteName,
                    description = description,
                    state = stateNote,
                    timeInMileSecond = timeInMileSecond,
                    dayNumber = currentDay,
                    yerNumber = currentYear,
                    monthNumber = currentMonth,
                    dayName = currentNameDay,
                )
                onAddNote(noteModel)
            }
        },

        openDialog = {
            noteUpdateDialog.value = null
            onOpenDialog(it)
        })

    noteDeleteDialog.value?.let { noteDelete ->
        ShowDialogDelete(click = {
            noteDeleteDialog.value = null
            if (it) {
                onDelete(noteDelete)
            }
        }, isOpenDialog = noteDeleteDialog.value != null)
    }
}

@Composable
fun ItemsNote(
    notes: List<NoteModel>,
    checkedNote: (NoteModel) -> Unit,
    updateNote: (NoteModel) -> Unit,
    deleteNote: (NoteModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = 16.dp,
                start = 16.dp
            ), contentPadding = PaddingValues(top = 25.dp)
    ) {
        items(items = notes, itemContent = {
            ItemListNote(
                isChecked = it.isChecked,
                stateNote = it.state,
                descriptionNote = it.description,
                nameNote = it.name,
                monthNumber = it.monthNumber ?: 0,
                yerNumber = it.yerNumber ?: 0,
                dayNumber = it.dayNumber ?: 0,
                onChecked = { checked ->
                    checkedNote(it.apply { isChecked = checked })
                },
                openDialogDelete = {
                    deleteNote(it)
                },
                openDialogEdit = {
                    updateNote(it)
                })
        })
    }
}

@Composable
fun ShowDialogDelete(
    modifier: Modifier = Modifier,
    isOpenDialog: Boolean,
    click: (Boolean) -> Unit,
) {
    ErrorDialog(
        modifier,
        isOpenDialog,
        isClickOk = {
            click(it)
        },
        message = stringResource(id = com.rahim.yadino.library.designsystem.R.string.can_you_delete),
        okMessage = stringResource(
            id = com.rahim.yadino.library.designsystem.R.string.ok
        )
    )
}
