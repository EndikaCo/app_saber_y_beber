package com.endcodev.saber_y_beber.domain

import android.util.Log
import com.endcodev.saber_y_beber.data.model.ChallengeModel
import com.endcodev.saber_y_beber.data.repository.GameRepository
import javax.inject.Inject

class GetRandomChallengeUseCase @Inject constructor(
    private val repository: GameRepository
) {
    companion object {
        const val TAG = "GetRandomChallengeUseCase:"
    }

    private lateinit var challengeList: List<ChallengeModel?>
    private var i = 0
    suspend operator fun invoke() {
        challengeList = repository.getAllChallengesFromDB().shuffled()
    }

    fun nextChallenge(): ChallengeModel? {
        Log.v(TAG, "challenge i:$i")
        return challengeList[i++]
    }

}