package com.endcodev.saber_y_beber.domain.usecases

import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.domain.model.ChallengeModel
import com.endcodev.saber_y_beber.data.repository.GameRepository
import com.endcodev.saber_y_beber.presentation.utils.ResourcesProvider
import javax.inject.Inject

class GetRandomChallengeUseCase @Inject constructor(
    private val repository: GameRepository,
    private val resources: ResourcesProvider
) {

    private lateinit var challengeList: List<ChallengeModel?>
    private var i = 0

    suspend operator fun invoke() {
        challengeList = repository.getAllChallengesFromDB().shuffled()
    }

    fun nextChallenge(): ChallengeModel? {
        return if (challengeList.size > i)
            challengeList[i++]
        else
            null
    }

    fun startChallenge(): ChallengeModel { //todo
        return ChallengeModel(
            resources.getString(R.string.game_start_round_title),
            resources.getString(R.string.game_start_round_text),
            resources.getString(R.string.anonyme),
            1
        )
    }

    fun finalChallenge(round: Int, player: String): ChallengeModel { //todo
        return ChallengeModel(
            resources.getString(R.string.fin_ronda, round),
            resources.getString(R.string.final_ranking_lead, player),
            resources.getString(R.string.anonyme),
            3
        )
    }
}