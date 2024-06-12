package com.rahim.utils.base.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val baseRepository: BaseRepository
) :
    ViewModel() {

    val currentYear get() = getCurrentTime()[0]
    val currentMonth get() = getCurrentTime()[1]
    val currentDay get() = getCurrentTime()[2]

    fun isShowWelcomeScreen() = sharedPreferencesRepository.isShowWelcomeScreen()

    private fun getCurrentTime(): List<Int> = baseRepository.getCurrentTime()

    fun showSampleRoutine(isShow: Boolean = true) {
        viewModelScope.launch {
            sharedPreferencesRepository.isShowSampleRoutine(isShow)
        }
    }

    fun showSampleNote(isShow: Boolean) {
        viewModelScope.launch {
            sharedPreferencesRepository.isShowSampleNote(isShow)
        }
    }

}