package com.rahim.yadino.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.utils.theme.Porcelain
import com.rahim.yadino.enums.RoutineExplanation
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.persianLocate
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun ItemRoutine(
  modifier: Modifier = Modifier,
  isChecked: Boolean,
  timeHoursRoutine: String,
  nameRoutine: String,
  explanationRoutine: String,
  onChecked: (Boolean) -> Unit,
  openDialogEdit: () -> Unit,
  openDialogDelete: () -> Unit,
) {
  val textUnderLine = if (isChecked) TextDecoration.LineThrough else TextDecoration.None

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
        BorderStroke(1.dp, color = Porcelain)
      } else {
        BorderStroke(
          1.dp,
          Brush.verticalGradient(gradientColors),
        )
      },
      onClick = {
        onChecked(!isChecked)
      },
      modifier = modifier
        .fillMaxWidth()
        .sizeIn(minHeight = 120.dp)
        .padding(bottom = 12.dp),
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(end = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Column(
          verticalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.weight(0.3f),
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
          Row(
            modifier = Modifier.padding(top = 22.dp, start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = timeHoursRoutine.persianLocate(),
              style = MaterialTheme.typography.bodySmall,
              textDecoration = textUnderLine,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary,

            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = stringResource(id = R.string.remmeber),
              style = MaterialTheme.typography.bodySmall,
              textDecoration = textUnderLine,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary,

            )
          }
        }
        Column(
          modifier = Modifier
            .padding(top = 12.dp)
            .weight(0.7f),
          horizontalAlignment = Alignment.End,
        ) {
          Text(
            text = nameRoutine,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = textUnderLine,
            fontWeight = FontWeight.SemiBold,
          )
          if (explanationRoutine.isNotEmpty()) {
            Text(
              modifier = Modifier.padding(top = 12.dp),
              text = "${stringResource(id = R.string.explanation)}: ${
                if (explanationRoutine == RoutineExplanation.ROUTINE_RIGHT_SAMPLE.explanation) {
                  stringResource(id = R.string.routine_right_sample)
                } else if (explanationRoutine == RoutineExplanation.ROUTINE_LEFT_SAMPLE.explanation) {
                  stringResource(id = R.string.routine_left_sample)
                } else {
                  explanationRoutine
                }
              }",
              color = MaterialTheme.colorScheme.secondaryContainer,
              textDecoration = textUnderLine,
              textAlign = TextAlign.End,
              style = MaterialTheme.typography.bodyMedium,

            )
          }
        }
      }
    }
  }
}
