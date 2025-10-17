package com.rahim.yadino.note.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.rahim.yadino.base.LoadableComponent
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.note.presentation.component.ItemListNote
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.note.presentation.component.DialogAddNote
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.note.presentation.model.NoteUiModel
import kotlinx.collections.immutable.PersistentList

@Composable
internal fun NoteRoute(
  modifier: Modifier = Modifier,
  viewModel: NoteViewModel = hiltViewModel(),
  openDialog: Boolean,
  clickSearch: Boolean,
  onOpenDialog: (isOpen: Boolean) -> Unit,
) {
  val (state, event) = use(viewModel = viewModel)

  NoteScreen(
    modifier = modifier,
    state = state,
    onUpdateNote = {
      event(NoteContract.NoteEvent.UpdateNote(it))
    },
    onAddNote = {
      event(NoteContract.NoteEvent.AddNote(it))
    },
    onDelete = {
      event(NoteContract.NoteEvent.DeleteNote(it))
    },
    onOpenDialog = onOpenDialog,
    onSearchText = {
      event(NoteContract.NoteEvent.SearchNote(it))
    },
    openDialog = openDialog,
    clickSearch = clickSearch,
  )
}

@Composable
private fun NoteScreen(
  modifier: Modifier = Modifier,
  state: NoteContract.NoteState,
  openDialog: Boolean,
  clickSearch: Boolean,
  onOpenDialog: (isOpen: Boolean) -> Unit,
  onUpdateNote: (NoteUiModel) -> Unit,
  onAddNote: (NoteUiModel) -> Unit,
  onDelete: (NoteUiModel) -> Unit,
  onSearchText: (String) -> Unit,
) {
  val noteDeleteDialog = rememberSaveable { mutableStateOf<NoteUiModel?>(null) }
  val noteUpdateDialog = rememberSaveable { mutableStateOf<NoteUiModel?>(null) }
  var searchText by rememberSaveable { mutableStateOf("") }
  val context = LocalContext.current

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    modifier = modifier
      .fillMaxSize(),

    ) {
    ShowSearchBar(clickSearch, searchText = searchText) { search ->
      searchText = search
      onSearchText(searchText)
    }
    LoadableComponent(
      loadableData = state.notes,
      loading = {},
      error = {},
      loaded = { notes ->
        if (notes.isEmpty()) {
          EmptyMessage(
            messageEmpty = if (searchText.isNotEmpty()) R.string.search_empty_note else R.string.not_note,
            painter = R.drawable.empty_note,
          )
        } else {
          ItemsNote(
            notes = notes,
            checkedNote = {
              onUpdateNote(it)
            },
            updateNote = {
              if (it.isChecked) {
                Toast.makeText(
                  context,
                  R.string.not_update_checked_note,
                  Toast.LENGTH_SHORT,
                ).show()
                return@ItemsNote
              }
              noteUpdateDialog.value = it
              onOpenDialog(true)
            },
            deleteNote = {
              if (it.isChecked) {
                Toast.makeText(
                  context,
                  R.string.not_removed_checked_note,
                  Toast.LENGTH_SHORT,
                ).show()
                return@ItemsNote
              }
              noteDeleteDialog.value = it
            },
          )
        }
      },
    )
  }

  when {
    noteUpdateDialog.value != null -> {
      ErrorDialog(
        modifier,
        isClickOk = {
          noteDeleteDialog.value = null
          if (it) {
            onDelete(noteUpdateDialog.value!!)
          }
        },
        message = stringResource(id = com.rahim.yadino.library.designsystem.R.string.can_you_delete),
        okMessage = stringResource(
          id = com.rahim.yadino.library.designsystem.R.string.ok,
        ),
      )
    }

    openDialog -> {
      DialogAddNote(
        updateNote = noteUpdateDialog.value,
        setNote = onAddNote,
        openDialog = {
          noteUpdateDialog.value = null
          onOpenDialog(it)
        },
      )
    }
  }
}

@Composable
fun ItemsNote(
  notes: PersistentList<NoteUiModel>,
  checkedNote: (NoteUiModel) -> Unit,
  updateNote: (NoteUiModel) -> Unit,
  deleteNote: (NoteUiModel) -> Unit,
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxWidth()
      .padding(
        end = 16.dp,
        start = 16.dp,
      ),
    contentPadding = PaddingValues(top = 25.dp),
  ) {
    items(
      items = notes,
      itemContent = {
        ItemListNote(
          isChecked = it.isChecked,
          priorityNote = it.state,
          descriptionNote = it.description,
          nameNote = it.name,
          timeNote = it.timeNote,
          onChecked = { checked ->
            checkedNote(it.copy(isChecked = checked))
          },
          openDialogDelete = {
            deleteNote(it)
          },
          openDialogEdit = {
            updateNote(it)
          },
        )
      },
    )
  }
}

@Composable
fun ShowDialogDelete(
  modifier: Modifier = Modifier,
  isOpenDialog: Boolean,
  click: (Boolean) -> Unit,
) {

}
