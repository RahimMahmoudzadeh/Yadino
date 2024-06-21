package com.rahim.ui.calender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.data.modle.data.TimeDate
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalenderViewModel @Inject constructor(
    private val dateTimeRepository: DataTimeRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val baseRepository: BaseRepository
) : BaseViewModel(sharedPreferencesRepository, baseRepository) {
    private val _times =
        MutableStateFlow<List<TimeDate>>(emptyList())
    val times: StateFlow<List<TimeDate>> = _times.asStateFlow()

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