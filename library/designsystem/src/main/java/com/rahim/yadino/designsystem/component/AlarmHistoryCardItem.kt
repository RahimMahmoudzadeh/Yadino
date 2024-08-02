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
import com.rahim.yadino.base.persianLocate
import com.rahim.yadino.library.designsystem.R

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
    val date ="${routineYearNumber}/${routineMonthNumber}/${routineDayNumber}".persianLocate()
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
                text = routineName,
                color = MaterialTheme.colorScheme.primary.copy(textAlpha),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textDecoration = textUnderLine
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${stringResource(id = R.string.time)}: ${
                    routineTimeHours.persianLocate()
                }  ${stringResource(id = R.string.date)}: $date",
                color = MaterialTheme.colorScheme.secondaryContainer.copy(textAlpha),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                textDecoration = textUnderLine
            )

        }

    }
}