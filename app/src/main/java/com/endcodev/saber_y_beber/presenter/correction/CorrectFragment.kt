package com.endcodev.saber_y_beber.presenter.correction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.CorrectionModel
import com.endcodev.saber_y_beber.data.model.ErrorModel
import com.endcodev.saber_y_beber.databinding.FragmentCorrectBinding
import com.endcodev.saber_y_beber.presenter.dialogs.ErrorDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CorrectFragment : Fragment(R.layout.fragment_correct) {

    private var _binding: FragmentCorrectBinding? = null
    private val binding: FragmentCorrectBinding get() = _binding!!
    private val correctViewModel: CorrectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCorrectBinding.inflate(
            inflater, container,
            false
        ).apply { _binding = this }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        correctViewModel.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        initObservers()
        onBackPressed()
    }

    private fun initViews() {
        binding.viewHeader.headerTitle.text = resources.getString(R.string.correction_title_default)
    }

    private fun initListeners() {

        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.correctOkBt.setOnClickListener {
            acceptCorrection()
        }
    }

    private fun initObservers() {

        correctViewModel.correctModel.observe(viewLifecycleOwner) { correction ->
            if (correction == null) {
                val dialog = ErrorDialogFragment(
                    onAcceptClickLister = {
                        if (it) {
                            findNavController().navigate(R.id.createFragment)
                        }
                    }, ErrorModel(
                        getString(R.string.correct_no_correction_available_title),
                        getString(R.string.correct_no_correction_available),
                        getString(R.string.ok),
                        ""
                    )
                )
                dialog.isCancelable = false
                dialog.show(childFragmentManager, "dialog")
            } else
                showCorrection(correction)
        }

        correctViewModel.toCreate.observe(viewLifecycleOwner) {

            findNavController().navigate(R.id.createFragment)
        }
    }

    private fun acceptCorrection() {

        if (allChecked())
            correctViewModel.acceptCorrection(true) // if all ok +1 to rating
        else
            correctViewModel.acceptCorrection(false) // if some wrong directly -1 to rating

        uncheckAll()
        correctViewModel.postAvailableCorrection() //else, correct another question
    }

    private fun allChecked(): Boolean {
        return (binding.correctDifficultyCheck.isChecked
                && binding.correctQuestCheck.isChecked
                && binding.correctAnswerCheck.isChecked
                && binding.correctOption2Check.isChecked
                && binding.correctOption3Check.isChecked
                && binding.correctFeedbackCheck.isChecked)
    }

    /**
     * @param it is the correction model to show in Ui.
     */
    private fun showCorrection(it: CorrectionModel) {
        binding.correctFail.text = it.fail
        binding.correctAnswer.text = it.option1
        binding.correctOption2.text = it.option2
        binding.correctOption3.text = it.option3
        binding.correctQuestion.text = it.correction
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        )
    }

    private fun uncheckAll() {
        binding.correctDifficultyCheck.isChecked = false
        binding.correctQuestCheck.isChecked = false
        binding.correctAnswerCheck.isChecked = false
        binding.correctOption2Check.isChecked = false
        binding.correctOption3Check.isChecked = false
        binding.correctFeedbackCheck.isChecked = false
    }


}