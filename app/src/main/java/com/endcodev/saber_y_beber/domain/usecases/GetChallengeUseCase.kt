package com.endcodev.saber_y_beber.domain.usecases

import android.util.Log
import com.endcodev.saber_y_beber.data.database.entities.toDB
import com.endcodev.saber_y_beber.data.repository.GameRepository
import com.endcodev.saber_y_beber.domain.model.ChallengeModel
import com.endcodev.saber_y_beber.domain.utils.App
import javax.inject.Inject

class GetChallengeUseCase @Inject constructor(
    private val repository: GameRepository
) {

    suspend operator fun invoke(): List<ChallengeModel> {
        var challengeList: List<ChallengeModel>? = null

        try {
            challengeList = repository.getAllChallengesFromApi()
            Log.e(App.tag, "Challenges from api $challengeList")
        } catch (e: Exception) {
            Log.e(App.tag, "No reply from Challenges retrofit API")
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