package com.example.aimessengerapp.View.MainPage

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aimessengerapp.ChatNameModel.ChatEntity
import com.example.aimessengerapp.View.ChatNameBubble
import com.example.aimessengerapp.View.LoadingScreen
import com.example.aimessengerapp.View.UpdateChatDialog
import com.example.aimessengerapp.ViewModel.Chat.ChatViewModel
import com.example.aimessengerapp.ViewModel.MessageViewModel
import com.example.aimessengerapp.ViewModel.RAG.RAGViewModel
import com.example.aimessengerapp.VoiceToTextParser
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min


/*
    Основной экран:
    - отображение списка сообщений текущего чата
    - ввод сообщения
    - поиск по сообщениям
    - переход в rag
    - ввод с микрофона
    - список чатов
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerPage2(viewModel: MessageViewModel, chatViewModel: ChatViewModel, ragViewModel: RAGViewModel, voiceToTextParser: VoiceToTextParser) {

    // Состояние бокового меню (открыто/закрыто)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    // Текущий выбранный чат для редактирования
    var selectedEntity by remember{
        mutableStateOf(ChatEntity(name = "",indexAt = -1,clicks = 0, isFavorite = false))
    }

    // Флаг для отображения диалога редактирования чата
    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }

    // Подписка на индекс текущего чата
    val currentChatIndex by chatViewModel.currentChatIndex.collectAsState()

    // Состояния для поиска сообщений и чатов
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
    val visibleIndex by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
    }

    // Флаг переключения между всеми чатами и избранными
    val isFavorite by chatViewModel.isFavorite.collectAsState()

    // Определение чата при запуске приложения
    LaunchedEffect(Unit) {
        if (chatViewModel.currentChatIndex.value == null)
            chatViewModel.determineChatToSelect()
        if (chatViewModel.chats.isNotEmpty()) {
            scope.launch {
                listChatState.animateScrollToItem(chatViewModel.chats.size - 1)
            }
        }
    }

    // Если текущий чат не выбран - показываем экран загрузки
    if (currentChatIndex==null)
        LoadingScreen()
    else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    )  {

                        //строка поиска по чатам
                        SearchChatBar(
                            chatViewModel = chatViewModel,
                            searchChat = searchChat,
                            onClick = {

                                //массив индексов чата, где найдены совпадения
                                val foundIndexChatList = mutableListOf<Int>()
                                var num: Int = 0
                                chatViewModel.chats.forEach{ it->
                                    //добавляем совпадения в массив
                                    if (it.name.contains(searchText, ignoreCase = true))
                                        foundIndexChatList.add(num)
                                    num++
                                }
                                num = 0
                                //наименьший индекс чата, который видит пользователь
                                val visibleItemCount = listChatState.layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0 + 1
                                //находим совпадение, которое находится выше экрана пользователя
                                while (num<foundIndexChatList.size && foundIndexChatList[num] < visibleItemCount){
                                    num++
                                }
                                //смещаемся
                                scope.launch {
                                    listChatState.animateScrollToItem(foundIndexChatList[max(0,num-1)])
                                }

                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        //строка выбора чатов (избранные/ все) + строка сортировки
                        ChooseBar(chatViewModel = chatViewModel, isFavorite = isFavorite)

                        Spacer(modifier = Modifier.height(20.dp))

                        //список чатов
                        LazyColumn(
                            state = listChatState
                        ) {
                            itemsIndexed(chatViewModel.chats) { index, item ->
                                // Фильтрация списка чатов в зависимости от режима (все или избранные)
                                if ((isFavorite && item.isFavorite) || (!isFavorite)) {
                                    NavigationDrawerItem(
                                        label = {
                                            //оболочка текущего чата
                                            ChatNameBubble(
                                                chat = item, //чат
                                                searchChat = searchChat, //строка поиска
                                                onClick = { //кнопка редактирования
                                                    selectedEntity = item
                                                    showUpdateDialog = true
                                                },
                                                onDelete = {//удаление
                                                    if (item.indexAt != currentChatIndex) {
                                                        chatViewModel.deleteChat(item)
                                                    }
                                                },
                                                onChooseFavorite = {//добавление или удаление из избранного
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
                                        onClick = {//нажатие на чат

                                            //выбор чата
                                            chatViewModel.selectChat(item.indexAt)
                                            //вывод сообщений по чату
                                            chatViewModel.getMessagesById(item.indexAt)
                                            //очистка строк поиска в чатах и сообщениях
                                            chatViewModel.onSearchChatChange("")
                                            chatViewModel.onSearchTextChange("")
                                            //увеличение числа кликов
                                            val chatModel =
                                                item.copy(clicks = item.clicks+1)
                                            chatViewModel.updateChat(chatModel)
                                            //закрытие бокового меню
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
                            //строка поиска сообщений в чате
                            SearchMessageBar(
                                chatViewModel = chatViewModel,
                                searchText = searchText,
                                onClick = {
                                    //массив индексов сообщений, где найдены совпадения
                                    val foundIndexList = mutableListOf<Int>()
                                    var num: Int = 0
                                    chatViewModel.messages.forEach{ it->
                                        //добавляем совпадения в массив
                                        if (it.first.contains(searchText, ignoreCase = true))
                                            foundIndexList.add(num)
                                        num++
                                    }
                                    num = 0
                                    //наименьший индекс сообщения, который видит пользователь
                                    val visibleItemCount = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0 + 1
                                    //находим совпадение, которое находится выше экрана пользователя
                                    while (num<foundIndexList.size && foundIndexList[num] < visibleItemCount){
                                        num++
                                    }
                                    //смещаемся
                                    scope.launch {
                                        listState.animateScrollToItem(foundIndexList[max(0,num-1)])
                                    }
                                })
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
                        //Строка с вводом сообщений, выбором rag и использованием микрофона
                        BottomBar(
                            viewModel,
                            chatViewModel,
                            ragViewModel,
                            currentChatIndex ?:0,
                            voiceToTextParser
                        )
                    }

                }
            ) { padding ->

                //экран для вывода сообщений
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