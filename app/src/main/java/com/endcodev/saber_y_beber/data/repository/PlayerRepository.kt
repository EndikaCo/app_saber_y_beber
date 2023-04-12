package com.endcodev.saber_y_beber.data.repository

import com.endcodev.saber_y_beber.data.database.dao.PlayerDao
import com.endcodev.saber_y_beber.data.database.entities.PlayerEntity
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.data.model.toDomain
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val playerDao: PlayerDao,
) {
    suspend fun getAllPlayersFromDB(): List<PlayersModel> {
        val response: List<PlayerEntity> = playerDao.getAllPlayers()
        return response.map { it.toDomain() }
    }

    fun insertPlayer(player: PlayerEntity) {
        playerDao.insertPlayer(player)
    }

    fun deletePlayer(name : String) {
        playerDao.deletePlayer(name)
    }

    suspend fun insertAllPlayers(playerList: List<PlayerEntity>) {
        playerDao.insertAllPlayers(playerList)
    }

    suspend fun clearAllPlayers() {
        playerDao.deleteAllPlayers()
    }
}