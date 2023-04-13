package com.endcodev.saber_y_beber.domain

import android.widget.ImageView
import com.endcodev.saber_y_beber.R

class SetDifficultyUseCase(private val ShotsImage: ImageView) {

    fun setDifficulty(difficulty: Int) {
        when (difficulty) {
            0 -> ShotsImage.setBackgroundResource(R.drawable.difficulty_0)
            1 -> ShotsImage.setBackgroundResource(R.drawable.difficulty_1)
            2 -> ShotsImage.setBackgroundResource(R.drawable.difficulty_2)
            3 -> ShotsImage.setBackgroundResource(R.drawable.difficulty_3)
        }
    }
}