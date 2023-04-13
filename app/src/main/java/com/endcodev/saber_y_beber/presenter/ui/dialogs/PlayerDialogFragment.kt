package com.endcodev.saber_y_beber.presenter.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.databinding.DialogFragmentPlayerBinding

open class PlayerDialogFragment(
    private val onSubmitClickListener: (PlayersModel) -> Unit
) : androidx.fragment.app.DialogFragment() {

    private lateinit var binding: DialogFragmentPlayerBinding
    var image: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFragmentPlayerBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.idName.onFocusChangeListener = OnFocusChangeListener { _, _ ->
            binding.idName.postDelayed({
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(binding.idName, InputMethodManager.SHOW_IMPLICIT)
            },200)
        }
        binding.idName.requestFocus()

        binding.idContinue.setOnClickListener {
            if (binding.idName.text.isNullOrEmpty())
                binding.idName.error = "Cant be empty"
            //todo cannot be equal to other name else if()
            else {
                onSubmitClickListener.invoke(PlayersModel(image, binding.idName.text.toString(), 0))
                dismiss()
            }
        }

        binding.idPicture.setOnClickListener {
            when (image) {
                0 -> {
                    binding.idPicture.setImageResource(R.drawable.player_girl)
                    binding.idGenre.text = "F"
                    image++
                }
                1 -> {
                    binding.idPicture.setImageResource(R.drawable.player_horse)
                    binding.idGenre.text = "-"
                    image++
                }
                2 -> {
                    image = 0
                    binding.idGenre.text = "M"
                    binding.idPicture.setImageResource(R.drawable.player_boy)
                }
            }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }
}


