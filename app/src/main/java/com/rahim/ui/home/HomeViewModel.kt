package com.rahim.ui.home

import com.rahim.data.repository.sharedPreferences.SharedPreferencesRepository
import com.rahim.utils.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(sharedPreferencesRepository: SharedPreferencesRepository) :
    BaseViewModel(sharedPreferencesRepository) {

}
