package com.rahim.yadino.calender

import com.rahim.yadino.base.db.model.TimeDate
import com.rahim.yadino.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CalenderViewModel @Inject constructor(
//    private val dateTimeRepository: DateTimeRepository,
) : BaseViewModel() {
    private val _times =
        MutableStateFlow<List<TimeDate>>(emptyList())
    val times: StateFlow<List<TimeDate>> = _times

    init {
        getTimesMonth()
    }

    fun getTimesMonth(yearNumber: Int = currentYear, monthNumber: Int = currentMonth) {
//        viewModelScope.launch {
//            dateTimeRepository.getTimesMonth(yearNumber, monthNumber).catch {}.collectLatest {
//                _times.value = it
//            }
//        }
    }
}