package dev.ykzza.chesstimer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreRepository(
    context: Context
) {

    private object PreferencesKeys {
        val timeModeId = intPreferencesKey(PREFERENCES_ID)
        val timeModeBaseTime = longPreferencesKey(PREFERENCES_BASE_TIME)
        val timeModeAddTime = longPreferencesKey(PREFERENCES_ADD_TIME)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveTimeMode(
        id: Int,
        baseTime: Long,
        addTime: Long
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.timeModeId] = id
            preferences[PreferencesKeys.timeModeBaseTime] = baseTime
            preferences[PreferencesKeys.timeModeAddTime] = addTime
        }
    }

    val readTimeMode: Flow<TimeModeDbModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val id = preferences[PreferencesKeys.timeModeId] ?: -1
            val baseTime = preferences[PreferencesKeys.timeModeBaseTime] ?: 600000
            val timeModeAddTime = preferences[PreferencesKeys.timeModeAddTime] ?: 0
            TimeModeDbModel(id, baseTime, timeModeAddTime)
        }

    companion object {
        private const val PREFERENCES_ID = "timeModeId"
        private const val PREFERENCES_BASE_TIME = "timeModeBaseTime"
        private const val PREFERENCES_ADD_TIME = "timeModeAddTime"
    }
}

