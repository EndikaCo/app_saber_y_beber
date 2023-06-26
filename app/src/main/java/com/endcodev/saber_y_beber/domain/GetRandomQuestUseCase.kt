package com.endcodev.saber_y_beber.domain

import android.util.Log
import com.endcodev.saber_y_beber.data.repository.GameRepository
import com.endcodev.saber_y_beber.data.model.QuestModel

import javax.inject.Inject

class GetRandomQuestUseCase @Inject constructor(
    private val repository: GameRepository
) {

    companion object {
        const val TAG = "GetRandomQuestUseCase:"
    }

    private lateinit var questList: List<QuestModel?>
    private var i = 0
    suspend operator fun invoke() {
        questList = repository.getAllQuestFromDB().shuffled()
    }

    fun nextQuest(): QuestModel? {
        Log.v(TAG, "quest i:$i")
        return questList[i++]
    }
}