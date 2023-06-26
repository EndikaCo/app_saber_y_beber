package com.endcodev.saber_y_beber.data.model

import com.endcodev.saber_y_beber.data.database.entities.QuestEntity

data class QuestModel(
    var challenge: String,
    var author: String,
    var answer: Int,
    var option1: String,
    var option2: String,
    var option3: String,
    val difficulty: Int,
    val fail: String
)

fun QuestModel.toDomain() =
    QuestModel(challenge, author, answer, option1, option2, option3, difficulty, fail)

fun QuestEntity.toDomain() =
    QuestModel(challenge, author, answer, option1, option2, option3, difficulty, fail)