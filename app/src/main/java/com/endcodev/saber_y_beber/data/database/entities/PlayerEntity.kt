package com.endcodev.saber_y_beber.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.endcodev.saber_y_beber.domain.model.PlayersModel

@Entity(tableName = "player_table")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "genre") val genre: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "points") val points: Int,
)

fun PlayersModel.toDb() = PlayerEntity(genre = genre, name = name, points = points)
