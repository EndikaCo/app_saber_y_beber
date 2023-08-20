package com.endcodev.saber_y_beber.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.endcodev.saber_y_beber.data.model.ChallengeModel

@Entity(tableName = "challenge_table")
data class ChallengeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "challenge") val challenge: String,
    @ColumnInfo(name = "diff") val diff: Int,
    @ColumnInfo(name = "title") val title: String,

    )

fun ChallengeModel.toDB() =
    ChallengeEntity(challenge = challenge, author = author, diff = diff, title = title)