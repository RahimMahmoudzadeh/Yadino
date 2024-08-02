package com.rahim.yadino.welcome

import com.rahim.yadino.base.viewmodel.BaseViewModel
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
) : BaseViewModel() {
    fun saveShowWelcome(isShow: Boolean) {
        sharedPreferencesRepository.saveShowWelcome(isShow)
    }
}