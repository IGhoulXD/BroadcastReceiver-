package com.example.broadcastreceiver.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.broadcastreceiver.ui.viewmodel.MainViewModel


@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val message by viewModel.message.collectAsState()

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
            onClick = { viewModel.saveConfig() },
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
