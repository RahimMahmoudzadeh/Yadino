package com.rahim.yadino

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.rahim.yadino.core.base.R
import com.rahim.yadino.enums.MonthName
import com.rahim.yadino.enums.message.MessageUi
import com.rahim.yadino.enums.message.error.ErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber

fun Int.calculateMonthName(): String {
  return when (this) {
    1 -> {
      MonthName.FARFARDINE.nameMonth
    }

    2 -> {
      MonthName.ORDIBESHT.nameMonth
    }

    3 -> {
      MonthName.KHORDAD.nameMonth
    }

    4 -> {
      MonthName.TIR.nameMonth
    }

    5 -> {
      MonthName.MORDAD.nameMonth
    }

    6 -> {
      MonthName.SHAHRIVAR.nameMonth
    }

    7 -> {
      MonthName.MEHER.nameMonth
    }

    8 -> {
      MonthName.ABAN.nameMonth
    }

    9 -> {
      MonthName.AZAR.nameMonth
    }

    10 -> {
      MonthName.DE.nameMonth
    }

    11 -> {
      MonthName.BAHMAN.nameMonth
    }

    else -> {
      MonthName.ISFAND.nameMonth
    }
  }
}

fun MessageUi.toStringResource(): Int = when (this) {
  MessageUi.ERROR_GET_PROCESS -> R.string.errorGetProses
  MessageUi.ERROR_EQUAL_ROUTINE_MESSAGE -> R.string.equalRoutineMessage
  MessageUi.ERROR_SAVE_REMINDER -> R.string.errorSaveReminder
  MessageUi.ERROR_NOTIFICATION_PERMISSION -> R.string.errorSaveProses
  MessageUi.ERROR_REMINDER_PERMISSION -> R.string.errorSaveProses
  MessageUi.ERROR_NOTIFICATION_AND_REMINDER_PERMISSION -> R.string.errorSaveProses
  MessageUi.ERROR_TIME_PASSED -> R.string.errorTimePassed
  MessageUi.ERROR_SEARCH_ROUTINE -> R.string.we_encountered_problem_during_search_routine
  MessageUi.SUCCESS_SAVE_REMINDER -> R.string.your_routine_has_been_successfully_recorded
  MessageUi.SUCCESS_UPDATE_REMINDER -> R.string.your_routine_has_been_successfully_updated
  MessageUi.ERROR_UPDATE_REMINDER -> R.string.errorUpdateReminder
  MessageUi.ERROR_REMOVE_REMINDER -> R.string.errorRemoveReminder
}

fun String.toPersianDigits(): String {
  val builder = StringBuilder(this.length) // Initialize with the same capacity
  for (ch in this) {
    when (ch) {
      '0' -> builder.append('۰')
      '1' -> builder.append('۱')
      '2' -> builder.append('۲')
      '3' -> builder.append('۳')
      '4' -> builder.append('۴')
      '5' -> builder.append('۵')
      '6' -> builder.append('۶')
      '7' -> builder.append('۷')
      '8' -> builder.append('۸')
      '9' -> builder.append('۹')
      else -> builder.append(ch) // Don't forget non-digit characters
    }
  }
  return builder.toString()
}


fun String.isPackageInstalled(packageManager: PackageManager): Boolean {
  try {
    packageManager.getPackageInfo(this, 0)
    return true
  } catch (e: PackageManager.NameNotFoundException) {
    return false
  }
}

fun DrawScope.createOvalBottomPath(
  ovalHeight: Float,
): Path {
  val constantOffsetPx = size.width / 12
  val path = Path()
  path.moveTo(0f, 0f)
  path.lineTo(size.width, 0f)
  path.lineTo(size.width, ovalHeight - constantOffsetPx)
  path.quadraticTo(
    x1 = size.width / 2,
    y1 = ovalHeight + constantOffsetPx,
    x2 = 0f,
    y2 = ovalHeight - constantOffsetPx,
  )
  path.close()
  return path
}

fun Context.showToastShort(stringId: Int, duration: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this, this.resources.getString(stringId), duration).show()
}
