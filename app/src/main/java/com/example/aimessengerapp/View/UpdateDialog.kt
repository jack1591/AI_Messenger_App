package com.example.aimessengerapp.View

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
import com.example.aimessengerapp.RAGRepositories.Goal
import com.example.aimessengerapp.RAGRepositories.Location
import com.example.aimessengerapp.RAGRepositories.Person

@Composable
fun UpdateDialog(
    person: Person? = null,
    location: Location? = null,
    goal: Goal? = null,

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