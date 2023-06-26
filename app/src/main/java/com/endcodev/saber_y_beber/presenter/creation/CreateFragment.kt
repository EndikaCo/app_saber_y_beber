package com.endcodev.saber_y_beber.presenter.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentCreationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class CreateFragment : Fragment(R.layout.fragment_creation) {

    private var _binding: FragmentCreationBinding? = null
    private val binding: FragmentCreationBinding get() = _binding!!
    private val createViewModel: CreateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCreationBinding.inflate(
            inflater, container,
            false
        ).apply { _binding = this }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewModel.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
        onBackPressed()
    }

    private fun initObservers() {
        createViewModel.difficulty.observe(viewLifecycleOwner){
            setDifficulty(it)
        }
    }

    private fun initListeners() {
        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.createDifficulty.setOnClickListener {
            createViewModel.difficultyChange()
        }

        binding.createConfirmBt.setOnClickListener {
            postData()
        }
    }

    private fun postData() {
        val quest = binding.createQuest.text.toString()
        val optionA = binding.createCorrect.text.toString()
        val optionB = binding.createOption2.text.toString()
        val optionC = binding.createOption3.text.toString()
        val feedback = binding.createFeedback.text.toString()
        val alternative = binding.createAlternative.text.toString()

        createViewModel.checkValues(quest, optionA, optionB,
            optionC, feedback, alternative )
    }

    fun setDifficulty(difficulty: Int) {
        when (difficulty) {
            0 -> binding.createDifficulty.setBackgroundResource(R.drawable.difficulty_0)
            1 -> binding.createDifficulty.setBackgroundResource(R.drawable.difficulty_1)
            2 -> binding.createDifficulty.setBackgroundResource(R.drawable.difficulty_2)
            3 -> binding.createDifficulty.setBackgroundResource(R.drawable.difficulty_3)
        }
    }

    private fun drinkInverter(drinkNum: Int): Int {
        return when (drinkNum) {
            3 -> 1
            2 -> 2
            else -> 3
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                    exitProcess(0)
                }
            }
        )
    }

}