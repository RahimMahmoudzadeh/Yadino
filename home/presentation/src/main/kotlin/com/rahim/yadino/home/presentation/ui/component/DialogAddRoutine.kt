package com.rahim.yadino.home.presentation.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.DialogProperties
import com.rahim.yadino.designsystem.component.DialogButtonBackground
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.size.SizeDimensions
import com.rahim.yadino.designsystem.utils.theme.Onahau
import com.rahim.yadino.designsystem.utils.theme.Purple
import com.rahim.yadino.designsystem.utils.theme.PurpleGrey
import com.rahim.yadino.home.presentation.model.RoutineUiModel
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.toPersianDigits
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import saman.zamani.persiandate.PersianDate
import timber.log.Timber
import java.time.LocalTime

const val MAX_NAME_LENGTH = 22
const val MAX_EXPLANATION_LENGTH = 40

@OptIn(
  ExperimentalMaterial3Api::class,
)
@Composable
fun DialogAddRoutine(
  modifier: Modifier = Modifier,
  updateRoutine: RoutineUiModel? = null,
  onCloseDialog: () -> Unit,
  onRoutineCreated: (routine: RoutineUiModel) -> Unit,
) {

  val size = LocalSize.current
  val space = LocalSpacing.current
  val fontSize = LocalFontSize.current

  var routineName by rememberSaveable { mutableStateOf(if (updateRoutine?.name.isNullOrBlank()) "" else updateRoutine.name) }
  var routineExplanation by rememberSaveable { mutableStateOf(if (updateRoutine?.explanation.isNullOrBlank()) "" else updateRoutine.explanation) }
  var time by rememberSaveable { mutableStateOf(if (updateRoutine?.timeHours.isNullOrBlank()) "12:00" else updateRoutine.timeHours) }

  var isErrorName by remember { mutableStateOf(false) }
  var isErrorExplanation by remember { mutableStateOf(false) }
  val alarmDialogState = rememberMaterialDialogState()

  val persianData = PersianDate()
  val date = persianData.initJalaliDate(
    updateRoutine?.yearNumber ?: persianData.shYear,
    updateRoutine?.monthNumber ?: persianData.shMonth,
    updateRoutine?.dayNumber ?: persianData.shDay,
  )
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
        onCloseDialog()
      },
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = space.space16, end = space.space12, start = space.space12, bottom = space.space8),
      ) {
        Text(
          fontSize = fontSize.fontSize18,
          fontWeight = FontWeight.Bold,
          text = stringResource(id = R.string.creat_new_work),
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodyMedium.copy(
            brush = Brush.verticalGradient(
              gradientColors,
            ),
          ),
        )
        TextField(
          modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(top = space.space18)
            .height(size.size60)
            .border(
              width = size.size1,
              brush = Brush.verticalGradient(gradientColors),
              shape = RoundedCornerShape(size.size4),
            ),
          value = routineName,
          onValueChange = {
            isErrorName = it.length >= MAX_NAME_LENGTH
            routineName = if (it.length <= MAX_NAME_LENGTH) it else routineName
          },
          placeholder = {
            Text(
              text = stringResource(id = R.string.name_hint_text_filed_routine),
              color = MaterialTheme.colorScheme.primary,
            )
          },
          colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
            disabledIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
          ),
          textStyle = MaterialTheme.typography.bodyLarge,
        )

        if (isErrorName) {
          Text(
            modifier = Modifier.padding(start = space.space16),
            text = if (routineName.isEmpty()) {
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
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(top = space.space18)
            .height(size.size90)
            .border(
              width = size.size1,
              brush = Brush.verticalGradient(gradientColors),
              shape = RoundedCornerShape(size.size4),
            ),
          value = routineExplanation ?: "",
          onValueChange = {
            isErrorExplanation = it.length >= MAX_EXPLANATION_LENGTH
            routineExplanation =
              if (it.length <= MAX_EXPLANATION_LENGTH) it else routineExplanation
          },
          textStyle = MaterialTheme.typography.bodyMedium,
          placeholder = {
            Text(
              text = stringResource(id = R.string.routine_explanation),
              color = MaterialTheme.colorScheme.primary,
            )
          },
          colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
            disabledIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
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
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier
            .fillMaxWidth()
            .padding(
              top = space.space10,
              start = space.space20,
            ),
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              modifier = Modifier.padding(top = space.space14),
              text = stringResource(id = R.string.set_alarms),
              color = MaterialTheme.colorScheme.primary,
              style = MaterialTheme.typography.bodyMedium,
            )
            Text(
              modifier = Modifier.padding(top = space.space14, start = space.space4),
              text = time?.toPersianDigits() ?: "",
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.SemiBold,
              style = MaterialTheme.typography.bodyMedium,
            )
          }
          OutlinedButton(
            border = BorderStroke(
              size.size1,
              Brush.horizontalGradient(gradientColors),
            ),
            onClick = {
              alarmDialogState.show()
            },
          ) {
            Text(
              text = stringResource(id = R.string.time_change),
              style = MaterialTheme.typography.bodyMedium.copy(
                brush = Brush.verticalGradient(
                  gradientColors,
                ),
              ),
            )
          }
        }
        Spacer(modifier = Modifier.height(size.size22))
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
            size = size,
            space = space,
            onClick = {
              Timber.tag("tagNameRoutine").d(routineName.length.toString())
              if (routineName.isEmpty()) {
                isErrorName = true
              } else {
                val routine = RoutineUiModel(
                  id = updateRoutine?.id,
                  name = routineName,
                  explanation = routineExplanation,
                  timeHours = time,
                  dayNumber = date.shDay,
                  monthNumber = date.shMonth,
                  yearNumber = date.shYear,
                  dayName = date.dayName(),
                  colorTask = null,
                )
                onRoutineCreated(routine)
              }
            },
          )
          Spacer(modifier = Modifier.width(size.size10))
          TextButton(
            onClick = {
              onCloseDialog()
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
  ShowTimePicker(sizeDimensions = size, currentTime = time ?: "", dialogState = alarmDialogState) {
    time = it.toString()
  }
}

@OptIn(ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShowTimePicker(
  currentTime: String,
  sizeDimensions: SizeDimensions,
  dialogState: MaterialDialogState,
  time: (LocalTime) -> Unit,
) {
  MaterialDialog(
    properties = DialogProperties(dismissOnClickOutside = false),
    border = BorderStroke(sizeDimensions.size2, Brush.horizontalGradient(gradientColors)),
    backgroundColor = MaterialTheme.colorScheme.background,
    dialogState = dialogState,
    buttons = {
      positiveButton(
        text = stringResource(id = R.string.confirmation),
        textStyle = TextStyle(
          brush = Brush.verticalGradient(
            gradientColors,
          ),
        ),
      )
      negativeButton(
        textStyle = TextStyle(
          color = MaterialTheme.colorScheme.primary,
        ),
        text = stringResource(id = R.string.cancel),
      )
    },
  ) {
    timepicker(
      colors = TimePickerDefaults.colors(
        activeBackgroundColor = Onahau,
        inactiveBackgroundColor = Onahau,
        activeTextColor = Color.Black,
        borderColor = Purple,
        selectorColor = Purple,
        headerTextColor = PurpleGrey,
      ),
      title = "  ",
      timeRange = LocalTime.MIDNIGHT..LocalTime.MAX,
      is24HourClock = true,
      initialTime = calculateCurrentTime(currentTime),
    ) { time ->
      time(time)
    }
  }
}

private fun calculateCurrentTime(currentTime: String): LocalTime {
  val index = currentTime.indexOf(":")
  val hours = currentTime.subSequence(0, index).toString().toInt()
  val minute = currentTime.subSequence(index.plus(1), currentTime.length).toString().toInt()
  return LocalTime.of(hours, minute, 0)
}
