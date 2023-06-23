package com.endcodev.saber_y_beber.data.retrofit
import com.endcodev.saber_y_beber.data.model.CorrectionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseService @Inject constructor(
    private val api: FirebaseApiClient
) {

    suspend fun getCorrections(): List<CorrectionModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getAllCorrections()
            response.body() ?: emptyList()
        }
    }
}