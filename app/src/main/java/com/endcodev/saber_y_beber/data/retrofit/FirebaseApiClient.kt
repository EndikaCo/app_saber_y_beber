package com.endcodev.saber_y_beber.data.retrofit

import com.endcodev.saber_y_beber.data.model.CorrectionModel
import retrofit2.http.GET

interface FirebaseApiClient {

    @GET("corrections/.json")
    suspend fun getAllCorrections(): retrofit2.Response<List<CorrectionModel>>

}