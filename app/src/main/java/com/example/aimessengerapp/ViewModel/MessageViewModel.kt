package com.example.aimessengerapp.ViewModel

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aimessengerapp.api.NetworkResponse
import com.example.aimessengerapp.api.RequestModel
import com.example.aimessengerapp.api.ResponseModel
import com.example.aimessengerapp.api.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {

    private val messageAPI = RetrofitInstance.messageApi
    private val _messageResult = MutableLiveData<NetworkResponse<ResponseModel>>()
    val messageResult : LiveData<NetworkResponse<ResponseModel>> = _messageResult

    var request by mutableStateOf("")


    fun getData(requestModel: RequestModel){
        _messageResult.value = NetworkResponse.Loading

        try{
            viewModelScope.launch {
                val response = messageAPI.postData(requestModel)
                if (response.isSuccessful){
                    response.body()?.let{
                        _messageResult.value = NetworkResponse.Success(it)
                    }
                }
                else {
                    _messageResult.value = NetworkResponse.Error("Unable to load data")
                }
            }
        }
        catch(e: Exception){
            _messageResult.value = NetworkResponse.Error(e.message.toString())
        }
    }

    fun clearResponse(){
        _messageResult.value = NetworkResponse.Waiting
    }


}