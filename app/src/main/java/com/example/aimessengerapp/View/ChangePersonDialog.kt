package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.example.aimessengerapp.RAGRepositories.Person

@Composable
fun ChangePersonDialog(
    person: Person?,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
){
    var textState by remember{
        mutableStateOf(TextFieldValue(person?.name ?: ""))
    }

    if (showDialog){
        AlertDialog(
            title = {
                Text(text = "Изменение объекта")
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
                    Log.i("textState",textState.text)
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