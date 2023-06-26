package com.endcodev.saber_y_beber.domain

import android.util.Log
import com.endcodev.saber_y_beber.data.database.entities.toDB
import com.endcodev.saber_y_beber.data.model.ChallengeModel
import com.endcodev.saber_y_beber.data.repository.GameRepository

import javax.inject.Inject

class GetChallengeUseCase @Inject constructor(
    private val repository: GameRepository
) {

    companion object {
        const val TAG = "GetChallengeUseCase:"
    }

    suspend operator fun invoke(): List<ChallengeModel> {
        var challengeList: List<ChallengeModel>? = null

        try {
            challengeList = repository.getAllChallengesFromApi()
            Log.e(TAG, "Challenges from api $challengeList")
        } catch (e: Exception) {
            Log.e(TAG, "No reply from Challenges retrofit API")
            repository.getAllChallengesFromDB()
        }
        return if (!challengeList.isNullOrEmpty()) {
            repository.clearChallenges()
            repository.insertChallenges(challengeList.map { it.toDB() })
            challengeList
        } else
            repository.getAllChallengesFromDB()
    }
}