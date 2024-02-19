package com.endcodev.saber_y_beber.presentation.creation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentCreationBinding
import com.endcodev.saber_y_beber.domain.utils.App
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.A_EMPTY
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.B_EMPTY
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.C_EMPTY
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.DIF_EMPTY
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.OK
import com.endcodev.saber_y_beber.presentation.creation.CreateViewModel.Companion.QUEST_EMPTY
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFragment : Fragment(R.layout.fragment_creation) {

    private var _binding: FragmentCreationBinding? = null
    private val binding: FragmentCreationBinding get() = _binding!!
    private val createViewModel: CreateViewModel by viewModels()
    private var mInterstitialAd: InterstitialAd? = null

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
        initAdmob()
    }

    private fun initObservers() {
        createViewModel.difficulty.observe(viewLifecycleOwner) {
            setDifficulty(it)
        }

        createViewModel.notification.observe(viewLifecycleOwner) {
            if (it == OK) {
                Toast.makeText(
                    requireContext(), getString(R.string.question_correctly_created),
                    Toast.LENGTH_SHORT
                ).show()
                mInterstitialAd?.show(requireActivity())
                findNavController().navigate(R.id.homeFragment)
            } else
                Toast.makeText(
                    requireContext(),
                    getString(R.string.something_went_wrong),
                    Toast.LENGTH_SHORT
                ).show()
        }

        createViewModel.questError.observe(viewLifecycleOwner) {
            if (it.questError == QUEST_EMPTY)
                binding.createQuest.error = resources.getString(R.string.create_error_quest)
            if (it.correctError == A_EMPTY)
                binding.createCorrect.error = resources.getString(R.string.create_error_quest)
            if (it.option2Error == B_EMPTY)
                binding.createOption2.error = resources.getString(R.string.create_error_quest)
            if (it.option3Error == C_EMPTY)
                binding.createOption3.error = resources.getString(R.string.create_error_quest)
            if (it.difficultyError == DIF_EMPTY)
                Toast.makeText(
                    context, resources.getString(R.string.create_error_dif),
                    Toast.LENGTH_LONG
                ).show()
        }

        //ProgressBar visibility
        createViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
        }
    }

    private fun initAdmob() {
        val adRequest: AdRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(App.tag, adError.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(App.tag, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
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

    /** post to server*/
    private fun postData() {
        val quest = binding.createQuest.text.toString()
        val optionA = binding.createCorrect.text.toString()
        val optionB = binding.createOption2.text.toString()
        val optionC = binding.createOption3.text.toString()
        createViewModel.checkValues(quest, optionA, optionB, optionC)
    }

    private fun setDifficulty(difficulty: Int) {
        when (difficulty) {
            1 -> binding.createDifficulty.setBackgroundResource(R.drawable.d1)
            2 -> binding.createDifficulty.setBackgroundResource(R.drawable.d2)
            3 -> binding.createDifficulty.setBackgroundResource(R.drawable.d3)
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