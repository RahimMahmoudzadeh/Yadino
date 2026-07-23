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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.rahim.yadino.designsystem.utils.theme.AppTheme
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.utils.theme.Porcelain
import com.rahim.yadino.enums.RoutineExplanation
import com.rahim.yadino.library.designsystem.Res
import com.rahim.yadino.library.designsystem.delete
import com.rahim.yadino.library.designsystem.edit
import com.rahim.yadino.library.designsystem.explanation
import com.rahim.yadino.library.designsystem.remmeber
import com.rahim.yadino.library.designsystem.routine_left_sample
import com.rahim.yadino.library.designsystem.routine_right_sample
import com.rahim.yadino.toPersianDigits
import com.stevdza_san.swipeable.Swipeable
import com.stevdza_san.swipeable.domain.ActionCustomization
import com.stevdza_san.swipeable.domain.HapticFeedbackConfig
import com.stevdza_san.swipeable.domain.SwipeAction
import com.stevdza_san.swipeable.domain.SwipeBackground
import com.stevdza_san.swipeable.domain.SwipeBehavior
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
  val size = AppTheme.size
  val space = AppTheme.spacing

  val textUnderLine = if (isChecked) TextDecoration.LineThrough else TextDecoration.None

  val delete = SwipeAction(
    customization = ActionCustomization(
      icon = Res.drawable.delete,
      iconColor = MaterialTheme.colorScheme.background,
      containerColor = MaterialTheme.colorScheme.background,
    ),
    onAction = {
      openDialogDelete()
    },
  )

  val edit = SwipeAction(
    customization = ActionCustomization(
      icon = Res.drawable.edit,
      iconColor = MaterialTheme.colorScheme.background,
      containerColor = MaterialTheme.colorScheme.background,
    ),
    onAction = {
      openDialogEdit()
    },
  )
  Swipeable(
    behavior = SwipeBehavior.REVEAL,
    rightRevealActions = listOf(
      delete,
    ),
    leftRevealActions = listOf(
      edit,
    ),
    rightBackground = SwipeBackground.linearGradient(
      colors = listOf(Color.Red, Color.White),
    ),
    hapticFeedbackConfig = HapticFeedbackConfig.Default,
  ) {
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
      border = if (isChecked) {
        BorderStroke(size.size1, color = Porcelain)
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
        .sizeIn(minHeight = 120.dp)
        .padding(bottom = space.space12),
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(end = space.space12),
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
            modifier = Modifier.padding(top = space.space22, start = space.space12),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = timeHoursRoutine.toPersianDigits(),
              style = MaterialTheme.typography.bodySmall,
              textDecoration = textUnderLine,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary,

              )
            Spacer(modifier = Modifier.width(size.size3))
            Text(
              text = stringResource(Res.string.remmeber),
              style = MaterialTheme.typography.bodySmall,
              textDecoration = textUnderLine,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary,

              )
          }
        }
        Column(
          modifier = Modifier
            .padding(top = space.space12)
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
              modifier = Modifier.padding(top = space.space12),
              text = "${stringResource(Res.string.explanation)}: ${
                if (explanationRoutine == RoutineExplanation.ROUTINE_RIGHT_SAMPLE.explanation) {
                  stringResource(Res.string.routine_right_sample)
                } else if (explanationRoutine == RoutineExplanation.ROUTINE_LEFT_SAMPLE.explanation) {
                  stringResource(Res.string.routine_left_sample)
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
