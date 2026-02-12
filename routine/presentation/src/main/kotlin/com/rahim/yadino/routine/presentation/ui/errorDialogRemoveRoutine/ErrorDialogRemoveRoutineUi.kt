package com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.rahim.yadino.base.use
import com.rahim.yadino.designsystem.component.DialogButtonBackground
import com.rahim.yadino.designsystem.component.DialogButtonBorder
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.routine.presentation.ui.errorDialogRemoveRoutine.component.ErrorDialogRemoveRoutineComponent
import com.rahim.yadino.showToastShort
import com.rahim.yadino.toStringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorDialogUi(
  modifier: Modifier = Modifier,
  component: ErrorDialogRemoveRoutineComponent,
) {

  val (state, effect, event) = use(component)

  val space = LocalSpacing.current
  val size = LocalSize.current
  val fontSize = LocalFontSize.current
  val context = LocalContext.current

  LaunchedEffect(effect) {
    effect.collect { effect ->
      when (effect) {
        is ErrorDialogRemoveRoutineComponent.Effect.ShowToast -> context.showToastShort(stringId = effect.message.toStringResource())
      }
    }
  }

  BasicAlertDialog(
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      dismissOnClickOutside = false,
    ),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = space.space22)
      .border(
        1.dp,
        brush = Brush.verticalGradient(gradientColors),
        shape = RoundedCornerShape(space.space8),
      ),
    onDismissRequest = {},
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(space.space8))
        .background(color = MaterialTheme.colorScheme.background),
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        fontSize = fontSize.fontSize18,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = space.space30, end = space.space50, start = space.space50),
        text = state.title,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
      )
      Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            vertical = space.space30,
          ),
      ) {
        DialogButtonBorder(
          text = stringResource(id = R.string.no),
          gradient = Brush.verticalGradient(gradientColors),
          modifier = Modifier,
          textSize = fontSize.fontSize14,
          width = 0.22f,
          space = space,
          size = size,
          height = size.size40,
          onClick = {
            event(ErrorDialogRemoveRoutineComponent.Event.CancelClicked)
          },
        )
        DialogButtonBackground(
          text = state.submitTextButton,
          gradient = Brush.verticalGradient(gradientColors),
          modifier = Modifier
            .fillMaxWidth(0.3f)
            .height(size.size40)
            .padding(start = space.space16),
          size = size,
          space = space,
          onClick = {
            event(ErrorDialogRemoveRoutineComponent.Event.OkClicked)
          },
        )
      }
    }
  }
}
