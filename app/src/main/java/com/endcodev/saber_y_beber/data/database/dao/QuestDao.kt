package com.endcodev.saber_y_beber.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.endcodev.saber_y_beber.data.database.entities.QuestEntity

@Dao
interface QuestDao {

    @Query("SELECT * FROM quest_table")
    suspend fun getAllQuest(): List<QuestEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuest(quest: List<QuestEntity>)

    @Query("DELETE FROM quest_table")
    suspend fun deleteAllQuest()

}