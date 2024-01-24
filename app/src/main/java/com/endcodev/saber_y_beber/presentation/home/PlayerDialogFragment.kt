package com.endcodev.saber_y_beber.presentation.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.DialogFragmentPlayerBinding
import com.endcodev.saber_y_beber.domain.model.PlayersModel

open class PlayerDialogFragment(
    private val onSubmitClickListener: (PlayersModel) -> Unit
) : androidx.fragment.app.DialogFragment() {

    private lateinit var binding: DialogFragmentPlayerBinding
    private var image: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogFragmentPlayerBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.idName.requestFocus()

        initListeners()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun initListeners() {

        with(binding) {

            idName.onFocusChangeListener = OnFocusChangeListener { _, _ ->
                textUpdate()
            }

            idContinue.setOnClickListener {
                acceptPlayer()
            }

            errorCancel2.setOnClickListener {
                dismiss()
            }

            idPicture.setOnClickListener {
                changeGender()
            }
        }
    }

    private fun acceptPlayer() {
        if (binding.idName.text.isNullOrEmpty())
            binding.idName.error = "Cant be empty"
        else {
            val player = PlayersModel(image, binding.idName.text.toString(), 0)
            onSubmitClickListener.invoke(player)
            dismiss()
        }
    }

    private fun textUpdate() {
        with(binding) {
            idName.postDelayed({
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(
                    idName,
                    InputMethodManager.SHOW_IMPLICIT
                )
            }, 200)
        }
    }

    private fun changeGender() {
        with(binding) {
            when (image) {
                0 -> {
                    idPicture.setImageResource(R.drawable.player_girl)
                    idGenre.text = "F"
                    image++
                }

                1 -> {
                    idPicture.setImageResource(R.drawable.player_horse)
                    idGenre.text = "-"
                    image++
                }

                2 -> {
                    image = 0
                    idGenre.text = "M"
                    idPicture.setImageResource(R.drawable.player_boy)
                }

                else -> {}
            }
        }
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}


