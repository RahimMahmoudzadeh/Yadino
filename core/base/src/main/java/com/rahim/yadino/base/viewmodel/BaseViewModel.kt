package com.rahim.yadino.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

open class BaseViewModel(
    private val persianData: PersianDate = PersianDate()
) :
    ViewModel() {
    val currentDay = persianData.shDay
    val currentMonth = persianData.shMonth
    val currentYear = persianData.shYear
}