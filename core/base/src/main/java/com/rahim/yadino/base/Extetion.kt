package com.rahim.yadino.base

import android.content.Context
import com.rahim.yadino.R
import com.rahim.yadino.base.enums.MonthName
import com.rahim.yadino.base.enums.error.ErrorMessageCode
import com.rahim.yadino.core.base.R

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

fun String.calculateTimeFormat(currentYer: Int, currentMonth: Int, currentDay: String): String {
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
    return "$currentYer-$currentMonth-$currentDay"
}

fun ErrorMessageCode.errorMessage(context:Context):String{
    return context.run {
        when(this@errorMessage){
            ErrorMessageCode.ERROR_GET_PROCESS->{
                this.resources.getString(R.string.errorGetProses)
            }
            ErrorMessageCode.EQUAL_ROUTINE_MESSAGE->{
                this.resources.getString(R.string.equalRoutineMessage)
            }
            ErrorMessageCode.ERROR_SAVE_PROSES->{
                this.resources.getString(R.string.errorSaveProses)
            }
        }
    }
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