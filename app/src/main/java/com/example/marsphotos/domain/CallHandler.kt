package com.example.marsphotos.domain

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat

object CallHandler {

    fun handleIncomingCall(context: Context, phoneNumber: String) {
        val message = "Hola, no puedo contestar en este momento. Te responderé más tarde."

        // Verificar permisos antes de enviar SMS
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            Log.e("CallHandler", "Permiso SEND_SMS no concedido")
            return
        }

        // Intents para verificar el estado del SMS
        val sentIntent = Intent("SMS_SENT")
        val deliveredIntent = Intent("SMS_DELIVERED")

        val sentPendingIntent = PendingIntent.getBroadcast(
            context, 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val deliveredPendingIntent = PendingIntent.getBroadcast(
            context, 0, deliveredIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, sentPendingIntent, deliveredPendingIntent)
            Log.d("CallHandler", "Enviando SMS a: $phoneNumber")
        } catch (e: Exception) {
            Log.e("CallHandler", "Error al enviar SMS: ${e.message}")
        }
    }
}
