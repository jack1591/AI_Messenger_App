package com.example.aimessengerapp.View.RAG_UI.Dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel

//окно изменения имени шаблона
@Composable
fun UpdateDialog(
    model: RAGViewModel,
    name: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
){

    val textState by model.dialogText.collectAsState()
    model.updateDialogText(name)


    if (showDialog){
        AlertDialog(
            title = {
                Text(text = "Изменение объекта")
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
