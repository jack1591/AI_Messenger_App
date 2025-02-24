package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp

@Composable
fun PatternsRAGScreen(){

    Column(
        modifier = Modifier
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 20.dp)
                .clickable { Log.i("RAG", "Person is clicked") }
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Person")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedCard(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 20.dp)
                .clickable { Log.i("RAG", "Person is clicked") }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Place")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedCard(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 20.dp)
                .clickable { Log.i("RAG", "Person is clicked") },
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Goal")
            }
        }
    }

}