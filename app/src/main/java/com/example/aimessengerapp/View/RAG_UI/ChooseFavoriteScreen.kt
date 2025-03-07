package com.example.aimessengerapp.View.RAG_UI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChooseFavoriteScreen(
    onAll: () -> Unit,
    onFavorite: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Button(modifier = Modifier
            .padding(5.dp),
            onClick = {
                onFavorite()
            }
        ) {
            Text(text = "FAVORITE", fontSize = 10.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(modifier = Modifier
            .padding(5.dp),
            onClick = {
                onAll()
            }
        ) {
            Text(text = "ALL", fontSize = 10.sp)
        }
    }
}