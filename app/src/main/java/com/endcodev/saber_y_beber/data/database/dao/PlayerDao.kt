package com.endcodev.saber_y_beber.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.endcodev.saber_y_beber.data.database.entities.PlayerEntity

@Dao
interface PlayerDao {

    @Query("SELECT * FROM player_table")
    suspend fun getAllPlayers(): List<PlayerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPlayers(player: List<PlayerEntity>)

    @Query("DELETE FROM player_table")
    suspend fun deleteAllPlayers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(player: PlayerEntity)

    @Query("DELETE FROM player_table WHERE name = :name")
    fun deletePlayer(name: String)

}