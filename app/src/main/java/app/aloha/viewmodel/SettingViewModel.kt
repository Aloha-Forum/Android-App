package app.aloha.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.aloha.dataStore
import app.aloha.internet.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStore:  DataStore<Preferences>,
) : ViewModel() {

    fun getTheme(): Flow<Boolean?> {
        return dataStore.data.map { it[booleanPreferencesKey("theme")] }
    }

    fun setTheme(theme: Boolean?) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                if (theme == null)
                    preferences.remove(booleanPreferencesKey("theme"))
                else
                    preferences[booleanPreferencesKey("theme")] = theme
            }
        }
    }
}

