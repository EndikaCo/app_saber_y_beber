package com.endcodev.saber_y_beber.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.endcodev.saber_y_beber.data.model.QuestModel

@Entity(tableName = "quest_table")
data class QuestEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "challenge") val challenge: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "answer") val answer: Int,
    @ColumnInfo(name = "option1") val option1: String,
    @ColumnInfo(name = "option2") val option2: String,
    @ColumnInfo(name = "option3") val option3: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int,
    @ColumnInfo(name = "fail") val fail: String
)

fun QuestModel.toDB() = QuestEntity(
    challenge = challenge, author = author, answer = answer, option1 = option1,
    option2 = option2, option3 = option3, difficulty = difficulty, fail = fail
)
