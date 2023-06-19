package com.endcodev.saber_y_beber.domain

import android.util.Log
import com.endcodev.saber_y_beber.data.database.entities.PlayerEntity
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.data.repository.PlayerRepository
import javax.inject.Inject

class GetPlayersUseCase @Inject constructor(
    private val repository: PlayerRepository
) {

    companion object {
        const val TAG = "Get player use case Log:"
    }

    suspend operator fun invoke(): List<PlayersModel> {
        var playerList: List<PlayersModel>? = null

        try {
            playerList = repository.getAllPlayersFromDB()
            Log.e(TAG, "players found $playerList")
        } catch (e: Exception) {
            Log.e(TAG, "No players found")
        }
        if (playerList.isNullOrEmpty()){
            Log.e(TAG, "null $playerList")
            repository.insertAllPlayers(examplePlayers())
            playerList = repository.getAllPlayersFromDB()
        }
        return playerList
    }

    private fun examplePlayers() : MutableList<PlayerEntity>{
        return arrayListOf(
            PlayerEntity(1,0, "Player 1", 0),
            PlayerEntity(2,1, "Player 2", 0),
            PlayerEntity(3,2, "Horse Luis", 0)
        )
    }

     suspend fun saveAllPlayers(playersList: List<PlayerEntity>?)
    {
        if (playersList != null) {
            Log.e(TAG, "save players $playersList")
            repository.insertAllPlayers(playersList)
            Log.e(TAG, "new players ${repository.getAllPlayersFromDB()}")
        }
    }

    fun savePlayer(player : PlayerEntity?)
    {
        if (player != null) {
            Log.e(TAG, "player inserted")
            repository.insertPlayer(player)
        }
    }

    suspend fun deleteAllPlayers() {
        repository.clearAllPlayers()
        Log.e(TAG, "all players deleted")
    }

     fun deletePlayer(name : String) {
        repository.deletePlayer(name)
        Log.e(TAG, "player deleted $name")
    }
}