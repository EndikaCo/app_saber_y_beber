package com.endcodev.saber_y_beber.data.repository

import com.endcodev.saber_y_beber.domain.model.CorrectionModel
import com.endcodev.saber_y_beber.data.retrofit.FirebaseService
import javax.inject.Inject

class CorrectionRepository @Inject constructor(
    private val api: FirebaseService,
) {
    suspend fun getAllCorrectionsFromApi(): List<CorrectionModel> {
        return api.getCorrections()
    }
}