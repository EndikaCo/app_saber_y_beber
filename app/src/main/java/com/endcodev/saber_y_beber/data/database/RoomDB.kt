package com.endcodev.saber_y_beber.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.endcodev.saber_y_beber.data.database.dao.ChallengeDao
import com.endcodev.saber_y_beber.data.database.dao.PlayerDao
import com.endcodev.saber_y_beber.data.database.dao.QuestDao
import com.endcodev.saber_y_beber.data.database.entities.ChallengeEntity
import com.endcodev.saber_y_beber.data.database.entities.PlayerEntity
import com.endcodev.saber_y_beber.data.database.entities.QuestEntity

@Database(
    entities = [
        QuestEntity::class,
        ChallengeEntity::class,
        PlayerEntity::class],
    version = 2
)
abstract class RoomDB : RoomDatabase() {
    abstract fun getQuestDao(): QuestDao
    abstract fun getChallengeDao(): ChallengeDao
    abstract fun getPlayerDao(): PlayerDao
}