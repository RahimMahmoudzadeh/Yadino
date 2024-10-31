package com.rahim.yadino.designsystem.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.rahim.yadino.model.TimeDate
import com.rahim.yadino.persianLocate
import com.rahim.yadino.designsystem.component.DialogButtonBackground
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.theme.Onahau
import com.rahim.yadino.designsystem.theme.Purple
import com.rahim.yadino.designsystem.theme.PurpleGrey
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.model.RoutineModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import saman.zamani.persiandate.PersianDate
import timber.log.Timber
import java.time.LocalTime
import kotlin.math.exp

const val MAX_NAME_LENGTH = 22
const val MAX_EXPLANATION_LENGTH = 40

@OptIn(
  ExperimentalMaterial3Api::class,
)
@Composable
fun DialogAddRoutine(
  modifier: Modifier = Modifier,
  currentNumberDay: Int,
  currentNumberMonth: Int,
  currentNumberYear: Int,
  updateRoutine: RoutineModel? = null,
  timesMonth: List<TimeDate> = emptyList(),
  openDialog: () -> Unit,
  routineItems: (routine: RoutineModel) -> Unit,
  monthIncrease: ((year: Int, month: Int) -> Unit)? = null,
  monthDecrease: ((year: Int, month: Int) -> Unit)? = null,
) {
  var monthChecked by rememberSaveable { mutableIntStateOf(currentNumberMonth) }
  var yearChecked by rememberSaveable { mutableIntStateOf(currentNumberYear) }
  var dayChecked by rememberSaveable { mutableIntStateOf(currentNumberDay) }

//  monthChecked = currentNumberMonth
//  yearChecked = currentNumberYear
//  dayChecked = currentNumberDay

  var routineName by rememberSaveable { mutableStateOf(if (updateRoutine?.name.isNullOrBlank()) "" else updateRoutine.name) }
  var routineExplanation by rememberSaveable { mutableStateOf(if (updateRoutine?.explanation.isNullOrBlank()) "" else updateRoutine.explanation) }
  var time by rememberSaveable { mutableStateOf(if (updateRoutine?.timeHours.isNullOrBlank()) "12:00" else updateRoutine.timeHours) }

  var isErrorName by remember { mutableStateOf(false) }
  var isShowDateDialog by remember { mutableStateOf(false) }
  var isErrorExplanation by remember { mutableStateOf(false) }
  val alarmDialogState = rememberMaterialDialogState()

  val persianData = PersianDate()
  val date = persianData.initJalaliDate(
    updateRoutine?.yearNumber ?: yearChecked,
    updateRoutine?.monthNumber ?: monthChecked,
    updateRoutine?.dayNumber ?: dayChecked,
  )

  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    BasicAlertDialog(
      properties = DialogProperties(
        usePlatformDefaultWidth = false, dismissOnClickOutside = false,
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
        openDialog()
      },
    ) {
      Surface(
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(percent = 4),
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, end = 12.dp, start = 12.dp, bottom = 8.dp),
        ) {
          Text(
            fontSize = 18.sp,
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
              .padding(top = 18.dp)
              .height(60.dp)
              .border(
                width = 1.dp,
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(4.dp),
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
            ),
            textStyle = MaterialTheme.typography.bodyLarge,
          )

          if (isErrorName) {
            Text(
              modifier = Modifier.padding(start = 16.dp),
              text = if (routineName.isEmpty()) stringResource(id = R.string.emptyField) else stringResource(
                id = R.string.length_textFiled_name_routine,
              ),
              color = MaterialTheme.colorScheme.error,
            )
          }
          TextField(
            modifier = Modifier
              .background(MaterialTheme.colorScheme.background)
              .fillMaxWidth()
              .padding(top = 18.dp)
              .height(90.dp)
              .border(
                width = 1.dp,
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(4.dp),
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
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
              .fillMaxWidth()
              .padding(
                top = 10.dp,
                start = 20.dp,
              ),
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                modifier = Modifier.padding(top = 14.dp),
                text = stringResource(id = R.string.set_alarms),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
              )
              Text(
                modifier = Modifier.padding(top = 14.dp, start = 4.dp),
                text = time?.persianLocate() ?: "",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
              )
            }
            OutlinedButton(
              border = BorderStroke(
                1.dp, Brush.horizontalGradient(gradientColors),
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
          if (timesMonth.isNotEmpty()) {
            Row(
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier
                .fillMaxWidth()
                .padding(
                  top = if (timesMonth.isNotEmpty()) 0.dp else 10.dp,
                  start = 20.dp,
                ),
            ) {
              Text(
                modifier = Modifier.padding(top = 14.dp),
                text = stringResource(id = R.string.set_reminder),
                color = MaterialTheme.colorScheme.primary,
              )
              OutlinedButton(
                border = BorderStroke(
                  1.dp, Brush.horizontalGradient(gradientColors),
                ),
                onClick = { isShowDateDialog = true },
              ) {
                Text(
                  text = stringResource(id = R.string.data_change),
                  style = TextStyle(brush = Brush.verticalGradient(gradientColors)),
                )
              }
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
                Timber.tag("tagNameRoutine").d(routineName.length.toString())

                if (routineName.isEmpty()) {
                  isErrorName = true
                } else {
                  val routine = RoutineModel(
                    name = routineName,
                    explanation = routineExplanation,
                    timeHours = time,
                    dayNumber = dayChecked,
                    monthNumber = monthChecked,
                    yearNumber = yearChecked,
                    dayName = persianData.dayName(date),
                    colorTask = null,
                  )
                  routineItems(routine)
                }
              },
            )
            Spacer(modifier = Modifier.width(10.dp))
            TextButton(
              onClick = {
                openDialog()
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
          if (isShowDateDialog) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
              DialogChoseDate(
                times = timesMonth,
                yearNumber = yearChecked,
                monthNumber = monthChecked,
                dayNumber = dayChecked,
                closeDialog = {
                  isShowDateDialog = false
                },
                dayCheckedNumber = { year, month, day ->
                  dayChecked = day
                  monthChecked = month
                  yearChecked = year
                },
                monthIncrease = { year, month ->
                  monthIncrease?.invoke(year, month)
                },
                monthDecrease = { year, month ->
                  monthDecrease?.invoke(year, month)
                },
              )
            }
          }
        }
      }
    }
  }

  ShowTimePicker(time ?: "", alarmDialogState) {
    time = it.toString()
  }
}


@OptIn(ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShowTimePicker(
  currentTime: String, dialogState: MaterialDialogState, time: (LocalTime) -> Unit,
) {
  MaterialDialog(
    properties = DialogProperties(dismissOnClickOutside = false),
    border = BorderStroke(2.dp, Brush.horizontalGradient(gradientColors)),
    backgroundColor = MaterialTheme.colorScheme.background,
    dialogState = dialogState,
    buttons = {
      positiveButton(
        text = stringResource(id = R.string.confirmation),
        textStyle = TextStyle(
          brush = Brush.verticalGradient(
            gradientColors,
          ),
          fontSize = 14.sp,
        ),
      )
      negativeButton(
        textStyle = TextStyle(
          color = MaterialTheme.colorScheme.primary, fontSize = 14.sp,
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

fun calculateCurrentTime(currentTime: String): LocalTime {
  val index = currentTime.indexOf(":")
  val hours = currentTime.subSequence(0, index).toString().toInt()
  val minute = currentTime.subSequence(index.plus(1), currentTime.length).toString().toInt()
  return LocalTime.of(hours, minute, 0)
}
