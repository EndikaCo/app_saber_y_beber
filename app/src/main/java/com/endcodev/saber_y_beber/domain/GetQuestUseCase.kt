package com.endcodev.saber_y_beber.domain

import android.util.Log
import com.endcodev.saber_y_beber.data.database.entities.toDB
import com.endcodev.saber_y_beber.data.repository.GameRepository
import com.endcodev.saber_y_beber.data.model.QuestModel
import javax.inject.Inject

class GetQuestUseCase @Inject constructor(
    private val repository: GameRepository
) {

    companion object {
        const val TAG = "GetQuestUseCase:"
    }

    suspend operator fun invoke(): List<QuestModel> {
        var questList: List<QuestModel>? = null

        try {
            questList = repository.getAllQuestFromApi()
        } catch (e: Exception) {
            Log.e(TAG, "No reply from  quest retrofit API")
            repository.getAllQuestFromDB()
        }
        return if (!questList.isNullOrEmpty()) {
            repository.clearQuest()
            repository.insertQuest(questList.map { it.toDB() })
            questList
        } else
            repository.getAllQuestFromDB()
    }
}