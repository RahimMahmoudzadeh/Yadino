package com.rahim.utils.extention

import android.content.Context
import com.rahim.R
import com.rahim.yadino.base.enums.MonthName
import com.rahim.yadino.base.enums.error.ErrorMessageCode

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