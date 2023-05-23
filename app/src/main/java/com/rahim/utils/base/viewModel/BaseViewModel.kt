package com.rahim.utils.base.viewModel

import androidx.lifecycle.ViewModel
import com.rahim.data.repository.base.BaseRepository
import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val baseRepository: BaseRepository
) :
    ViewModel() {
    val errorGetProses = "مشکلی در دریافت اطلاعات پیش امده است! لطفا دوباره امتحان کنید"
    val errorSaveProses = "مشکلی در ثبت اطلاعات پیش امده است! لطفا دوباره امتحان کنید"
    val successSave = "ثبت اطلاعات با موفقیت ثبت شد!"
    val successUpdateRoutine = "روتین شما با موفقیت ویرایش شد!"
    val successRemove = "حذف روتین با موفقیت انجام شد!"
    fun saveShowWelcome(isShow: Boolean) {
        sharedPreferencesRepository.saveShowWelcome(isShow)
    }

    fun isShowWelcomeScreen(): Boolean = sharedPreferencesRepository.isShowWelcomeScreen()

    fun getCurrentTime(): List<Int> = baseRepository.getCurrentTime()

}