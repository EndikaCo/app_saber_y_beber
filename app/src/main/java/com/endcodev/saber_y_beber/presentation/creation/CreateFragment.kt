package com.endcodev.saber_y_beber.presentation.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentCreationBinding
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.A_EMPTY
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.B_EMPTY
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.C_EMPTY
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.DIF_EMPTY
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.QUEST_EMPTY
import dagger.hilt.android.AndroidEntryPoint

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

        initViews()
        initListeners()
        initObservers()
        onBackPressed()
    }

    private fun initViews() {
        binding.viewHeader.headerTitle.text = resources.getString(R.string.create_title)
    }

    /**
     * Initialize the Observers.
     */
    private fun initObservers() {
        createViewModel.difficulty.observe(viewLifecycleOwner){
            setDifficulty(it)
        }

        createViewModel.notification.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        createViewModel.questError.observe(viewLifecycleOwner) {
            if( it.questError == QUEST_EMPTY)
                binding.createQuest.error = resources.getString(R.string.create_error_quest)
            if(it.correctError == A_EMPTY)
                binding.createCorrect.error = resources.getString(R.string.create_error_quest)
            if(it.option2Error == B_EMPTY)
                binding.createOption2.error = resources.getString(R.string.create_error_quest)
            if(it.option3Error == C_EMPTY)
                binding.createOption3.error = resources.getString(R.string.create_error_quest)
            if(it.difficultyError == DIF_EMPTY)
                Toast.makeText(context, resources.getString(R.string.create_error_dif),
                    Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Initialize the listeners.
     */
    private fun initListeners() {
        binding.viewHeader.headerBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.createDifficulty.setOnClickListener {
            createViewModel.difficultyChange()
        }

        binding.createOk.setOnClickListener {
            postData()
        }
    }

    /**
     * post to server
     */
    private fun postData() {
        val quest = binding.createQuest.text.toString()
        val optionA = binding.createCorrect.text.toString()
        val optionB = binding.createOption2.text.toString()
        val optionC = binding.createOption3.text.toString()

        createViewModel.checkValues(quest, optionA, optionB,
            optionC)
    }

    private fun setDifficulty(difficulty: Int) { //todo repeated in correct
        when (difficulty) {
            0 -> binding.createDifficulty.setBackgroundResource(R.drawable.difficulty_0)
            1 -> binding.createDifficulty.setBackgroundResource(R.drawable.difficulty_1)
            2 -> binding.createDifficulty.setBackgroundResource(R.drawable.difficulty_2)
            3 -> binding.createDifficulty.setBackgroundResource(R.drawable.difficulty_3)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}