package com.endcodev.saber_y_beber.data.model
import com.endcodev.saber_y_beber.data.database.entities.ChallengeEntity

data class ChallengeModel(
    val title: String,
    var challenge: String,
    val author: String,
    val diff: Int
)

fun ChallengeModel.toDomain() = ChallengeModel(title, challenge, author, diff)
fun ChallengeEntity.toDomain() = ChallengeModel("", challenge, author, diff)