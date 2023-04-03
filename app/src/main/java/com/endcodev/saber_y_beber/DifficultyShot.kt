package com.endcodev.saber_y_beber

import android.widget.ImageView

class DifficultyShot(private val ShotsImage: ImageView) {

    fun setDifficulty(difficulty: Int) {
        when (difficulty) {
            0 -> ShotsImage.setBackgroundResource(R.drawable.difficulty_0)
            1 -> ShotsImage.setBackgroundResource(R.drawable.difficulty_1)
            2 -> ShotsImage.setBackgroundResource(R.drawable.difficulty_2)
            3 -> ShotsImage.setBackgroundResource(R.drawable.difficulty_3)
        }
    }
}