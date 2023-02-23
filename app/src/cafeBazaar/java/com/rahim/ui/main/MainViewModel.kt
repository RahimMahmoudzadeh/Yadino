package com.rahim.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.data.repository.dataTime.DataTimeRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val dataTimeRepository: DataTimeRepository) :
    BaseViewModel() {
    init {
        viewModelScope.launch {
            dataTimeRepository.addTime()
        }
    }
}