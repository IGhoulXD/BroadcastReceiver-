package com.example.broadcastreceiver.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.broadcastreceiver.data.UserPreferences
import com.example.data.model.UserConfig

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    init {
        viewModelScope.launch {
            UserPreferences.getUserConfig(context).collect { config ->
                _phoneNumber.value = config.phoneNumber
                _message.value = config.message
            }
        }
    }

    fun setPhoneNumber(number: String) {
        _phoneNumber.value = number
    }

    fun setMessage(msg: String) {
        _message.value = msg
    }

    fun saveConfig() {
        viewModelScope.launch {
            UserPreferences.saveUserConfig(context, UserConfig(_phoneNumber.value, _message.value))
        }
    }
}
