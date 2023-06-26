package com.endcodev.saber_y_beber.data.model

data class EditTextErrorModel(
    var questError: EditTextError?,
    var CorrectError: EditTextError?,
    var option2Error: EditTextError?,
    var option3Error: EditTextError?,
    var feedbackError: EditTextError?,
    var alternativeError: EditTextError?,
    var difficultyError: EditTextError?,
)

data class EditTextError(
    val error: String,
    var type: Int,
)