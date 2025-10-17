package com.rahim.yadino.designsystem.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.rahim.yadino.designsystem.component.DialogButtonBackground
import com.rahim.yadino.designsystem.component.DialogButtonBorder
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.library.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorDialog(
  modifier: Modifier = Modifier,
  message: String,
  okMessage: String,
  isClickOk: (Boolean) -> Unit,
) {
  BasicAlertDialog(
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      dismissOnClickOutside = false,
    ),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 22.dp)
      .border(
        1.dp,
        brush = Brush.verticalGradient(gradientColors),
        shape = RoundedCornerShape(8.dp),
      ),
    onDismissRequest = { isClickOk(false) },
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(color = MaterialTheme.colorScheme.background),
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        fontSize = 18.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 30.dp, end = 50.dp, start = 50.dp),
        text = message,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
      )
      Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(
            top = 30.dp,
            bottom = 30.dp,
          ),
      ) {
        DialogButtonBorder(
          text = stringResource(id = R.string.no),
          gradient = Brush.verticalGradient(gradientColors),
          modifier = Modifier,
          textSize = 14.sp,
          width = 0.22f,
          40.dp,
          onClick = {
            isClickOk(false)
          },
        )
        DialogButtonBackground(
          text = okMessage,
          gradient = Brush.verticalGradient(gradientColors),
          modifier = Modifier
            .fillMaxWidth(0.3f)
            .height(40.dp)
            .padding(start = 16.dp),
          textSize = 14.sp,
          onClick = {
            isClickOk(true)
          },
        )
      }
    }
  }
}
