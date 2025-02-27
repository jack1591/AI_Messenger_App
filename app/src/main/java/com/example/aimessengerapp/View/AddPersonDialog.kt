package com.example.aimessengerapp.View

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun AddPersonDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
){
    var textState by remember{
        mutableStateOf(TextFieldValue(""))
    }

    if (showDialog){
        AlertDialog(
            title = {
                Text(text = "Добавление объекта")
            },
            text = {
                OutlinedTextField(
                    value = textState,
                    onValueChange = {textState = it}
                )
            },
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = {
                    onConfirm(textState.text)
                    textState = TextFieldValue("")
                    onDismiss()
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Отмена")
                }
            }
        )
    }
}