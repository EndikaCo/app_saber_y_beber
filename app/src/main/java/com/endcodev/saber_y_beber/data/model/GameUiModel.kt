package com.endcodev.saber_y_beber.data.model


data class GameUiModel(
    var quest: String,
    var author: String,
    var answer: Int,
    var option1: String?,
    var option2: String?,
    var option3: String?,
    val difficulty: Int,
    val title: String,
    val round: Int,
    var optionsColor: Int,
    var answered: Int,
    val actualPlayer: Int,
    val report: Float,
    val ranking: Int,
    var optionSelected : Int,
)




