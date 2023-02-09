package com.rahim.utils.base.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {
    val errorGetProses="مشکلی در دریافت اطلاعات پیش امده است! لطفا دوباره امتحان کنید"
    val errorSaveProses="مشکلی در ثبت اطلاعات پیش امده است! لطفا دوباره امتحان کنید"
    val successSave="ثبت اطلاعات با موفقیت ثبت شد!"
    val successUpdateRoutine="روتین شما با موفقیت ویرایش شد!"
    val successRemove="حذف روتین با موفقیت انجام شد!"
}