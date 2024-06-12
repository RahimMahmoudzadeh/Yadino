package com.rahim.ui.note

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahim.data.modle.note.NoteModel
import com.rahim.ui.dialog.DialogAddNote
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.utils.base.view.ItemListNote
import com.rahim.utils.resours.Resource
import com.rahim.R
import com.rahim.utils.base.view.ShowSearchBar

@Composable
fun NoteRoute(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = hiltViewModel(),
    openDialog: Boolean,
    clickSearch: Boolean,
    onOpenDialog: (isOpen: Boolean) -> Unit,
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    NoteScreen(
        modifier = modifier,
        currentYear = viewModel.currentYear,
        currentMonth = viewModel.currentMonth,
        currentDay = viewModel.currentDay,
        currentNameDay = viewModel.nameDay ?: "",
        notes = notes,
        onUpdateNote = viewModel::updateNote,
        showSampleNote =viewModel::showSampleNote,
        onAddNote =viewModel::addNote,
        onDelete = viewModel::delete,
        onOpenDialog = onOpenDialog,
        onSearchText = viewModel::searchItems,
        openDialog = openDialog,
        clickSearch = clickSearch,
    )
}

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    currentYear: Int,
    currentMonth: Int,
    currentDay: Int,
    currentNameDay: String,
    notes: Resource<List<NoteModel>>,
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
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val searchItems = ArrayList<NoteModel>()



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (notes.data.isNullOrEmpty()) Arrangement.Center else Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = if (notes.data.isNullOrEmpty()) 25.dp else 0.dp
            )
    ) {
        ShowSearchBar(clickSearch, searchText = searchText) { search ->
            searchText = search
            onSearchText(searchText)
        }

        when (notes) {
            is Resource.Loading -> {

            }

            is Resource.Success -> {

                if (notes.data?.isEmpty() == true) {
                    EmptyNote()
                } else {
                    if (searchItems.isEmpty() && searchText.isNotEmpty()) {
                        EmptyNote(
                            Modifier.padding(top = 70.dp),
                            messageEmpty = R.string.search_empty_note
                        )
                    } else {
                        ItemsNote(
                            searchItems.ifEmpty { notes.data as List<NoteModel> },
                            checkedNote = {
                                onUpdateNote(it)
                            },
                            updateNote = {
                                if (it.isChecked) {
                                    Toast.makeText(
                                        context,
                                        R.string.not_update_checked_note,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@ItemsNote
                                }
                                if (it.isSample) showSampleNote(true)

                                noteUpdateDialog.value = it
                            },
                            deleteNote = {
                                if (it.isChecked) {
                                    Toast.makeText(
                                        context,
                                        R.string.not_removed_checked_note,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@ItemsNote
                                }
                                if (it.isSample) showSampleNote(true)

                                noteDeleteDialog.value = it
                            })
                    }
                }
            }

            is Resource.Error -> {

            }
        }
    }
    DialogAddNote(noteUpdate = if (noteUpdateDialog.value != null) noteUpdateDialog.value else null,
        isOpen = openDialog || noteUpdateDialog.value != null,
        note = {
            if (noteUpdateDialog.value != null) {
                onUpdateNote(it)
            } else {
                onAddNote(it)
            }
        },
        currentYer = currentYear,
        currentMonth = currentMonth,
        currentDay = currentDay,
        currentDayName = currentNameDay,
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
fun EmptyNote(modifier: Modifier = Modifier, @StringRes messageEmpty: Int = R.string.not_note) {
    Image(
        modifier = modifier
            .sizeIn(minHeight = 320.dp)
            .fillMaxWidth(),
        painter = painterResource(id = R.drawable.empty_note),
        contentDescription = "empty list home"
    )
    Text(
        text = stringResource(id = messageEmpty),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.primary
    )
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
            ItemListNote(noteModel = it, onChecked = {
                checkedNote(it)
            }, openDialogDelete = {
                deleteNote(it)
            }, openDialogEdit = {
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
        modifier, isOpenDialog, isClickOk = {
            click(it)
        }, message = stringResource(id = R.string.can_you_delete), okMessage = stringResource(
            id = R.string.ok
        )
    )
}