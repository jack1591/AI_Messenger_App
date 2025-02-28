package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimessengerapp.RAGRepositories.RAGObject
import com.example.aimessengerapp.ViewModel.RAGViewModel
import kotlinx.coroutines.flow.first


@Composable
fun PatternScreen(ragViewModel: RAGViewModel,type: String){
    var objectName by remember{
        mutableStateOf("")
    }

    val ragObjects by ragViewModel.ragObjects.collectAsState(emptyList())

    var showDialog by rememberSaveable{
        mutableStateOf(false)
    }

    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }

    var selectedObject by remember { mutableStateOf<RAGObject?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            items(ragObjects) { ragObject ->
                if (type==ragObject.type) {
                    PatternObjectBubble(
                        ragObject = ragObject,
                        onUpdate = {
                            selectedObject =
                                ragObject; ragViewModel.update(ragObject); showUpdateDialog = true
                        },
                        onDelete = { ragViewModel.delete(ragObject) }
                    )
                }
            }

            item {
                Button(modifier = Modifier
                    .padding(5.dp),
                    onClick = {
                        showDialog = true
                    }
                ) {
                    Text(text = "ADD", fontSize = 10.sp)
                }

            }
        }
    }

    InsertDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = { name ->
            if (name.isNotEmpty()) {
                val objectModel = RAGObject(name = name, type = type)
                ragViewModel.insert(objectModel)
            }
        })

        UpdateDialog(
            showDialog = showUpdateDialog,
            onDismiss = { showUpdateDialog = false },
            onConfirm = { newName ->
                selectedObject?.let {
                    Log.i("textState", newName)
                    val objectModel = RAGObject(id = it.id, name = newName, type = type)
                    ragViewModel.update(objectModel)
                }
                showUpdateDialog = false
            })
}