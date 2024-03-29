package com.endcodev.saber_y_beber.domain.model

data class CorrectionModel(
    var correction: String,
    var author: String,
    var option1: String,
    var option2: String,
    var option3: String,
    val difficulty: Int,
    val correctors: ArrayList<CorrectorModel>
)

data class CorrectorModel(
    val id: String,
    val ok: Boolean,
)