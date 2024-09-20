package com.rahim.yadino.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.yadino.sharedPreferences.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository,
) : ViewModel() {
    fun saveShowWelcome(isShow: Boolean) {
        sharedPreferencesRepository.saveShowWelcome(isShow)
    }
    fun isShowWelcomeScreen() = sharedPreferencesRepository.isShowWelcomeScreen()

}
