package com.rahim.yadino.calender

import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.model.TimeDate
import com.rahim.yadino.base.viewmodel.BaseViewModel
import com.rahim.yadino.dateTime.DataTimeRepository
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalenderViewModel @Inject constructor(
    private val dateTimeRepository: DataTimeRepository,
) : BaseViewModel() {
    private val _times =
        MutableStateFlow<List<TimeDate>>(emptyList())
    val times: StateFlow<List<TimeDate>> = _times

    init {
        getTimesMonth()
    }

    fun getTimesMonth(yearNumber: Int = currentYear, monthNumber: Int = currentMonth) {
        viewModelScope.launch {
            dateTimeRepository.getTimesMonth(yearNumber, monthNumber).catch {}.collectLatest {
                _times.value = it
            }
        }
    }
}