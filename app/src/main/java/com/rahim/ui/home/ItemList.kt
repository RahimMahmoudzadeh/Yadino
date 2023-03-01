package com.rahim.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.theme.Porcelain
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.Zircon
import com.rahim.utils.base.view.gradientColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemHome(
    routine: Routine,
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheck: (Boolean) -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(containerColor = Zircon),
        border = if (isChecked) BorderStroke(1.dp, color = Porcelain) else BorderStroke(
            1.dp,
            Brush.horizontalGradient(gradientColors)
        ),
        onClick = { onCheck(!isChecked) },
        modifier = modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 120.dp).padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheck(it) },
                colors = CheckboxDefaults.colors(checkedColor = Purple)
            )
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Text(text = routine.name, style = TextStyle(fontWeight = FontWeight.Bold))
            }
        }
    }
}