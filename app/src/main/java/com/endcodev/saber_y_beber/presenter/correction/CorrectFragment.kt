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

    /**
     * Initialize the views.
     */
    private fun initViews() {
        binding.viewHeader.headerTitle.text = resources.getString(R.string.correction_title_default)
    }

    /**
     * Initialize the listeners.
     */
    private fun initListeners() {

        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        binding.correctOk.setOnClickListener {
            acceptCorrection()
        }
    }

    /**
     * Initialize the observers.
     */
    private fun initObservers() {

        // update Ui when the correction is available.
        correctViewModel.correctModel.observe(viewLifecycleOwner) { correction ->
            // if correction is null, show error dialog.
            if (correction == null) {
                showErrorDialog()
            } else {
                // Show the correction in UI.
                showCorrection(correction)
            }
        }

        // navigate to create fragment.
        correctViewModel.toCreate.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.createFragment)
        }
    }

    /**
     * Show error dialog.
     */
    private fun showErrorDialog() {
        val dialog = ErrorDialogFragment(
            onAcceptClickLister = {
                if (it) { findNavController().navigate(R.id.createFragment) }
            }, ErrorModel(
                getString(R.string.correct_no_correction_available_title),
                getString(R.string.correct_no_correction_available),
                getString(R.string.ok),
                ""
            )
        )
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "dialog")
    }

    private fun acceptCorrection() {

        if (allChecked())
            correctViewModel.acceptCorrection(true) // if all ok +1 to rating
        else
            correctViewModel.acceptCorrection(false) // if some wrong directly -1 to rating

        uncheckAll()
        correctViewModel.postAvailableCorrection() //else, correct another question
    }

    /**
     * @return TRUE if all the checks are checked.
     */
    private fun allChecked(): Boolean {
        return (binding.correctDifficultyCheck.isChecked
                && binding.correctQuestCheck.isChecked
                && binding.correctAnswerCheck.isChecked
                && binding.correctOption2Check.isChecked
                && binding.correctOption3Check.isChecked
                )
    }

    /**
     * @param correction is the correction model to show in Ui.
     */
    private fun showCorrection(correction: CorrectionModel) {
        binding.correctAnswer.text = correction.option1
        binding.correctOption2.text = correction.option2
        binding.correctOption3.text = correction.option3
        binding.correctQuestion.text = correction.correction
        setDifficulty(correction.difficulty)
    }

    private fun setDifficulty(difficulty: Int) {
        when (difficulty) {
            0 -> binding.correctDifficulty.setBackgroundResource(R.drawable.difficulty_0)
            1 -> binding.correctDifficulty.setBackgroundResource(R.drawable.difficulty_1)
            2 -> binding.correctDifficulty.setBackgroundResource(R.drawable.difficulty_2)
            3 -> binding.correctDifficulty.setBackgroundResource(R.drawable.difficulty_3)
        }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}