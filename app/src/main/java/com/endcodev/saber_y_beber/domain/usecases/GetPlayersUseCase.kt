package com.endcodev.saber_y_beber.domain.usecases

import android.util.Log
import com.endcodev.saber_y_beber.data.database.entities.PlayerEntity
import com.endcodev.saber_y_beber.data.repository.PlayerRepository
import com.endcodev.saber_y_beber.domain.model.PlayersModel
import com.endcodev.saber_y_beber.domain.utils.App
import javax.inject.Inject

class GetPlayersUseCase @Inject constructor(
    private val repository: PlayerRepository
) {

    suspend operator fun invoke(): List<PlayersModel> {
        var playerList: List<PlayersModel>? = null

        try {
            playerList = repository.getAllPlayersFromDB()
            Log.v(App.tag, "players found $playerList")
        } catch (e: Exception) {
            Log.e(App.tag, "No players found, $e")
        }
        if (playerList.isNullOrEmpty()) {
            Log.v(App.tag, "null $playerList")
            repository.insertAllPlayers(examplePlayers())
            playerList = repository.getAllPlayersFromDB()
        }
        return playerList
    }

    private fun examplePlayers(): MutableList<PlayerEntity> {
        return arrayListOf(
            PlayerEntity(1, 0, "Player 1", 0),
            PlayerEntity(2, 1, "Player 2", 0),
            PlayerEntity(3, 2, "Horse Luis", 0)
        )
    }

    fun savePlayer(player: PlayerEntity?) {
        if (player != null) {
            Log.e(App.tag, "player inserted")
            repository.insertPlayer(player)
        }
    }

    fun deletePlayer(name: String) {
        repository.deletePlayer(name)
        Log.e(App.tag, "player deleted $name")
    }
}