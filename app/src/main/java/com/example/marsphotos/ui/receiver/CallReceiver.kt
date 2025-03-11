package com.example.marsphotos.ui.receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.CallLog
import android.telephony.TelephonyManager
import android.telephony.SmsManager
import android.util.Log
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.marsphotos.domain.CallHandler

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            var incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            Log.e("CallReceiver", "La llamada está aquí:")

            // Verificación 1: Directamente de EXTRA_INCOMING_NUMBER
            if (!incomingNumber.isNullOrEmpty() && state == TelephonyManager.EXTRA_STATE_RINGING) {
                Log.d("CallReceiver", "Número detectado directamente: $incomingNumber")
                // Retrasamos el envío del mensaje para evitar spam si la llamada es contestada rápidamente
                Handler(Looper.getMainLooper()).postDelayed({
                    Log.d("CallReceiver", "Llamando a CallHandler con el número: $incomingNumber")
                    CallHandler.handleIncomingCall(context, incomingNumber!!)
                }, 5000) // 5 segundos de retraso antes de enviar el SMS
            } else {
                Log.e("CallReceiver", "No se pudo obtener el número de la llamada entrante de EXTRA_INCOMING_NUMBER")

                // Verificación 2: Intentar obtener el número desde el registro de llamadas si no se obtiene de EXTRA_INCOMING_NUMBER
                incomingNumber = getPhoneNumberFromCallLog(context)
                if (!incomingNumber.isNullOrEmpty()) {
                    Log.d("CallReceiver", "Número obtenido desde el registro de llamadas: $incomingNumber")

                    // Retrasamos el envío del mensaje
                    Handler(Looper.getMainLooper()).postDelayed({
                        Log.d("CallReceiver", "Llamando a CallHandler con el número: $incomingNumber")
                        CallHandler.handleIncomingCall(context, incomingNumber)
                        openMessagingApp(context,incomingNumber);
                    }, 5000) // 5 segundos de retraso antes de enviar el SMS
                } else {
                    Log.e("CallReceiver", "No se pudo obtener el número ni de EXTRA_INCOMING_NUMBER ni del registro de llamadas")
                }
            }
        }
    }

    // Función para obtener el número desde el registro de llamadas
    private fun getPhoneNumberFromCallLog(context: Context): String? {
        var phoneNumber: String? = null
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                arrayOf(CallLog.Calls.NUMBER),
                null,  // No hay una condición específica en WHERE
                null,  // Sin filtros adicionales
                CallLog.Calls.DATE + " DESC" // Ordenar por fecha en orden descendente
            )

            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
                    if (columnIndex != -1) { // Verifica si la columna existe
                        phoneNumber = it.getString(columnIndex)
                    } else {
                        Log.e("CallReceiver", "La columna CallLog.Calls.NUMBER no existe.")
                    }
                } else {
                    Log.e("CallReceiver", "No se encontraron registros en el registro de llamadas.")
                }
            }
        } catch (e: SecurityException) {
            Log.e("CallReceiver", "Permiso para leer el registro de llamadas no concedido: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("CallReceiver", "Error al acceder al registro de llamadas: ${e.localizedMessage}")
        } finally {
            cursor?.close()
        }
        return phoneNumber
    }



    private fun sendAutoResponse(context: Context, phoneNumber: String) {
        val message = "No pude contestar tu llamada. ¿Te gustaría dejarme un mensaje?"

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Log.d("CallReceiver", "Mensaje enviado a $phoneNumber: $message")
            } catch (e: Exception) {
                Log.e("CallReceiver", "Error al enviar el mensaje: ${e.localizedMessage}")
            }
        } else {
            Log.e("CallReceiver", "Permiso SEND_SMS no concedido")
        }
    }

    // Abrir la app de mensajería con el número
    private fun openMessagingApp(context: Context, phoneNumber: String) {
        try {
            val sendIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phoneNumber")
                putExtra("sms_body", "No pude contestar tu llamada. ¿Te gustaría dejarme un mensaje?")
            }
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(sendIntent)
            Log.d("CallReceiver", "Abriendo la app de mensajería con el número: $phoneNumber")
        } catch (e: Exception) {
            Log.e("CallReceiver", "Error al abrir la app de mensajería: ${e.localizedMessage}")
        }
    }
}
