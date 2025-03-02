package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import com.example.aimessengerapp.RAGRepositories.RAGObject
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerPage2(viewModel: MessageViewModel, chatViewModel: ChatViewModel, ragViewModel: RAGViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    var selectedIndex by rememberSaveable {
        mutableStateOf(-1)
    }
    var selectedName by rememberSaveable {
        mutableStateOf("")
    }
    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }

    var items = remember {
        mutableStateListOf(
            "example1",
            "example2",
            "example3"
        )
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet{
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Text(text = item)
                                IconButton(onClick = {
                                    selectedName = item
                                    selectedIndex = index
                                    showUpdateDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "edit name of chat"
                                    )
                                }
                            }
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                        })

                }
            }
        },
        drawerState = drawerState
        )
    {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "AIMessengerApp") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar (
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
                ){
                    BottomBar(viewModel,chatViewModel,ragViewModel,1)
                }

            }
        ){ padding->
            ScaffoldMain(padding = padding, chatViewModel = chatViewModel, ragViewModel = ragViewModel, numberOfChat = 1)
        }
    }

    UpdateChatDialog(
        model = chatViewModel,
        name = selectedName,
        showDialog = showUpdateDialog,
        onDismiss = { chatViewModel.clearDialogText(); showUpdateDialog = false },
        onConfirm = { newName ->
            Log.i("newName", selectedIndex.toString())
            items[selectedIndex] = newName
            Log.i("newName", items[selectedIndex])
            showUpdateDialog = false
        })
}
