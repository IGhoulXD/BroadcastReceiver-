package com.example.broadcastreceiver.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.UserConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

object UserPreferences {
    private val PHONE_NUMBER_KEY = stringPreferencesKey("phone_number")
    private val MESSAGE_KEY = stringPreferencesKey("message")

    fun getUserConfig(context: Context): Flow<UserConfig> {
        return context.dataStore.data.map { prefs ->
            UserConfig(
                phoneNumber = prefs[PHONE_NUMBER_KEY] ?: "",
                message = prefs[MESSAGE_KEY] ?: ""
            )
        }
    }

    suspend fun saveUserConfig(context: Context, config: UserConfig) {
        context.dataStore.edit { prefs ->
            prefs[PHONE_NUMBER_KEY] = config.phoneNumber
            prefs[MESSAGE_KEY] = config.message
        }
    }
}
