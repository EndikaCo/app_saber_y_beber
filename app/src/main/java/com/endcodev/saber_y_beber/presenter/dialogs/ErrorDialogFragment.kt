package com.endcodev.saber_y_beber.presenter.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import com.endcodev.saber_y_beber.data.model.ErrorModel
import com.endcodev.saber_y_beber.databinding.DialogFragmentErrorBinding

class ErrorDialogFragment(
    private val onAcceptClickLister: (Boolean) -> Unit,
    private val error: ErrorModel,
) : DialogFragment() {

    private var _binding: DialogFragmentErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.run {
            _binding = DialogFragmentErrorBinding.inflate(layoutInflater).apply {}

            onBackPress()
            initListeners()
            initViews()

            AlertDialog.Builder(this).apply {
                setView(binding.root)
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initListeners() {
        binding.errorAccept.setOnClickListener {
            onAcceptClickLister.invoke(true)
            dismiss()
        }

        binding.errorCancel.setOnClickListener {
            onAcceptClickLister.invoke(false)
            dismiss()
        }
    }

    private fun onBackPress() {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
    }

    private fun initViews() {
        binding.errorTitle.text = error.title
        binding.errorDescription.text = error.description

        binding.errorAccept.text = error.acceptButton

        if (error.cancelButton == "")
        {
            binding.errorCancel.visibility = View.GONE
        }else
            binding.errorCancel.text = error.cancelButton
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