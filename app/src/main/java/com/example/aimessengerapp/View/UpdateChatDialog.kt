package com.example.aimessengerapp.View

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.aimessengerapp.ChatNameModel.ChatEntity
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel

//диалоговое окно для изменение имени чата
@Composable
fun UpdateChatDialog(
    model: ChatViewModel,
    entity: ChatEntity,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
){

    val textState by model.dialogText.collectAsState()
    model.updateDialogText(entity.name)

    //если показываем диалог
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
                    onConfirm(textState) //изменяем имя на newName
                    model.clearDialogText() //очищаем содержимое диалога
                    onDismiss() //выходим
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
