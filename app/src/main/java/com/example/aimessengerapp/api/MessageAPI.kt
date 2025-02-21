package com.example.aimessengerapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageAPI {

    @POST("generate")
    suspend fun postData(
        @Body request: RequestModel
    ): Response<ResponseModel>
}