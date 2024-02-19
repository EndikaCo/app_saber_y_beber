package com.endcodev.saber_y_beber.data.repository

import com.endcodev.saber_y_beber.data.database.dao.ChallengeDao
import com.endcodev.saber_y_beber.data.database.dao.QuestDao
import com.endcodev.saber_y_beber.data.database.entities.ChallengeEntity
import com.endcodev.saber_y_beber.data.database.entities.QuestEntity
import com.endcodev.saber_y_beber.data.retrofit.FirebaseService
import com.endcodev.saber_y_beber.domain.model.ChallengeModel
import com.endcodev.saber_y_beber.domain.model.QuestModel
import com.endcodev.saber_y_beber.domain.model.toDomain
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val api: FirebaseService,
    private val questDao: QuestDao,
    private val challengeDao: ChallengeDao,
) {
    suspend fun getAllQuestFromApi(): List<QuestModel> {
        val response: List<QuestModel> = api.getQuests()
        return response.map { it.toDomain() }
    }

    suspend fun getAllQuestFromDB(): List<QuestModel> {
        val response: List<QuestEntity> = questDao.getAllQuest()
        return response.map { it.toDomain() }
    }

    suspend fun insertQuest(questList: List<QuestEntity>) {
        questDao.insertAllQuest(questList.shuffled())
    }

    suspend fun clearQuest() {
        questDao.deleteAllQuest()
    }

    suspend fun getAllChallengesFromApi(): List<ChallengeModel> {
        val response: List<ChallengeModel> = api.getChallenges()
        return response.map { it.toDomain() }
    }

    suspend fun getAllChallengesFromDB(): List<ChallengeModel> {
        val response: List<ChallengeEntity> = challengeDao.getAllChallenges()
        return response.map { it.toDomain() }
    }

    suspend fun insertChallenges(challenges: List<ChallengeEntity>) {
        challengeDao.insertAllChallenge(challenges)
    }

    suspend fun clearChallenges() {
        challengeDao.deleteAllChallenges()
    }
}