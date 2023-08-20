package com.endcodev.saber_y_beber.data.model

import com.endcodev.saber_y_beber.data.database.entities.QuestEntity

data class QuestModel(
    var quest: String,
    var author: String,
    var option1: String,
    var option2: String,
    var option3: String,
    val difficulty: Int,
    val post: Boolean
)

fun QuestModel.toDomain() =
    QuestModel(quest, author, option1, option2, option3, difficulty, post)

fun QuestEntity.toDomain() =
    QuestModel(quest, author, option1, option2, option3, difficulty, post)