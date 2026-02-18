package com.rahim.yadino.note.presentation.ui.main

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.rahim.yadino.base.LoadableComponent
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.EmptyMessage
import com.rahim.yadino.designsystem.component.ShowSearchBar
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.size.SpaceDimensions
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.note.presentation.R
import com.rahim.yadino.note.presentation.model.ErrorDialogRemoveNoteUiModel
import com.rahim.yadino.note.presentation.model.NameNoteUi
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.ui.component.ItemListNote
import com.rahim.yadino.note.presentation.ui.main.component.NoteMainComponent
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun NoteMainScreen(modifier: Modifier = Modifier, clickSearch: Boolean, component: NoteMainComponent) {

  val (state, effect, event) = use(component)

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        containerColor = CornflowerBlueLight,
        contentColor = Color.White,
        onClick = {
          event(NoteMainComponent.Event.OnShowAddNoteDialog)
        },
      ) {
        Icon(imageVector = ImageVector.vectorResource(com.rahim.yadino.library.designsystem.R.drawable.ic_add), "add item")
      }
    },
  ) { innerPadding ->
    NoteScreen(
      modifier = modifier.padding(innerPadding),
      state = state,
      onUpdateNote = { updateNote ->
        event(NoteMainComponent.Event.OnOpenUpdateNoteDialog(updateNote))
      },
      onShowErrorDialog = {
        event(NoteMainComponent.Event.ShowErrorRemoveNoteDialog(it))
      },
      onSearchText = {
        event(NoteMainComponent.Event.Search(it))
      },
      onCheckedNote = {
        event(NoteMainComponent.Event.OnChecked(it))
      },
      clickSearch = clickSearch,
    )
  }
}

@OptIn(FlowPreview::class)
@Composable
private fun NoteScreen(
  modifier: Modifier = Modifier,
  state: NoteMainComponent.State,
  clickSearch: Boolean,
  onUpdateNote: (note: NoteUiModel) -> Unit,
  onShowErrorDialog: (errorDialogRemoveNoteUiModel: ErrorDialogRemoveNoteUiModel) -> Unit,
  onCheckedNote: (NoteUiModel) -> Unit,
  onSearchText: (NameNoteUi) -> Unit,
) {
  var searchText by rememberSaveable { mutableStateOf("") }

  val context = LocalContext.current
  val size = LocalSize.current
  val space = LocalSpacing.current
  val fontSize = LocalFontSize.current
  val title = stringResource(com.rahim.yadino.library.designsystem.R.string.can_you_delete)
  val submitTextButton = stringResource(com.rahim.yadino.library.designsystem.R.string.ok)

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
              onShowErrorDialog(
                ErrorDialogRemoveNoteUiModel(title = title, submitTextButton = submitTextButton, noteUiModel = it),
              )
            },
          )
        }
      },
    )
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
