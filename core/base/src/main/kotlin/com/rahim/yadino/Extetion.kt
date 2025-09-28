package com.rahim.yadino

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.rahim.yadino.core.base.R
import com.rahim.yadino.enums.MonthName
import com.rahim.yadino.enums.error.ErrorMessageCode
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

fun String.calculateTimeFormat(currentYear: Int, currentMonth: Int, currentDay: String): String {
  val currentMonth = if (currentMonth.toString().length == 1) {
    "0$currentMonth"
  } else {
    currentMonth
  }
  val currentDay = if (currentDay.length == 1) {
    "0$currentDay"
  } else {
    currentDay
  }
  return "$currentYear-$currentMonth-$currentDay"
}

fun ErrorMessageCode.errorMessage() = when (this@errorMessage) {
  ErrorMessageCode.ERROR_GET_PROCESS -> R.string.errorGetProses
  ErrorMessageCode.EQUAL_ROUTINE_MESSAGE -> R.string.equalRoutineMessage

  ErrorMessageCode.ERROR_SAVE_PROSES -> R.string.errorSaveProses

  ErrorMessageCode.ERROR_NOTIFICATION_PERMISSION -> R.string.errorSaveProses
  ErrorMessageCode.ERROR_REMINDER_PERMISSION -> R.string.errorSaveProses
  ErrorMessageCode.ERROR_NOTIFICATION_AND_REMINDER_PERMISSION -> R.string.errorSaveProses
  ErrorMessageCode.ERROR_TIME_PASSED -> R.string.errorTimePassed
}

fun String.persianLocate(): String {
  var result = ""
  var fa = '۰'
  for (ch in this) {
    fa = ch
    when (ch) {
      '0' -> fa = '۰'
      '1' -> fa = '۱'
      '2' -> fa = '۲'
      '3' -> fa = '۳'
      '4' -> fa = '۴'
      '5' -> fa = '۵'
      '6' -> fa = '۶'
      '7' -> fa = '۷'
      '8' -> fa = '۸'
      '9' -> fa = '۹'
    }
    result = "${result}$fa"
  }
  return result
}

fun String.isPackageInstalled(packageManager: PackageManager): Boolean {
  try {
    packageManager.getPackageInfo(this, 0)
    return true
  } catch (e: PackageManager.NameNotFoundException) {
    return false
  }
}

fun <T> Flow<List<T>>.getMatchingItems(predicate: (T) -> Boolean): Flow<List<T>> {
  return this.map { list ->
    val matchingItems = list.filter(predicate)
    Timber.tag("getMatchingItems").d("getMatchingItems list${list.map { it }}")
    Timber.tag("getMatchingItems").d("getMatchingItems ${matchingItems.map { it }}")
    matchingItems.ifEmpty {
      emptyList()
    }
  }.flowOn(Dispatchers.IO)
}

suspend fun <T> Flow<T>.collectWithoutHistory(collector: suspend (T) -> Unit) {
  var firstEmission = true
  collect { value ->
    if (!firstEmission) {
      collector(value)
    }
    firstEmission = false
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
fun Context.showToastShort(stringId: Int,duration:Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this, this.resources.getString(stringId), duration).show()
}
