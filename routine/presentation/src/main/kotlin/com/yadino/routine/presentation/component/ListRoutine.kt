package com.yadino.routine.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rahim.yadino.designsystem.component.ItemRoutine
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.yadino.routine.presentation.model.RoutineUiModel
import kotlinx.collections.immutable.PersistentList

@Composable()
fun ListRoutines(
  modifier: Modifier = Modifier,
  routines: PersistentList<RoutineUiModel>,
  checkedRoutine: (RoutineUiModel) -> Unit,
  deleteRoutine: (RoutineUiModel) -> Unit,
  updateRoutine: (RoutineUiModel) -> Unit,
) {

  val space = LocalSpacing.current

  LazyColumn(
    modifier = modifier,
    contentPadding = PaddingValues(top = 0.dp, start = space.space16, end = space.space16),
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
