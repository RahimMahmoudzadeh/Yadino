package com.rahim.yadino.note.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueDark
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.utils.theme.Mantis
import com.rahim.yadino.designsystem.utils.theme.Porcelain
import com.rahim.yadino.designsystem.utils.theme.Punch
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.model.PriorityNote
import com.rahim.yadino.toPersianDigits
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListNote(
  modifier: Modifier = Modifier,
  onChecked: (Boolean) -> Unit,
  isChecked: Boolean,
  priorityNote: PriorityNote,
  nameNote: String,
  descriptionNote: String,
  timeNote: NoteUiModel.TimeNoteUiModel,
  openDialogEdit: () -> Unit,
  openDialogDelete: () -> Unit,
) {
  val space = LocalSpacing.current
  val size = LocalSize.current

  val textUnderLine = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
  val date by remember { mutableStateOf("${timeNote.yearNumber}/${timeNote.monthNumber}/${timeNote.dayNumber}") }

  val delete = SwipeAction(
    icon = painterResource(id = R.drawable.delete),
    background = MaterialTheme.colorScheme.background,
    onSwipe = {
      openDialogDelete()
    },
  )

  val edit = SwipeAction(
    icon = painterResource(id = R.drawable.edit),
    background = MaterialTheme.colorScheme.background,
    isUndo = true,
    onSwipe = {
      openDialogEdit()
    },
  )
  SwipeableActionsBox(
    backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.background,
    startActions = listOf(delete),
    endActions = listOf(edit),
  ) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
      border = if (isChecked) {
        BorderStroke(
          size.size1,
          color = Porcelain,
        )
      } else {
        BorderStroke(
          size.size1,
          Brush.verticalGradient(gradientColors),
        )
      },
      onClick = {
        onChecked(!isChecked)
      },
      modifier = modifier
        .fillMaxWidth()
        .sizeIn(minHeight = size.size120)
        .padding(bottom = space.space12),
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(end = space.space12),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Checkbox(
          checked = isChecked,
          onCheckedChange = {
            onChecked(!isChecked)
          },
          colors = CheckboxDefaults.colors(
            uncheckedColor = CornflowerBlueLight,
            checkedColor = MaterialTheme.colorScheme.background,
          ),
        )
        Column(modifier = Modifier.padding(top = space.space12)) {
          Text(
            modifier = Modifier.align(Alignment.End),
            color = if (priorityNote == PriorityNote.HIGH_PRIORITY) Punch else if (priorityNote == PriorityNote.NORMAL) CornflowerBlueDark else Mantis,
            text = nameNote,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textDecoration = textUnderLine,
          )
          Text(
            textAlign = TextAlign.End,
            modifier = Modifier
              .align(Alignment.End)
              .padding(top = space.space10),
            text = descriptionNote,
            color = MaterialTheme.colorScheme.secondaryContainer,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            textDecoration = textUnderLine,
          )
        }
      }
      Text(
        modifier = Modifier
          .align(Alignment.Start)
          .padding(start = space.space12, top = space.space12, bottom = space.space12),
        text = date.toPersianDigits(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        textDecoration = textUnderLine,
      )
    }
  }
}
