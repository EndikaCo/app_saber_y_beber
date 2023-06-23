package com.endcodev.saber_y_beber.data.model

data class CorrectionModel(
    var correction: String,
    var author: String,
    var option1: String,
    var option2: String,
    var option3: String,
    val difficulty: Int,
    val fail: String,
    val rating: Int,
    val correctors: ArrayList<CorrectorModel>
)