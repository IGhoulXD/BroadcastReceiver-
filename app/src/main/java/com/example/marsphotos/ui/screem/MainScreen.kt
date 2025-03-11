package com.example.marsphotos.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marsphotos.ui.viewmodel.MainViewModel


@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val message by viewModel.message.collectAsState()
    LaunchedEffect(phoneNumber, message) {
        Log.d("GuardarDatos", "Número: $phoneNumber, Mensaje: $message")
    }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { viewModel.setPhoneNumber(it) },
            label = { Text("Número de teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = message,
            onValueChange = { viewModel.setMessage(it) },
            label = { Text("Mensaje de respuesta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.saveConfig(context) },
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Guardar Configuración")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}
