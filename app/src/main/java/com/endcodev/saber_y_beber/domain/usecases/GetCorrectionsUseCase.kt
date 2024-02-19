package com.endcodev.saber_y_beber.domain.usecases

import android.util.Log
import com.endcodev.saber_y_beber.data.repository.CorrectionRepository
import com.endcodev.saber_y_beber.domain.model.CorrectionModel
import com.endcodev.saber_y_beber.domain.utils.App
import javax.inject.Inject

class GetCorrectionsUseCase @Inject constructor(
    private val repository: CorrectionRepository
) {
    suspend operator fun invoke(): List<CorrectionModel>? {
        var quest: List<CorrectionModel>? = null

        try {
            quest = repository.getAllCorrectionsFromApi()
        } catch (e: Exception) {
            Log.e(App.tag, "No reply from  quest retrofit API $e")
        }
        return quest
    }
}