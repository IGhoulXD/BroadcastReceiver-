package com.example.data

import com.example.broadcastreceiver.data.UserPreferences

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object CallHandler {
    fun handleIncomingCall(context: Context, incomingNumber: String?) {
        if (incomingNumber == null) return

        val userConfig = runBlocking { UserPreferences.getUserConfig(context).first() }
        if (incomingNumber == userConfig.phoneNumber) {
            sendAutoResponse(userConfig.phoneNumber, userConfig.message)
        }
    }

    private fun sendAutoResponse(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("CallHandler", "Mensaje enviado a $phoneNumber")
        } catch (e: Exception) {
            Log.e("CallHandler", "Error enviando SMS", e)
        }
    }
}
