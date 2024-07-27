package com.rahim.yadino.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
//    private val baseRepository: BaseRepository
) :
    ViewModel() {

//    val currentYear get() = getCurrentTime()[0]
//    val currentMonth get() = getCurrentTime()[1]
//    val currentDay get() = getCurrentTime()[2]

//    fun isShowWelcomeScreen() = sharedPreferencesRepository.isShowWelcomeScreen()

//    private fun getCurrentTime(): List<Int> = baseRepository.getCurrentTime()

//    fun showSampleRoutine(isShow: Boolean = true) {
//        viewModelScope.launch {
//            sharedPreferencesRepository.isShowSampleRoutine(isShow)
//        }
//    }
}