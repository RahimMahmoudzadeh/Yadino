package com.rahim.yadino.welcome

import androidx.lifecycle.viewModelScope
import com.rahim.yadino.base.viewmodel.BaseViewModel
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
) : BaseViewModel() {
    fun saveShowWelcome(isShow: Boolean) {
        sharedPreferencesRepository.saveShowWelcome(isShow)
    }
    fun isShowWelcomeScreen() = sharedPreferencesRepository.isShowWelcomeScreen()

}