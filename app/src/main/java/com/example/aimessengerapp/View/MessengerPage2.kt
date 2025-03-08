package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aimessengerapp.ChatNameModel.ChatEntity
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerPage2(viewModel: MessageViewModel, chatViewModel: ChatViewModel, ragViewModel: RAGViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedEntity by remember{
        mutableStateOf(ChatEntity(name = "",indexAt = -1,clicks = 0, isFavorite = false))
    }

    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }

    val currentChatIndex by chatViewModel.currentChatIndex.collectAsState()

    val searchText by chatViewModel.searchText.collectAsState()
    val listIndex by chatViewModel.savedListIndex.collectAsState()

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = listIndex
    )

    val searchChat by chatViewModel.searchChat.collectAsState()
    val listChatIndex by chatViewModel.savedListChatIndex.collectAsState()

    val listChatState = rememberLazyListState(
        initialFirstVisibleItemIndex = listChatIndex
    )

    val isFavorite by chatViewModel.isFavorite.collectAsState()

    LaunchedEffect(Unit) {
        if (chatViewModel.currentChatIndex.value == null)
            chatViewModel.determineChatToSelect()
    }

    if (currentChatIndex==null)
        LoadingScreen()
    else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    )  {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.padding(start = 15.dp),
                                value = searchChat,
                                onValueChange = { newText ->
                                    chatViewModel.onSearchChatChange(newText)
                                }
                            )
                            IconButton(onClick = {

                                val foundChatIndex = chatViewModel.chats.indexOfFirst {
                                    it.name.contains(searchChat, ignoreCase = true)
                                }
                                if (foundChatIndex != -1) {
                                    chatViewModel.onSavedListChatIndexChange(foundChatIndex)
                                    scope.launch {
                                        listChatState.animateScrollToItem(foundChatIndex)
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "search for message")
                            }

                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!isFavorite)
                                Text(text = "Все чаты")
                            else
                                Text(text = "Избранные чаты")

                            IconButton(onClick = {
                                chatViewModel.changeList()
                            }) {
                                Icon(imageVector = Icons.Default.Star,
                                    tint = if (isFavorite) Color(0xFFB07D2B) else Color.Gray,
                                    contentDescription = "add to chosen")
                            }

                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (chatViewModel.numberOfSort.value == 1)
                                Text(text = "Сортировка по дате")
                            else if (chatViewModel.numberOfSort.value == 2)
                                Text(text = "Сортировка по популярности")

                            Row() {
                                IconButton(onClick = {
                                    chatViewModel.getAllChats()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "sort by date"
                                    )
                                }

                                IconButton(onClick = {
                                    chatViewModel.getAllChatsByPopularity()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ThumbUp,
                                        contentDescription = "sort by popularity"
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        LazyColumn(
                            state = listChatState
                        ) {
                            itemsIndexed(chatViewModel.chats) { index, item ->
                                if ((isFavorite && item.isFavorite) || (!isFavorite)) {
                                    NavigationDrawerItem(
                                        label = {
                                            ChatNameBubble(
                                                chat = item,
                                                searchChat = searchChat,
                                                onClick = {
                                                    selectedEntity = item
                                                    showUpdateDialog = true
                                                },
                                                onDelete = {
                                                    if (item.indexAt != currentChatIndex) {
                                                        chatViewModel.deleteChat(item)
                                                    }
                                                },
                                                onChooseFavorite = {
                                                    val chatModel =
                                                        item.copy(isFavorite = !item.isFavorite)
                                                    chatViewModel.updateChat(chatModel)
                                                    Log.i(
                                                        "chatFavoriteAll",
                                                        chatViewModel.chats[index].isFavorite.toString()
                                                    )
                                                })
                                        },
                                        selected = item.indexAt == currentChatIndex,
                                        onClick = {
                                            chatViewModel.selectChat(item.indexAt)
                                            chatViewModel.getMessagesById(item.indexAt)
                                            chatViewModel.onSearchChatChange("")
                                            chatViewModel.onSearchTextChange("")
                                            chatViewModel.incrementChatClicks(currentChatIndex ?: 0)
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        })
                                }
                            }
                        }
                    }
                }
            },
            drawerState = drawerState
        )


        {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.weight(2f),
                                    value = searchText,
                                    onValueChange = { newText ->
                                        chatViewModel.onSearchTextChange(newText)
                                    }
                                )
                                IconButton(onClick = {

                                    val foundIndex = chatViewModel.messages.indexOfFirst {
                                        it.first.contains(searchText, ignoreCase = true)
                                    }
                                    if (foundIndex != -1) {
                                        chatViewModel.onSavedListIndexChange(foundIndex)
                                        scope.launch {
                                            listState.animateScrollToItem(foundIndex)
                                        }
                                    }

                                }) {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "search for message")
                                }
                            }
                        },
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
                    BottomAppBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp)
                    ) {
                        BottomBar(viewModel, chatViewModel, ragViewModel, currentChatIndex ?:0)
                    }

                }
            ) { padding ->

                ScaffoldMain(
                    listState = listState,
                    padding = padding,
                    chatViewModel = chatViewModel,
                    ragViewModel = ragViewModel
                )
            }
        }

        UpdateChatDialog(
            model = chatViewModel,
            entity = selectedEntity,
            showDialog = showUpdateDialog,
            onDismiss = { chatViewModel.clearDialogText(); showUpdateDialog = false },
            onConfirm = { newName ->
                if (newName.isNotEmpty()) {
                    selectedEntity.name = newName
                    chatViewModel.updateChat(selectedEntity)
                    showUpdateDialog = false
                }
            })

    }
}