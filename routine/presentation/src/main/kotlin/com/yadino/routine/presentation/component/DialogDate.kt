package com.yadino.routine.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.rahim.yadino.calculateMonthName
import com.rahim.yadino.designsystem.component.DialogButtonBackground
import com.rahim.yadino.designsystem.component.TimeItems
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.theme.YadinoTheme
import com.rahim.yadino.enums.HalfWeekName
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.toPersianDigits
import com.yadino.routine.presentation.model.TimeDateUiModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogChoseDate(
  modifier: Modifier = Modifier,
  times: PersistentList<TimeDateUiModel>,
  yearNumber: Int,
  monthNumber: Int,
  dayNumber: Int,
  closeDialog: () -> Unit = {},
  dayCheckedNumber: (year: Int, month: Int, day: Int) -> Unit,
  monthIncrease: (year: Int, month: Int) -> Unit,
  monthDecrease: (year: Int, month: Int) -> Unit,
) {

  val size = LocalSize.current
  val space = LocalSpacing.current
  val fontSize = LocalFontSize.current

  var dayClicked by rememberSaveable { mutableIntStateOf(0) }

  dayClicked = dayNumber

  val date = "$yearNumber ${monthNumber.calculateMonthName()}".toPersianDigits()

  BasicAlertDialog(
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      dismissOnClickOutside = true,
    ),
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = space.space14),
    onDismissRequest = {},
  ) {
    Surface(
      color = MaterialTheme.colorScheme.background,
      shape = RoundedCornerShape(percent = 6),
    ) {
      Column(modifier = Modifier.padding(bottom = space.space12)) {
        Row(
          Modifier
            .fillMaxWidth()
            .background(brush = Brush.verticalGradient(gradientColors))
            .padding(vertical = space.space10, horizontal = space.space12),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          IconButton(
            onClick = {
              monthIncrease(yearNumber, monthNumber)
            },
          ) {
            Icon(
              tint = Color.White,
              painter = painterResource(id = R.drawable.less_then),
              contentDescription = "less then sign",
            )
          }
          Text(
            modifier = Modifier
              .padding(top = space.space12)
              .fillMaxWidth(0.5f),
            text = date,
            color = Color.White,
            textAlign = TextAlign.Center,
          )
          IconButton(
            onClick = {
              monthDecrease(yearNumber, monthNumber)
            },
          ) {
            Icon(
              painterResource(id = R.drawable.greater_then),
              contentDescription = "greater then sign",
              tint = Color.White,
            )
          }
        }
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = space.space12, horizontal = space.space18),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          val days = listOf(
            HalfWeekName.SATURDAY.nameDay,
            HalfWeekName.SUNDAY.nameDay,
            HalfWeekName.MONDAY.nameDay,
            HalfWeekName.TUESDAY.nameDay,
            HalfWeekName.WEDNESDAY.nameDay,
            HalfWeekName.THURSDAY.nameDay,
            HalfWeekName.FRIDAY.nameDay,
          )
          days.reversed().forEach {
            when (it) {
              days.first() -> {
                Text(
                  text = it,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.padding(end = space.space12),
                )
              }

              days.last() -> {
                Text(
                  text = it,
                  color = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.padding(start = space.space12),
                )
              }

              else -> {
                Text(
                  text = it,
                  color = MaterialTheme.colorScheme.primary,
                )
              }
            }
          }
        }
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
          LazyVerticalGrid(
            modifier = Modifier.padding(start = space.space10, end = space.space10, bottom = space.space10),
            columns = GridCells.Fixed(7),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalArrangement = Arrangement.SpaceBetween,
            userScrollEnabled = false,
          ) {
            items(times) { time ->
              TimeItems(
                dayNumber = time.dayNumber,
                nameDay = time.nameDay,
                isToday = time.isToday,
                dayNumberChecked = dayClicked,
                fontSize = fontSize,
                size = size,
                space = space,
                dayCheckedNumber = { day ->
                  dayClicked = day
                },
              )
            }
          }
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(start = space.space12, top = space.space12),
          ) {
            DialogButtonBackground(
              text = stringResource(id = R.string.selection),
              gradient = Brush.verticalGradient(gradientColors),
              modifier = Modifier
                .fillMaxWidth(0.25f)
                .height(size.size40),
              textSize = fontSize.fontSize16,
              textStyle = TextStyle(fontWeight = FontWeight.Bold),
              space = space,
              size = size,
              onClick = {
                dayCheckedNumber(yearNumber, monthNumber, dayClicked)
                closeDialog()
              },
            )
            Spacer(modifier = Modifier.width(size.size10))
            TextButton(
              onClick = {
                closeDialog()
              },
            ) {
              Text(
                fontSize = fontSize.fontSize16,
                text = stringResource(id = R.string.cancel),
                style = TextStyle(
                  brush = Brush.verticalGradient(
                    gradientColors,
                  ),
                  fontWeight = FontWeight.Bold,
                ),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
@Preview(device = Devices.PIXEL_4)
fun DialogChoseDateWrapperLight() {
  val times = ArrayList<TimeDateUiModel>()
  for (i in 1..31) {
    times.add(
      TimeDateUiModel(
        dayNumber = i,
        nameDay = if (i == 7 || i == 14 || i == 21 || i == 28) "ج" else "ش",
        haveTask = false,
        yearNumber = 1403,
        monthNumber = 2,
        monthName = "اردیبهشت",
        isChecked = false,
        isToday = i == 21,
      ),
    )
  }
  YadinoTheme {
    DialogChoseDate(
      times = times.toPersistentList(),
      yearNumber = 1403,
      monthNumber = 2,
      dayNumber = 21,
      closeDialog = {},
      dayCheckedNumber = { year, month, day -> },
      monthIncrease = { year, month -> },
      monthDecrease = { year, month -> },
    )
  }
}

@Composable
@Preview(device = Devices.PIXEL_4, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DialogChoseDateWrapperDark() {
  val times = ArrayList<TimeDateUiModel>()
  for (i in 1..31) {
    times.add(
      TimeDateUiModel(
        dayNumber = i,
        nameDay = if (i == 7 || i == 14 || i == 21 || i == 28) "ج" else "ش",
        haveTask = false,
        yearNumber = 1403,
        monthNumber = 2,
        monthName = "اردیبهشت",
        isChecked = false,
        isToday = i == 28,
      ),
    )
  }
  YadinoTheme {
    DialogChoseDate(
      times = times.toPersistentList(),
      yearNumber = 1403,
      monthNumber = 2,
      dayNumber = 22,
      closeDialog = {},
      dayCheckedNumber = { year, month, day -> },
      monthIncrease = { year, month -> },
      monthDecrease = { year, month -> },
    )
  }
}
