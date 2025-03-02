package com.example.aimessengerapp.View.RAG_UI.Dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.aimessengerapp.ViewModel.RAGViewModel


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

    /*
    var textState by rememberSaveable{
        mutableStateOf("")
    }

    textState = name
     */

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
                    model.clearDialogText()
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
