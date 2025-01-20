package app.aloha.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import app.aloha.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepo @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val dataStore = context.dataStore

    suspend fun <T> save(key: Preferences.Key<T>, value: T?) {
        context.dataStore.edit { prefs ->
            if (value != null)
                prefs[key] = value
            else
                prefs.remove(key)
        }
    }

    fun <T> get(key: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { prefs -> prefs[key] }
    }
}

object PrefKeys {
    val ACCESS_TOKEN = stringPreferencesKey("user_age")
    val USERNAME = stringPreferencesKey("uid")
    val EMAIL = stringPreferencesKey("email")
}