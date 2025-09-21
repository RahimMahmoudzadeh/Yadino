package com.yadino.routine.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rahim.yadino.designsystem.component.ItemRoutine
import com.yadino.routine.domain.model.RoutineModelDomainLayer

@Composable()
fun ListRoutines(
    modifier: Modifier = Modifier,
    routines: List<RoutineModelDomainLayer>,
    checkedRoutine: (RoutineModelDomainLayer) -> Unit,
    deleteRoutine: (RoutineModelDomainLayer) -> Unit,
    updateRoutine: (RoutineModelDomainLayer) -> Unit,
) {
  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(top = 0.dp, start = 16.dp, end = 16.dp),
  ) {
    items(items = routines) { routine ->
      ItemRoutine(
        nameRoutine = routine.name,
        isChecked = routine.isChecked,
        timeHoursRoutine = routine.timeHours ?: "",
        explanationRoutine = routine.explanation ?: "",
        onChecked = {
          checkedRoutine(routine.copy(isChecked = it))
        },
        openDialogDelete = {
          deleteRoutine(routine)
        },
        openDialogEdit = {
          updateRoutine(routine)
        },
      )
    }
  }
}
