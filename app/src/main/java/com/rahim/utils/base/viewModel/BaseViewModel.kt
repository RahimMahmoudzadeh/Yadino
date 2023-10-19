package com.rahim.utils.base.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    val sharedPreferencesRepository: SharedPreferencesRepository,
    private val baseRepository: BaseRepository
) :
    ViewModel() {

    val errorGetProses = "مشکلی در دریافت اطلاعات پیش امده است! لطفا دوباره امتحان کنید"
    val errorSaveProses = "مشکلی در ثبت اطلاعات پیش امده است! لطفا دوباره امتحان کنید"
    val successSave = "ثبت اطلاعات با موفقیت ثبت شد!"
    val successUpdateRoutine = "روتین شما با موفقیت ویرایش شد!"
    val successRemove = "حذف روتین با موفقیت انجام شد!"

    val currentYer = getCurrentTime()[0]
    val currentMonth = getCurrentTime()[1]
    val currentDay = getCurrentTime()[2]

    private val _flowNameDay =
        MutableStateFlow("")
    val flowNameDay: StateFlow<String> = _flowNameDay

    private val _idAlarms =
        MutableStateFlow(listOf(0L))
    val idAlarms: StateFlow<List<Long>> = _idAlarms

    init {
        getIdAlarms()
    }

    fun isShowWelcomeScreen() = sharedPreferencesRepository.isShowWelcomeScreen()

    fun getCurrentTime(): List<Int> = baseRepository.getCurrentTime()

    fun getCurrentNameDay(date: String, format: String) {
        viewModelScope.launch {
            val time = baseRepository.getCurrentNameDay(date, format)
            _flowNameDay.value = time
        }
    }

    fun showSampleRoutine(isShow: Boolean) {
        viewModelScope.launch {
            sharedPreferencesRepository.isShowSampleRoutine(isShow)
        }
    }

    fun showSampleNote(isShow: Boolean) {
        viewModelScope.launch {
            sharedPreferencesRepository.isShowSampleNote(isShow)
        }
    }

    private fun getIdAlarms() {
        viewModelScope.launch {
            baseRepository.getIdAlarms().catch {}.collect {
                _idAlarms.value = it
            }
        }
    }
}