package com.example.aimessengerapp

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.aimessengerapp.api.NetworkResponse
import com.example.aimessengerapp.api.RequestModel

@Composable
fun MessengerPage(viewModel: MessageViewModel){

    var request by remember{
        mutableStateOf("")
    }
    val messageResult = viewModel.messageResult.observeAsState()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = request,
                onValueChange = {
                    request = it
                },
                label = {
                    Text(text = "ask something")
                })
            IconButton(onClick = {
                val requestModel = RequestModel(request)
                viewModel.getData(requestModel)
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search_location")
            }
        }
        when (val result = messageResult.value){
            is NetworkResponse.Success -> {
                Text(text = result.data.response.toString())
            }
            is NetworkResponse.Error ->{
                Text(text = result.message)
            }
            is NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            null -> {}
        }

    }
}