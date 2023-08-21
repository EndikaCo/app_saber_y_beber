package com.endcodev.saber_y_beber.domain

import android.util.Log
import com.endcodev.saber_y_beber.data.model.CorrectionModel
import com.endcodev.saber_y_beber.data.repository.CorrectionRepository
import javax.inject.Inject

class GetCorrectionsUseCase @Inject constructor(
    private val repository: CorrectionRepository
) {

    companion object {
        const val TAG = "GetCorrectionsUseCase:"
    }

    suspend operator fun invoke(): List<CorrectionModel>? {
        var quest: List<CorrectionModel>? = null

        try {
            quest = repository.getAllCorrectionsFromApi()
        } catch (e: Exception) {
            Log.e(TAG, "No reply from  quest retrofit API")
        }
        return quest
    }
}