package com.endcodev.saber_y_beber.data.retrofit
import com.endcodev.saber_y_beber.data.model.ChallengeModel
import com.endcodev.saber_y_beber.data.model.CorrectionModel
import com.endcodev.saber_y_beber.data.model.QuestModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseService @Inject constructor(
    private val api: FirebaseApiClient
) {

    suspend fun getQuests(): List<QuestModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getAllQuest()
            response.body() ?: emptyList()
        }
    }

    suspend fun getChallenges(): List<ChallengeModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getAllChallenges()
            response.body() ?: emptyList()
        }
    }

    suspend fun getCorrections(): List<CorrectionModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getAllCorrections()
            response.body() ?: emptyList()
        }
    }
}