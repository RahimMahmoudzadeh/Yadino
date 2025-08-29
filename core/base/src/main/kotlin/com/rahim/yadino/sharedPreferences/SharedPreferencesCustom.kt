package com.rahim.yadino.sharedPreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.rahim.yadino.Constants.IS_DARK_THEME
import com.rahim.yadino.Constants.NAME_SHARED_PREFERENCE
import com.rahim.yadino.Constants.SAMPLE_NOTE
import com.rahim.yadino.Constants.SAMPLE_ROUTINE
import com.rahim.yadino.Constants.WELCOME_SHARED
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SharedPreferencesCustom @Inject constructor(private val context: Context) {
  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = NAME_SHARED_PREFERENCE)

  suspend fun saveWelcomePage(isShow: Boolean) {
    context.dataStore.edit { data ->
      data[WELCOME_SHARED] = isShow
    }
  }

  fun isShowWelcome() = context.dataStore.data.map { data -> data[WELCOME_SHARED] ?: false }

  suspend fun showSampleRoutine(isShow: Boolean) {
    context.dataStore.edit { data ->
      data[SAMPLE_ROUTINE] = isShow
    }
  }

  fun isShowSampleRoutine() = context.dataStore.data.map { data -> data[SAMPLE_ROUTINE] ?: false }

  suspend fun showSampleNote(isShow: Boolean) {
    context.dataStore.edit { data ->
      data[SAMPLE_NOTE] = isShow
    }
  }

  fun isSampleNote() = context.dataStore.data.map { data -> data[SAMPLE_NOTE] ?: false }

  suspend fun setDarkTheme(isDarkTheme: Boolean) {
    context.dataStore.edit { data ->
      data[IS_DARK_THEME] = isDarkTheme
    }
  }

  fun isDarkTheme() = context.dataStore.data.map { data -> data[IS_DARK_THEME] }
}
