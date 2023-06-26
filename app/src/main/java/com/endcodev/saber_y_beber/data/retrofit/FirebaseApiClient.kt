package com.endcodev.saber_y_beber.data.retrofit

import com.endcodev.saber_y_beber.data.model.ChallengeModel
import com.endcodev.saber_y_beber.data.model.CorrectionModel
import com.endcodev.saber_y_beber.data.model.QuestModel
import retrofit2.http.GET

interface FirebaseApiClient {

    @GET("quests/.json")
    suspend fun getAllQuest(): retrofit2.Response<List<QuestModel>>

    @GET("challenges/.json")
    suspend fun getAllChallenges(): retrofit2.Response<List<ChallengeModel>>

    @GET("corrections/.json")
    suspend fun getAllCorrections(): retrofit2.Response<List<CorrectionModel>>
}