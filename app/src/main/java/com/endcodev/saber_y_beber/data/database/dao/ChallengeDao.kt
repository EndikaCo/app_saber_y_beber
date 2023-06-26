package com.endcodev.saber_y_beber.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.endcodev.saber_y_beber.data.database.entities.ChallengeEntity

@Dao
interface ChallengeDao {

    @Query("SELECT * FROM challenge_table")
    suspend fun getAllChallenges(): List<ChallengeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllChallenge(challenges: List<ChallengeEntity>)

    @Query("DELETE FROM challenge_table")
    suspend fun deleteAllChallenges()
}