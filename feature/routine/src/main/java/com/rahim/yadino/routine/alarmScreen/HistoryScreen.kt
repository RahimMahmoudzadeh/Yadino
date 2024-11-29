package com.rahim.yadino.routine.alarmScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahim.yadino.base.use
import com.rahim.yadino.persianLocate
import com.rahim.yadino.designsystem.component.AlarmHistoryCardItem
import com.rahim.yadino.designsystem.theme.CornflowerBlueLight
import com.rahim.yadino.feature.routine.R
import com.rahim.yadino.routine.model.RoutineModel

@Composable
internal fun HistoryRoute(
  historyViewModel: HistoryViewModel = hiltViewModel(),
) {
  val (state, event) = use(historyViewModel)

  HistoryScreen(state)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistoryScreen(
  state: RoutineHistoryContract.HistoryState,
) {
  val (completedTasks, incompleteTasks) = if (state.routines.isNotEmpty()) {
     state.routines.partition { sort -> sort.isChecked }
  }else{
    emptyList<RoutineModel>() to emptyList()
  }

  var expanded by rememberSaveable {
    mutableStateOf(false)
  }
  val rotateState by animateFloatAsState(
    targetValue = if (expanded) 180f else 0f,
    label = "",
  )
  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize(),
    ) {
      item {
        val text =
          if (incompleteTasks.isEmpty()) stringResource(id = R.string.not_alarm) else
            "${stringResource(id = R.string.you)} ${
              incompleteTasks.size.toString().persianLocate()
            } ${stringResource(id = R.string.have_alarm)}"
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
          text = text,
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.labelMedium,
          color = if (incompleteTasks.isEmpty()) MaterialTheme.colorScheme.secondaryContainer else CornflowerBlueLight,
          fontWeight = FontWeight.Bold,
        )
      }
      items(incompleteTasks) { routine ->
        AlarmHistoryCardItem(
          routineIsChecked = routine.isChecked,
          routineName = routine.name,
          routineDayNumber = routine.dayNumber ?: 0,
          routineYearNumber = routine.yearNumber ?: 0,
          routineMonthNumber = routine.monthNumber ?: 0,
          routineTimeHours = routine.timeHours ?: "",
        )
      }
      stickyHeader {
        RoutineCompleted(
          size = completedTasks.size,
          rotateState = rotateState,
          onClick = { expanded = !expanded },
        )
      }
      items(completedTasks) { routine ->
        AnimatedVisibility(
          visible = expanded,
          enter = fadeIn() + expandVertically(animationSpec = tween(1000)),
          exit = fadeOut() + shrinkVertically(animationSpec = tween(1000)),
        ) {
          AlarmHistoryCardItem(
            routineIsChecked = routine.isChecked,
            routineName = routine.name,
            routineDayNumber = routine.dayNumber ?: 0,
            routineYearNumber = routine.yearNumber ?: 0,
            routineMonthNumber = routine.monthNumber ?: 0,
            routineTimeHours = routine.timeHours ?: "",
          )
        }
      }

    }
  }
}

@Composable
private fun RoutineCompleted(
  size: Int,
  rotateState: Float,
  onClick: () -> Unit,
) {

  Row(
    modifier = Modifier
      .background(MaterialTheme.colorScheme.background)
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 9.dp),
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically,
  ) {

    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = stringResource(id = R.string.completed),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyLarge,
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "( ${
          size.toString().persianLocate()
        } ${stringResource(id = com.rahim.yadino.library.designsystem.R.string.routine)} )",
        style = MaterialTheme.typography.bodyMedium,
        color = CornflowerBlueLight,
        fontWeight = FontWeight.SemiBold,
      )

    }


    IconButton(
      onClick = {
        onClick()
      },
    ) {
      Icon(
        imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = "",
        modifier = Modifier
          .rotate(rotateState),
        tint = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

