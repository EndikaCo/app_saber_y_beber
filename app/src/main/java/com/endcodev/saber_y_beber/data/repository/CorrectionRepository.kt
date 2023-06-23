package com.endcodev.saber_y_beber.data.repository

import com.endcodev.saber_y_beber.data.model.CorrectionModel
import com.endcodev.saber_y_beber.data.retrofit.FirebaseService
import javax.inject.Inject

class CorrectionRepository @Inject constructor(
    private val api: FirebaseService,
) {

    suspend fun getAllCorrections(): List<CorrectionModel> {
        return api.getCorrections()
    }
}