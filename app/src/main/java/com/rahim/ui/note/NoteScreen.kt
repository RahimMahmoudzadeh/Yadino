package com.rahim.ui.note

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahim.R
import com.rahim.data.modle.note.NoteModel
import com.rahim.ui.dialog.DialogAddNote
import com.rahim.ui.dialog.ErrorDialog
import com.rahim.ui.home.EmptyHome
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey
import com.rahim.utils.Constants.YYYY_MM_DD
import com.rahim.utils.base.view.ItemListNote
import com.rahim.utils.base.view.ShowSearchBar
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.extention.calculateTimeFormat
import com.rahim.utils.resours.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    modifier: Modifier = Modifier,
    viewModel: NoteViewModel = hiltViewModel(),
    onClickAdd: Boolean,
    isOpenDialog: (Boolean) -> Unit
) {

    val noteDeleteDialog = rememberSaveable { mutableStateOf<NoteModel?>(null) }
    val noteUpdateDialog = rememberSaveable { mutableStateOf<NoteModel?>(null) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var clickSearch by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val currentYer = viewModel.currentYer
    val currentMonth = viewModel.currentMonth
    val currentDay = viewModel.currentDay
    val currentNameDay by viewModel.flowNameDay.collectAsStateWithLifecycle()
    val searchItems = ArrayList<NoteModel>()

    val notes by viewModel.getNotes().collectAsStateWithLifecycle(
        initialValue = Resource.Success(
            emptyList()
        )
    )

    viewModel.getCurrentNameDay(
        String().calculateTimeFormat(currentYer, currentMonth, currentDay.toString()), YYYY_MM_DD
    )

    Scaffold(
        topBar = {
            TopBarCenterAlign(modifier, stringResource(id = R.string.notes)) {
                clickSearch = !clickSearch && !notes.data.isNullOrEmpty()
            }
        }, containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (notes.data.isNullOrEmpty()) Arrangement.Center else Arrangement.Top,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(
                    top = if (notes.data.isNullOrEmpty()) 25.dp else 0.dp
                )
        ) {
            ShowSearchBar(clickSearch = clickSearch, searchText = searchText) { search ->
                searchText=search
                coroutineScope.launch(Dispatchers.IO) {
                    if (search.isNotEmpty()) {
                        searchItems.clear()
                        notes.data?.filter {
                            it.name.contains(search)
                        }?.let {
                            searchItems.addAll(it)
                        }
                    } else {
                        searchItems.clear()
                    }
                }
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
                                    viewModel.updateNote(it)
                                },
                                updateNote = {
                                    if (it.isChecked){
                                        Toast.makeText(context, R.string.not_update_checked_note, Toast.LENGTH_SHORT).show()
                                        return@ItemsNote
                                    }
                                    if (it.isSample) viewModel.showSampleNote(true)

                                    noteUpdateDialog.value = it
                                },
                                deleteNote = {
                                    if (it.isChecked){
                                        Toast.makeText(context, R.string.not_removed_checked_note, Toast.LENGTH_SHORT).show()
                                        return@ItemsNote
                                    }
                                    if (it.isSample) viewModel.showSampleNote(true)

                                    noteDeleteDialog.value = it
                                })
                        }
                    }
                }

                is Resource.Error -> {

                }
            }
        }
    }
    DialogAddNote(noteUpdate = if (noteUpdateDialog.value != null) noteUpdateDialog.value else null,
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