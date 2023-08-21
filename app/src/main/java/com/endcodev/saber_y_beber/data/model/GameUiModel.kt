package com.endcodev.saber_y_beber.data.model

/**
 * @param quest - The question
 * @param author - The author of the question
 * @param option1 - The first option
 * @param option2 - The second option
 * @param option3 - The third option
 * @param difficulty - The difficulty of the question
 * @param title - The title
 * @param round - The round
 * @param answered - state of the quest
 */
data class GameUiModel(
    var quest: String,
    var author: String,
    var option1: OptionModel?,
    var option2: OptionModel?,
    var option3: OptionModel?,
    val difficulty: Int,
    val title: String,
    val round: Int,
    var answered: Boolean
)




