package com.endcodev.saber_y_beber.domain.usecases

import android.util.Log
import com.endcodev.saber_y_beber.data.network.FirebaseClient
import com.endcodev.saber_y_beber.data.repository.CorrectionRepository
import com.endcodev.saber_y_beber.data.repository.GameRepository
import com.endcodev.saber_y_beber.domain.model.CorrectionModel
import com.endcodev.saber_y_beber.domain.model.ProfileModel
import com.endcodev.saber_y_beber.domain.model.QuestModel
import com.endcodev.saber_y_beber.domain.utils.App
import javax.inject.Inject


class GetAllFromUidUseCase @Inject constructor(
    private val repository: CorrectionRepository,
    private val firebase: FirebaseClient,
    private val gameRepository: GameRepository
) {

    suspend operator fun invoke(): List<ProfileModel> {
        val correctionList: List<CorrectionModel>?
        val questList: List<QuestModel>?
        val filteredCorrectionList: MutableList<ProfileModel> = mutableListOf()

        try {
            val uid = firebase.auth.uid
            correctionList = repository.getAllCorrectionsFromApi()

            if (correctionList.isNotEmpty()) {
                for (item in correctionList) {
                    if (item.correctors[0].id == uid) {
                        filteredCorrectionList.add(
                            ProfileModel(
                                quest = item.correction,
                                user = item.author,
                                result = false
                            )
                        )
                    }
                }
            }

            questList = gameRepository.getAllQuestFromApi()
            if (questList.isNotEmpty()) {
                for (item in questList) {
                    if (item.correctors != null && item.correctors[0].id == uid) {
                        filteredCorrectionList.add(
                            ProfileModel(
                                quest = item.quest,
                                user = item.author,
                                result = true
                            )
                        )
                    }
                }
            }

        } catch (e: Exception) {
            Log.e(App.tag, "No reply from  quest retrofit API")
        }
        return filteredCorrectionList
    }
}