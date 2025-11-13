package com.rahim.yadino.note.presentation.ui.addNoteDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.rahim.yadino.designsystem.component.DialogButtonBackground
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueDark
import com.rahim.yadino.designsystem.utils.theme.Mantis
import com.rahim.yadino.designsystem.utils.theme.Punch
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.model.PriorityNote
import saman.zamani.persiandate.PersianDate
import kotlin.random.Random

const val maxName = 22
const val maxExplanation = 40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteDialog(
  modifier: Modifier = Modifier,
  updateNote: NoteUiModel? = null,
  openDialog: (Boolean) -> Unit,
  setNote: (NoteUiModel) -> Unit,
) {

  val space = LocalSpacing.current
  val size = LocalSize.current
  val fontSize = LocalFontSize.current

  var state by remember { mutableStateOf(updateNote?.state ?: PriorityNote.LOW_PRIORITY) }
  var nameNote by rememberSaveable { mutableStateOf(updateNote?.name ?: "") }
  var description by rememberSaveable { mutableStateOf(updateNote?.description ?: "") }
  val persianDate = PersianDate()
  var isErrorName by remember { mutableStateOf(false) }
  var isErrorExplanation by remember { mutableStateOf(false) }
  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    BasicAlertDialog(
      properties = DialogProperties(
        usePlatformDefaultWidth = false,
        dismissOnClickOutside = false,
      ),
      modifier = modifier
        .fillMaxWidth(0.9f)
        .clip(shape = RoundedCornerShape(size.size8))
        .background(color = MaterialTheme.colorScheme.background)
        .border(
          size.size2,
          brush = Brush.verticalGradient(gradientColors),
          shape = RoundedCornerShape(size.size8),
        ),
      onDismissRequest = {
        openDialog(false)
      },
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = space.space16, end = space.space18, start = space.space18, bottom = space.space8),
      ) {
        Text(
          fontSize = fontSize.fontSize18,
          fontWeight = FontWeight.Bold,
          text = stringResource(id = R.string.creat_new_note),
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodyLarge.copy(
            brush = Brush.verticalGradient(
              gradientColors,
            ),
          ),
        )
        TextField(
          modifier = Modifier
            .padding(top = space.space18)
            .fillMaxWidth()
            .height(size.size58)
            .border(
              width = size.size1,
              brush = Brush.verticalGradient(gradientColors),
              shape = RoundedCornerShape(size.size4),
            ),
          value = nameNote,
          onValueChange = {
            isErrorName = it.length >= maxName
            nameNote = if (it.length <= maxName) it else nameNote
          },
          placeholder = {
            Text(
              text = stringResource(id = R.string.issue),
              color = MaterialTheme.colorScheme.primary,
            )
          },
          textStyle = MaterialTheme.typography.bodyLarge,
          colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
          ),
        )
        if (isErrorName) {
          Text(
            modifier = Modifier.padding(start = space.space16),
            text = if (nameNote.isEmpty()) {
              stringResource(id = R.string.emptyField)
            } else {
              stringResource(
                id = R.string.length_textFiled_name_routine,
              )
            },
            color = MaterialTheme.colorScheme.error,
          )
        }
        TextField(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = space.space18)
            .sizeIn(minHeight = 130.dp)
            .border(
              width = size.size1,
              brush = Brush.horizontalGradient(gradientColors),
              shape = RoundedCornerShape(size.size4),
            ),
          value = description,
          onValueChange = {
            isErrorExplanation = it.length >= maxExplanation
            description = if (it.length <= maxExplanation) it else description
          },
          textStyle = MaterialTheme.typography.bodyMedium,
          placeholder = {
            Text(
              text = stringResource(id = R.string.issue_explanation),
              color = MaterialTheme.colorScheme.primary,
            )
          },
          colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
          ),
        )
        if (isErrorExplanation) {
          Text(
            modifier = Modifier.padding(start = space.space16),
            text = stringResource(id = R.string.length_textFiled_explanation_routine),
            color = MaterialTheme.colorScheme.error,
          )
        }
        Row(
          modifier = Modifier.padding(top = space.space20, start = space.space6, end = space.space4),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Text(
            modifier = Modifier.weight(0.22f),
            style = MaterialTheme.typography.bodyLarge,
            text = stringResource(id = R.string.priority_level),
            color = MaterialTheme.colorScheme.tertiary,
          )
          Row(modifier = Modifier.weight(0.1f)) {
            Text(
              style = MaterialTheme.typography.bodyMedium,
              text = stringResource(id = R.string.up),
              color = Punch,
            )
            RadioButton(
              colors = RadioButtonDefaults.colors(
                selectedColor = Punch,
                unselectedColor = Punch,
              ),
              selected = state == PriorityNote.HIGH_PRIORITY,
              onClick = { state = PriorityNote.HIGH_PRIORITY },
              modifier = Modifier
                .semantics {
                  contentDescription = "Localized Description"
                }
                .size(size.size20)
                .padding(start = space.space8),
            )
          }
          Row(modifier = Modifier.weight(0.2f)) {
            Text(
              modifier = Modifier.padding(start = space.space24),
              text = stringResource(id = R.string.medium),
              color = CornflowerBlueDark,
              style = MaterialTheme.typography.bodyMedium,

              )
            RadioButton(
              colors = RadioButtonDefaults.colors(
                selectedColor = CornflowerBlueDark,
                unselectedColor = CornflowerBlueDark,
              ),
              selected = state == PriorityNote.NORMAL,
              onClick = { state = PriorityNote.NORMAL },
              modifier = Modifier
                .semantics {
                  contentDescription = "Localized Description"
                }
                .size(size.size20)
                .padding(start = space.space8),
            )
          }
          Row(modifier = Modifier.weight(0.13f)) {
            Text(
              fontSize = fontSize.fontSize16,
              modifier = Modifier.padding(start = space.space24),
              text = stringResource(id = R.string.low),
              color = Mantis,
              style = MaterialTheme.typography.bodyMedium,

              )
            RadioButton(
              colors = RadioButtonDefaults.colors(
                selectedColor = Mantis,
                unselectedColor = Mantis,
              ),
              selected = state == PriorityNote.LOW_PRIORITY,
              onClick = { state = PriorityNote.LOW_PRIORITY },
              modifier = Modifier
                .semantics {
                  contentDescription = "Localized Description"
                }
                .size(size.size20)
                .padding(start = space.space8),
            )
          }
        }

        Spacer(modifier = Modifier.height(space.space22))
        Row(
          modifier = Modifier
            .fillMaxWidth(1f)
            .padding(space.space12),
        ) {
          DialogButtonBackground(
            text = stringResource(id = R.string.confirmation),
            gradient = Brush.verticalGradient(gradientColors),
            modifier = Modifier
              .fillMaxWidth(0.3f)
              .height(size.size40),
            textStyle = MaterialTheme.typography.bodyMedium,
            space = space,
            size = size,
            onClick = {
              if (nameNote.isEmpty()) {
                isErrorName = true
              } else {
                setNote(
                  NoteUiModel(
                    id = updateNote?.id ?: Random.nextInt(),
                    name = nameNote,
                    description = description,
                    state = state,
                    isChecked = updateNote?.isChecked ?: false,
                    timeNote = updateNote?.timeNote ?: NoteUiModel.TimeNoteUiModel(
                      monthNumber = persianDate.shMonth,
                      dayNumber = persianDate.shDay,
                      yearNumber = persianDate.shYear,
                      dayName = persianDate.dayName(),
                      timeCreateMillSecond = System.currentTimeMillis(),
                    ),
                  ),
                )
                openDialog(false)
                nameNote = ""
                description = ""
                isErrorExplanation = false
                isErrorName = false
                state = PriorityNote.LOW_PRIORITY
              }
            },
          )

          Spacer(modifier = Modifier.width(size.size10))
          TextButton(
            onClick = {
              nameNote = ""
              description = ""
              state = PriorityNote.LOW_PRIORITY
              isErrorExplanation = false
              isErrorName = false
              openDialog(false)
            },
          ) {
            Text(
              fontSize = fontSize.fontSize16,
              text = stringResource(id = R.string.cancel),
              style = MaterialTheme.typography.bodyMedium.copy(
                brush = Brush.verticalGradient(
                  gradientColors,
                ),
              ),
            )
          }
        }
      }
    }
  }
}

@Preview()
@Composable
fun AddNoteDialogWrapper() {
  AddNoteDialog(
    openDialog = {},
    setNote = { note -> },
  )
}
