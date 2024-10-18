package com.rahim.yadino.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.rahim.yadino.persianLocate
import com.rahim.yadino.designsystem.theme.CornflowerBlueDark
import com.rahim.yadino.designsystem.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.theme.Mantis
import com.rahim.yadino.designsystem.theme.Porcelain
import com.rahim.yadino.designsystem.theme.Punch
import com.rahim.yadino.library.designsystem.R
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListNote(
    modifier: Modifier = Modifier,
    onChecked: (Boolean) -> Unit,
    isChecked:Boolean,
    stateNote:Int,
    nameNote:String,
    descriptionNote:String,
    monthNumber:Int,
    yearNumber:Int,
    dayNumber:Int,
    openDialogEdit: () -> Unit,
    openDialogDelete: () -> Unit,
) {
    val textUnderLine = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
    val date ="${yearNumber}/${monthNumber}/${dayNumber}"
    val delete = SwipeAction(
        icon = painterResource(id = R.drawable.delete),
        background = MaterialTheme.colorScheme.background,
        onSwipe = {
            openDialogDelete()
        }
    )

    val edit = SwipeAction(
        icon = painterResource(id = R.drawable.edit),
        background = MaterialTheme.colorScheme.background,
        isUndo = true,
        onSwipe = {
            openDialogEdit()
        },
    )
    SwipeableActionsBox(
        backgroundUntilSwipeThreshold = MaterialTheme.colorScheme.background,
        startActions = listOf(delete),
        endActions = listOf(edit)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            border = if (isChecked) BorderStroke(
                1.dp,
                color = Porcelain
            ) else BorderStroke(
                1.dp,
                Brush.verticalGradient(gradientColors)
            ),
            onClick = {
                onChecked(!isChecked )
            },
            modifier = modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 120.dp)
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        onChecked(!isChecked)
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = CornflowerBlueLight,
                        checkedColor = MaterialTheme.colorScheme.background
                    )
                )
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        color = if (stateNote == 0) Mantis else if (stateNote == 1) CornflowerBlueDark else Punch,
                        text = nameNote,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        textDecoration = textUnderLine
                    )
                    Text(
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 10.dp),
                        text = descriptionNote,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = textUnderLine
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 12.dp, top = 12.dp, bottom = 12.dp),
                text = date.persianLocate(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textDecoration = textUnderLine
            )
        }
    }
}


