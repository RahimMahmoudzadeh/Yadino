package com.rahim.yadino.note.presentation.component

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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.rahim.yadino.designsystem.component.DialogButtonBackground
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.theme.CornflowerBlueDark
import com.rahim.yadino.designsystem.theme.Mantis
import com.rahim.yadino.designsystem.theme.Punch
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.note.presentation.model.NoteUiModel
import com.rahim.yadino.note.presentation.model.PriorityNote
import saman.zamani.persiandate.PersianDate
import kotlin.random.Random

const val maxName = 22
const val maxExplanation = 40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAddNote(
  modifier: Modifier = Modifier,
  updateNote: NoteUiModel? = null,
  openDialog: (Boolean) -> Unit,
  setNote: (NoteUiModel) -> Unit,
) {

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
        .fillMaxWidth()
        .padding(horizontal = 22.dp)
        .border(
          2.dp,
          brush = Brush.verticalGradient(gradientColors),
          shape = RoundedCornerShape(8.dp),
        ),
      onDismissRequest = {
        openDialog(false)
      },
    ) {
      Surface(
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(percent = 4),
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, end = 18.dp, start = 18.dp, bottom = 8.dp),
        ) {
          Text(
            fontSize = 18.sp,
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
              .padding(top = 18.dp)
              .fillMaxWidth()
              .height(58.dp)
              .border(
                width = 1.dp,
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(4.dp),
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
            ),
          )
          if (isErrorName) {
            Text(
              modifier = Modifier.padding(start = 16.dp),
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
              .padding(top = 18.dp)
              .sizeIn(minHeight = 130.dp)
              .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(gradientColors),
                shape = RoundedCornerShape(4.dp),
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
            ),
          )
          if (isErrorExplanation) {
            Text(
              modifier = Modifier.padding(start = 16.dp),
              text = stringResource(id = R.string.length_textFiled_explanation_routine),
              color = MaterialTheme.colorScheme.error,
            )
          }
          Row(
            modifier = Modifier.padding(top = 20.dp, start = 6.dp, end = 4.dp),
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
                  .size(20.dp)
                  .padding(start = 8.dp),
              )
            }
            Row(modifier = Modifier.weight(0.2f)) {
              Text(
                modifier = Modifier.padding(start = 24.dp),
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
                  .size(20.dp)
                  .padding(start = 8.dp),
              )
            }
            Row(modifier = Modifier.weight(0.13f)) {
              Text(
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 24.dp),
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
                  .size(20.dp)
                  .padding(start = 8.dp),
              )
            }
          }

          Spacer(modifier = Modifier.height(22.dp))
          Row(
            modifier = Modifier
              .fillMaxWidth(1f)
              .padding(12.dp),
          ) {
            DialogButtonBackground(
              text = stringResource(id = R.string.confirmation),
              gradient = Brush.verticalGradient(gradientColors),
              modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(40.dp),
              textSize = 14.sp,
              textStyle = MaterialTheme.typography.bodyMedium,
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
                      timeCreate = System.currentTimeMillis().toString(),
                      timeNote = updateNote?.timeNote ?: NoteUiModel.TimeNoteUiModel(
                        monthNumber = persianDate.shMonth,
                        dayNumber = persianDate.shDay,
                        yearNumber = persianDate.shYear,
                        dayName = persianDate.dayName(),
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

            Spacer(modifier = Modifier.width(10.dp))
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
                fontSize = 16.sp,
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
}

@Preview()
@Composable
fun DialogAddNoteWrapper() {
  DialogAddNote(
    openDialog = {},
    setNote = { note -> },
  )
}
