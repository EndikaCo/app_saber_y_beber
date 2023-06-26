package com.endcodev.saber_y_beber.presenter.game

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.endcodev.saber_y_beber.R

class OptionButtons(
    private val options: RadioGroup,
    private val btOption1: RadioButton,
    private val btOption2: RadioButton,
    private val btOption3: RadioButton,
    private val context: Context?
) {
    fun optionColor(correct: Int, select: Int) {
        options.visibility = View.VISIBLE

        if (correct == 1) {
            changeDrawableColor(btOption1, 1, 1, select)
            changeDrawableColor(btOption2, 2, 0, select)
            changeDrawableColor(btOption3, 3, 0, select)
        }
        if (correct == 2) {
            changeDrawableColor(btOption1, 1, 0, select)
            changeDrawableColor(btOption2, 2, 1, select)
            changeDrawableColor(btOption3, 3, 0, select)
        }
        if (correct == 3) {
            changeDrawableColor(btOption1, 1, 0, select)
            changeDrawableColor(btOption2, 2, 0, select)
            changeDrawableColor(btOption3, 3, 1, select)
        }
        if (correct == -1) {
            changeDrawableColor(btOption1, 1, -1, select)
            changeDrawableColor(btOption2, 2, -1, select)
            changeDrawableColor(btOption3, 3, -1, select)
        }
        if (correct == -2) {
            options.visibility = View.GONE
        }
    }

    //changes drawables color and drawable images in options (RadioGroup)
    private fun changeDrawableColor(button: RadioButton, option: Int, value: Int, select: Int) {
        val color: Int
        val drawableRight: Drawable?



        val drawableLeft = when (option) {
            1 -> context?.let { ContextCompat.getDrawable(it, R.drawable.ic_option_a) }
            2 -> context?.let { ContextCompat.getDrawable(it, R.drawable.ic_option_b) }
            else ->    context?.let { ContextCompat.getDrawable(it, R.drawable.ic_option_c) }
        }
        drawableLeft!!.setBounds(0, 0, 100, 100)

        when (value) {
            1 -> {
                drawableRight = ContextCompat.getDrawable(context!!, R.drawable.ic_check)
                color = ContextCompat.getColor(context, R.color.green)
                button.setBackgroundResource(R.drawable.answer_option)
            }
            0 -> {
                drawableRight = ContextCompat.getDrawable(context!!,R.drawable.ic_close)
                color = ContextCompat.getColor(context, R.color.red)
                button.setBackgroundResource(R.drawable.answer_option)
            }
            else -> {
                drawableRight = null
                color = ContextCompat.getColor(context!!, R.color.black)
                button.setBackgroundResource(R.drawable.answer_option)
            }
        }

        when (select) {
            1 ->
                btOption1.setBackgroundResource(R.drawable.answer_option_correct)
            2 ->
                btOption2.setBackgroundResource(R.drawable.answer_option_correct)
            3 ->
                btOption3.setBackgroundResource(R.drawable.answer_option_correct)
        }
        drawableRight?.setBounds(0, 0, 100, 100)
        DrawableCompat.setTint(drawableLeft, color)
        button.setCompoundDrawables(drawableLeft, null, drawableRight, null)
    }
}