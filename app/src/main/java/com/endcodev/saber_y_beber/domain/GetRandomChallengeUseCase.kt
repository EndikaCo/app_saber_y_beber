package com.endcodev.saber_y_beber.domain

import android.util.Log
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.ChallengeModel
import com.endcodev.saber_y_beber.data.model.ErrorModel
import com.endcodev.saber_y_beber.data.repository.GameRepository
import com.endcodev.saber_y_beber.presenter.dialogs.ErrorDialogFragment
import com.endcodev.saber_y_beber.presenter.utils.ResourcesProvider
import javax.inject.Inject
import kotlin.system.exitProcess

class GetRandomChallengeUseCase @Inject constructor(
    private val repository: GameRepository,
    private val resources: ResourcesProvider
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
        return if (challengeList.size > i)
            challengeList[i++]
        else
            null
    }

    fun startChallenge(): ChallengeModel {
        return ChallengeModel(
            resources.getString(R.string.game_start_round_title),
            resources.getString(R.string.game_start_round_text),
            resources.getString(R.string.game_start_round_author),
            1
        )
    }

    fun finalChallenge(round: Int, player: String): ChallengeModel {
        return ChallengeModel(
            resources.getString(R.string.fin_ronda, round),
            resources.getString(R.string.final_ranking_lead, player),
            "Developer",
            3
        )
    }

}