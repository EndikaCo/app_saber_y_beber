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
        const val TAG = "GetQuestUseCase***"
    }

    suspend operator fun invoke(): List<QuestModel> {
        var questList: List<QuestModel>? = null

        try {
            // Attempt to get all quests from the API
            questList = repository.getAllQuestFromApi()
        } catch (e: Exception) {
            Log.e(TAG, "No reply from quest retrofit API")
            // Retrieve all quests from the database instead
            repository.getAllQuestFromDB()
        }

        // If the questList is not null or empty, update the database with the new quests and return the list
        return if (!questList.isNullOrEmpty()) {
            repository.clearQuest()
            repository.insertQuest(questList.map { it.toDB() })
            questList
        } else {
            // If the questList is null or empty, retrieve all quests from the database
            repository.getAllQuestFromDB()
        }
    }
}