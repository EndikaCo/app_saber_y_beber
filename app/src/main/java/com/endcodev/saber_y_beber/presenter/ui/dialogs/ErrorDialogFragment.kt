package com.endcodev.saber_y_beber.presenter.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.endcodev.saber_y_beber.databinding.DialogFragmentErrorBinding

class ErrorDialogFragment(
    private val onAcceptClickLister: (Boolean) -> Unit,
    private val title: String,
    private val description: String,
    private val acceptButton: String,
    private val cancelButton: String,
    private val titleDrawable: Drawable
) : DialogFragment() {

    private val dialogTag = "DIALOG_LOG:"
    private var _binding: DialogFragmentErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            _binding = DialogFragmentErrorBinding.inflate(layoutInflater).apply {}

            initViews()

            binding.errorAccept.setOnClickListener {
                onAcceptClickLister.invoke(true)
                dismiss()
            }

            binding.errorCancel.setOnClickListener {
                onAcceptClickLister.invoke(false)
                dismiss()
            }

            AlertDialog.Builder(this).apply {
                setView(binding.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initViews() {
        binding.errorTitle.text = title
        binding.errorDescription.text = description
        binding.errorAccept.text = acceptButton
        binding.errorCancel.text = cancelButton
        try {
            binding.errorTitle.setCompoundDrawablesWithIntrinsicBounds(
                titleDrawable,
                null,
                null,
                null
            )
        } catch (e: Exception) {
            Log.e(dialogTag, "Error: $e")
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