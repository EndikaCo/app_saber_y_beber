package com.endcodev.saber_y_beber.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.DialogFragmentNameBinding

class NameDialogFragment(
    private val onAccept: (String) -> Unit,
) : DialogFragment() {
    private var _binding: DialogFragmentNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            _binding = DialogFragmentNameBinding.inflate(layoutInflater).apply {}

            onBackPress()
            initListeners()

            AlertDialog.Builder(this).apply {
                setView(binding.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initListeners() {
        binding.nameAccept.setOnClickListener {
            binding.nameDescription.text.toString().let {
                if (it.isNotEmpty()) {
                    onAccept.invoke(it)
                    dismiss()
                }
                binding.nameDescription.error = getString(R.string.empty_name)
            }

        }

        binding.nameCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun onBackPress() {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}