package com.rahim.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.data.sharedPreferences.SharedPreferencesCustom
import com.rahim.utils.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataTimeRepository: DataTimeRepository,
    sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel(sharedPreferencesRepository) {
    init {
        viewModelScope.launch {
            dataTimeRepository.addTime()
        }
    }
}