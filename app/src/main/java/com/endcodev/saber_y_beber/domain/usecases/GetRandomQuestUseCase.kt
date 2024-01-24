package com.endcodev.saber_y_beber.domain.usecases

import com.endcodev.saber_y_beber.data.repository.GameRepository
import com.endcodev.saber_y_beber.domain.model.QuestModel
import javax.inject.Inject

class GetRandomQuestUseCase @Inject constructor(
    private val repository: GameRepository
) {

    private lateinit var questList: List<QuestModel?>
    private var i = 0

    suspend operator fun invoke() {
        questList = repository.getAllQuestFromDB().shuffled()
    }

    fun nextQuest(): QuestModel? {
        return if (questList.size > i)
            questList[i++]
        else
            null
    }
}