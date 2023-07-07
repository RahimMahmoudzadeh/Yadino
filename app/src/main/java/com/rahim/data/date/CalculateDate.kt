package com.rahim.data.date

import java.util.Calendar

interface CalculateDate {
    fun calculateTime(
        yer: Int?,
        month: Int?,
        dayOfYer: Int?,
        hours: Int,
        minute: Int
    ): Calendar

    fun calculateMonth(month: Int?): Int?
}