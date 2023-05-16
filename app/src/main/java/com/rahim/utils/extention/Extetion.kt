package com.rahim.utils.extention

import com.rahim.utils.enums.MonthName

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