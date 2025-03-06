package com.example.broadcastreceiver.data

import android.content.Context
import com.example.data.model.UserConfig
import kotlinx.coroutines.flow.Flow

class PreferencesRepository(private val context: Context) {
    fun getUserConfig(): Flow<UserConfig> = UserPreferences.getUserConfig(context)

    suspend fun saveUserConfig(config: UserConfig) {
        UserPreferences.saveUserConfig(context, config)
    }
}
