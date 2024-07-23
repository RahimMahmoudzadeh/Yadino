package com.rahim.ui.alarmHistory

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rahim.data.modle.Rotin.Routine
import com.rahim.yadino.designsystem.theme.Porcelain
import com.rahim.utils.base.view.gradientColors

@Composable
fun AlarmHistoryCardItem(
    routine: Routine,
) {

    val textUnderLine = if (routine.isChecked) TextDecoration.LineThrough else TextDecoration.None
    val textAlpha =if(routine.isChecked) 0.6f else 1f
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(
            1.dp,
            Brush.verticalGradient(gradientColors)
        ),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = routine.name,
                color = MaterialTheme.colorScheme.primary.copy(textAlpha),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textDecoration = textUnderLine
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "زمان: ${routine.timeHours}  تاریخ: ${"${routine.yerNumber}/${routine.monthNumber}/${routine.dayNumber}"}",
                color = MaterialTheme.colorScheme.secondaryContainer.copy(textAlpha),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                textDecoration = textUnderLine
            )

        }

    }
}