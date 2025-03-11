package com.example.marsphotos.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.data.UserPreferences
import com.example.marsphotos.model.UserConfig
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

    fun saveConfig(context: Context) {
        viewModelScope.launch {
            try {
                UserPreferences.saveUserConfig(context, UserConfig(_phoneNumber.value, _message.value))
                Log.d("GuardarDatos", "Configuración guardada con éxito: Número: ${_phoneNumber.value}, Mensaje: ${_message.value}")
            } catch (e: Exception) {
                Log.e("GuardarDatos", "Error al guardar la configuración: ${e.message}")
            }
        }
    }

    // Nueva función para abrir la aplicación de mensajería con el número y el mensaje predefinidos
    fun openMessagingApp(context: Context, phoneNumber: String, message: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:$phoneNumber")  // Especificamos el número de teléfono
                putExtra("sms_body", message)  // Prellenamos el mensaje
            }

            // Verificamos si hay alguna aplicación que pueda manejar esta intención
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Log.e("MainViewModel", "No se encontró ninguna app de mensajería para abrir.")
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error al abrir la app de mensajería: ${e.message}")
        }
    }
}
