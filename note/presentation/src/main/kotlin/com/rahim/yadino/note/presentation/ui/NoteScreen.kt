package com.rahim.yadino.note.presentation.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.Child
import com.rahim.yadino.base.LoadableComponent
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.dialog.ErrorDialog
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.size.SpaceDimensions
import com.rahim.yadino.note.presentation.R
import com.rahim.yadino.note.presentation.component.NoteComponent
import com.rahim.yadino.note.presentation.component.addNoteDialog.AddNoteDialogComponent
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.ui.addNoteDialog.AddNoteDialog
import com.rahim.yadino.note.presentation.ui.component.ItemListNote
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun NoteRoute(
  modifier: Modifier = Modifier,
  clickSearch: Boolean,
  component: NoteComponent,
  dialogSlot: Child.Created<Any, AddNoteDialogComponent>?,
) {
  val (state, effect, event) = use(component)

  dialogSlot?.let { dialogSlot ->
    dialogSlot.instance.also { dialogComponent ->
      AddNoteDialog(
        component = dialogComponent,
      )
    }
  }
  NoteScreen(
    modifier = modifier,
    state = state,
    onUpdateNote = { updateNote ->
      event(NoteComponent.Event.OnOpenUpdateNoteDialog(updateNote))
    },
    onDelete = {
      event(NoteComponent.Event.Delete(it))
    },
    onSearchText = {
      event(NoteComponent.Event.Search(it))
    },
    onCheckedNote = {
      event(NoteComponent.Event.OnChecked(it))
    },
    clickSearch = clickSearch,
  )
}

@OptIn(FlowPreview::class)
@Composable
private fun NoteScreen(
  modifier: Modifier = Modifier,
  state: NoteComponent.State,
  clickSearch: Boolean,
  onUpdateNote: (note: NoteUiModel) -> Unit,
  onDelete: (NoteUiModel) -> Unit,
  onCheckedNote: (NoteUiModel) -> Unit,
  onSearchText: (NameNoteUi) -> Unit,
) {
  val noteDeleteDialog = rememberSaveable { mutableStateOf<NoteUiModel?>(null) }
  var searchText by rememberSaveable { mutableStateOf("") }

  val context = LocalContext.current
  val size = LocalSize.current
  val space = LocalSpacing.current
  val fontSize = LocalFontSize.current

  LaunchedEffect(Unit) {
    snapshotFlow { searchText }
      .debounce(300)
      .distinctUntilChanged()
      .collect { query ->
        onSearchText(NameNoteUi(name = query))
      }
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    modifier = modifier
      .fillMaxSize(),

    ) {
    ShowSearchBar(clickSearch, searchText = searchText) { search ->
      searchText = search
    }
    LoadableComponent(
      loadableData = state.notes,
      loading = {},
      loaded = { notes ->
        if (notes.isEmpty()) {
          EmptyMessage(
            messageEmpty = if (searchText.isNotEmpty()) R.string.search_empty_note else R.string.not_note,
            painter = R.drawable.empty_note,
            space = space,
            size = size,
            fontSize = fontSize,
          )
        } else {
          ItemsNote(
            notes = notes,
            checkedNote = { note ->
              onCheckedNote(note)
            },
            spaceDimensions = space,
            updateNote = { updateNote ->
              if (updateNote.isChecked) {
                Toast.makeText(
                  context,
                  R.string.not_update_checked_note,
                  Toast.LENGTH_SHORT,
                ).show()
                return@ItemsNote
              }
              onUpdateNote(updateNote)
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
    noteDeleteDialog.value != null -> {
      ErrorDialog(
        modifier,
        isClickOk = {
          noteDeleteDialog.value = null
          if (it) {
            onDelete(noteDeleteDialog.value!!)
          }
        },
        message = stringResource(id = com.rahim.yadino.library.designsystem.R.string.can_you_delete),
        okMessage = stringResource(
          id = com.rahim.yadino.library.designsystem.R.string.ok,
        ),
      )
    }
  }
}

@Composable
fun ItemsNote(
  notes: PersistentList<NoteUiModel>,
  spaceDimensions: SpaceDimensions,
  checkedNote: (NoteUiModel) -> Unit,
  updateNote: (NoteUiModel) -> Unit,
  deleteNote: (NoteUiModel) -> Unit,
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxWidth()
      .padding(
        horizontal = spaceDimensions.space16,
      ),
    contentPadding = PaddingValues(top = spaceDimensions.space24),
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
