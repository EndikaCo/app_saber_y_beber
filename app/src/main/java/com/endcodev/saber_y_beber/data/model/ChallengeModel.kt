package com.endcodev.saber_y_beber.data.model

import com.endcodev.saber_y_beber.data.database.entities.ChallengeEntity

data class ChallengeModel(
    val title: String,
    var challenge: String,
    val author: String,
    val rating: Int
)

fun ChallengeModel.toDomain() = ChallengeModel(title, challenge, author, rating)
fun ChallengeEntity.toDomain() = ChallengeModel(title, challenge, author, rating)