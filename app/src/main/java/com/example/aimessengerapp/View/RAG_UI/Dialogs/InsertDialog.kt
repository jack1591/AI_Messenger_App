package com.example.aimessengerapp.View.RAG_UI.Dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel

//диалог добавления нового шаблона
@Composable
fun InsertDialog(
    model: RAGViewModel,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
){
    val textState by model.dialogText.collectAsState()

    if (showDialog){
        AlertDialog(
            title = {
                Text(text = "Добавление объекта")
            },
            text = {
                OutlinedTextField(
                    value = textState,
                    onValueChange = {model.updateDialogText(it)}
                )
            },
            onDismissRequest = {  },
            confirmButton = {
                Button(onClick = {
                    onConfirm(textState)
                    //очистить текст из диалога
                    model.clearDialogText()
                    //закрыть диалог
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