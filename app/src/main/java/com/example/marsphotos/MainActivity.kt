package com.example.broadcastreceiver

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.broadcastreceiver.ui.screens.MainScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions()

        setContent {
            MainScreen()
        }
    }

    private fun requestPermissions() {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions[Manifest.permission.READ_PHONE_STATE] == true &&
                    permissions[Manifest.permission.SEND_SMS] == true
            if (!granted) {
                // Manejar caso donde el usuario no concedió permisos
            }
        }.launch(arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS
        ))
    }
}
