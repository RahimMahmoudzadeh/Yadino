package com.rahim.yadino.calender

import androidx.lifecycle.ViewModel
import com.rahim.yadino.base.model.TimeDate
import com.rahim.yadino.dateTime.DateTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CalenderViewModel @Inject constructor(
  private val dateTimeRepository: DateTimeRepository,
) : ViewModel() {

  private val _times =
    MutableStateFlow<List<TimeDate>>(emptyList())
  val times: StateFlow<List<TimeDate>> = _times

  val currentYear = dateTimeRepository.currentTimeYer
  val currentMonth = dateTimeRepository.currentTimeMonth
  val currentDay = dateTimeRepository.currentTimeDay

  init {
    getTimesMonth()
  }

  fun getTimesMonth(yearNumber: Int = dateTimeRepository.currentTimeYer, monthNumber: Int = dateTimeRepository.currentTimeMonth) {
//        viewModelScope.launch {
//            dateTimeRepository.getTimesMonth(yearNumber, monthNumber).catch {}.collectLatest {
//                _times.value = it
//            }
//        }
  }
}
