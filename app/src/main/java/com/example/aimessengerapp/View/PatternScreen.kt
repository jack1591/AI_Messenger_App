package com.example.aimessengerapp.View

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aimessengerapp.RAGRepositories.Person
import com.example.aimessengerapp.ViewModel.RAGViewModel
import kotlinx.coroutines.flow.first

@Composable
fun PatternScreen(ragViewModel: RAGViewModel,type: String){
    var objectName by remember{
        mutableStateOf("")
    }

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Column (

        ){
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(ragViewModel.persons.value) { personName ->
                    Text(text = personName.name)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = objectName,
                    onValueChange = {
                        objectName = it
                    },
                    label = {
                        Text(text = "add ${type}")
                    })
                Button(modifier = Modifier
                    .padding(5.dp),
                    onClick = {
                        if (objectName.isNotEmpty()) {
                            if (type == "Person") {
                                val personModel = Person(name = objectName)
                                ragViewModel.addPerson(personModel)
                                objectName = ""
                            }
                        }
                    }
                ) {
                    Text(text = "ADD", fontSize = 10.sp)
                }
            }
        }
    }
}