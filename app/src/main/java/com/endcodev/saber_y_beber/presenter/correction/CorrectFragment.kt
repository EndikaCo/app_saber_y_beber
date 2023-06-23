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
import com.endcodev.saber_y_beber.databinding.FragmentCorrectBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess


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
        //correctViewModel.onCreate()
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
    }

    private fun initObservers() {

       // correctViewModel.correctModel.observe(viewLifecycleOwner) {
            //if (it != null)
            //showerror
            //else
            //    showCorrection(it)
        //}
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