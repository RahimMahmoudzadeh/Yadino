package com.rahim.yadino.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.theme.Porcelain
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.persianLocate

@Composable
fun AlarmHistoryCardItem(
  routineIsChecked: Boolean,
  routineName: String,
  routineTimeHours: String,
  routineYearNumber: Int,
  routineMonthNumber: Int,
  routineDayNumber: Int,
) {
  val textUnderLine = if (routineIsChecked) TextDecoration.LineThrough else TextDecoration.None
  val textAlpha = if (routineIsChecked) 0.6f else 1f
  val date = "$routineYearNumber/$routineMonthNumber/$routineDayNumber".persianLocate()

  val space= LocalSpacing.current
  val size= LocalSize.current

  Card(
    elevation = CardDefaults.elevatedCardElevation(4.dp),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = space.space6, vertical = space.space4),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    border = if (routineIsChecked) {
      BorderStroke(size.size1, color = Porcelain)
    } else {
      BorderStroke(
        size.size1,
        Brush.verticalGradient(gradientColors),
      )
    },
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = space.space14, vertical = space.space18),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
        text = "${stringResource(id = R.string.time)}: ${
          routineTimeHours.persianLocate()
        }  ${stringResource(id = R.string.date)}: $date",
        color = MaterialTheme.colorScheme.secondaryContainer.copy(textAlpha),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        textDecoration = textUnderLine,
      )
      Spacer(modifier = Modifier.width(size.size8))
      Text(
        text = routineName,
        color = MaterialTheme.colorScheme.primary.copy(textAlpha),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textDecoration = textUnderLine,
      )
    }
  }
}
