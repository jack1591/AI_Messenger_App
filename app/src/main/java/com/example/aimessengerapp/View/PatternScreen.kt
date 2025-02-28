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
import com.example.aimessengerapp.RAGRepositories.Goal
import com.example.aimessengerapp.RAGRepositories.Location
import com.example.aimessengerapp.RAGRepositories.Person
import com.example.aimessengerapp.ViewModel.RAGViewModel
import kotlinx.coroutines.flow.first

@Composable
fun PatternScreen(ragViewModel: RAGViewModel,type: String){
    var objectName by remember{
        mutableStateOf("")
    }

    val persons by ragViewModel.persons.collectAsState(emptyList())
    val locations by ragViewModel.locations.collectAsState(emptyList())
    val goals by ragViewModel.goals.collectAsState(emptyList())

    var showDialog by rememberSaveable{
        mutableStateOf(false)
    }

    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }

    var selectedPerson by remember { mutableStateOf<Person?>(null) }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var selectedGoal by remember { mutableStateOf<Goal?>(null) }

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
                if (type=="Person"){
                    items(persons) { person ->
                        PatternObjectBubble(
                            person = person,
                            type = "Person",
                            onUpdate = { selectedPerson = person; ragViewModel.updatePerson(person); showUpdateDialog = true }, // ✅ Открываем `AlertDialog`
                            onDelete = { ragViewModel.deletePerson(person) }
                        )
                    }
                }
                else if (type=="Location"){
                    items(locations) { location ->
                        PatternObjectBubble(
                            location = location,
                            type = "Location",
                            onUpdate = { Log.i("location","update location ${location.name}"); selectedLocation = location; ragViewModel.updateLocation(location); showUpdateDialog = true }, // ✅ Открываем `AlertDialog`
                            onDelete = { ragViewModel.deleteLocation(location) }
                        )
                    }
                }
                else {
                    items(goals) { goal ->
                        PatternObjectBubble(
                            goal = goal,
                            type = "Goal",
                            onUpdate = { selectedGoal = goal; ragViewModel.updateGoal(goal); showUpdateDialog = true }, // ✅ Открываем `AlertDialog`
                            onDelete = { ragViewModel.deleteGoal(goal) }
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
                if (type=="Person") {
                    val personModel = Person(name = name)
                    ragViewModel.addPerson(personModel)
                }
                else if (type=="Location") {
                    val locationModel = Location(name = name)
                    ragViewModel.addLocation(locationModel)
                }
                else {
                    val goalModel = Goal(name = name)
                    ragViewModel.addGoal(goalModel)
                }
            }
        })

    if (type=="Person") {
        UpdateDialog(
            person = selectedPerson,
            showDialog = showUpdateDialog,
            onDismiss = { showUpdateDialog = false },
            onConfirm = { newName ->
                selectedPerson?.let {
                    Log.i("textState", newName)
                    val personModel = Person(id = it.id, name = newName)
                    ragViewModel.updatePerson(personModel)
                }
                showUpdateDialog = false
            })
    }
    else if (type=="Location"){
        Log.i("location","lets update")
        UpdateDialog(
            location = selectedLocation,
            showDialog = showUpdateDialog,
            onDismiss = { showUpdateDialog = false },
            onConfirm = { newName ->
                selectedLocation?.let {
                    Log.i("textState", newName)
                    val locationModel = Location(id = it.id, name = newName)
                    ragViewModel.updateLocation(locationModel)
                }
                showUpdateDialog = false
            })
    }
    else {
        UpdateDialog(
            goal = selectedGoal,
            showDialog = showUpdateDialog,
            onDismiss = { showUpdateDialog = false },
            onConfirm = { newName ->
                selectedGoal?.let {
                    Log.i("textState", newName)
                    val goalModel = Goal(id = it.id, name = newName)
                    ragViewModel.updateGoal(goalModel)
                }
                showUpdateDialog = false
            })
    }
}