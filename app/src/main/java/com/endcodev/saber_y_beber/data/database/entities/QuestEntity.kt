package com.endcodev.saber_y_beber.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.endcodev.saber_y_beber.data.model.CorrectorModel
import com.endcodev.saber_y_beber.data.model.QuestModel

@Entity(tableName = "quest_table")
data class QuestEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "quest") val quest: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "option1") val option1: String,
    @ColumnInfo(name = "option2") val option2: String,
    @ColumnInfo(name = "option3") val option3: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int,
    @ColumnInfo(name = "post") val post: Boolean
)

fun QuestModel.toDB() = QuestEntity(
    quest = quest,
    author = author,
    option1 = option1,
    option2 = option2,
    option3 = option3,
    difficulty = difficulty,
    post = post
)
