package com.endcodev.saber_y_beber.data.model

import com.endcodev.saber_y_beber.data.database.entities.PlayerEntity

data class PlayersModel(
    val genre: Int,
    val name: String,
    var points: Int
)

fun PlayerEntity.toDomain() =
    PlayersModel(genre, name, points)