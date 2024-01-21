package com.endcodev.saber_y_beber.presentation.correction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.domain.model.CorrectionModel
import com.endcodev.saber_y_beber.domain.model.ErrorModel
import com.endcodev.saber_y_beber.databinding.FragmentCorrectBinding
import com.endcodev.saber_y_beber.domain.utils.App
import com.endcodev.saber_y_beber.presentation.dialogs.ErrorDialogFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CorrectFragment : Fragment(R.layout.fragment_correct) {

    private var mInterstitialAd: InterstitialAd? = null
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
        initAdmob()
        initViews()
        initListeners()
        initObservers()
        onBackPressed()
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
            mInterstitialAd?.show(requireActivity())
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

        correctViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                binding//todo
            else
                binding//todo
        }
    }

    /**
     * Show error dialog.
     */
    private fun showErrorDialog() {
        val dialog = ErrorDialogFragment(
            onAcceptClickLister = {
                if (it) {
                    findNavController().navigate(R.id.createFragment)
                    mInterstitialAd?.show(requireActivity())
                } else
                    findNavController().navigate(R.id.homeFragment)
            }, ErrorModel(
                getString(R.string.correct_no_correction_available_title),
                getString(R.string.correct_no_correction_available),
                getString(R.string.ok),
                getString(R.string.cancel)
            )
        )
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "dialog")
    }

    private fun acceptCorrection() {
        if (allChecked())
            correctViewModel.acceptCorrection(true)
        else
            correctViewModel.acceptCorrection(false)
        uncheckAll()
        correctViewModel.postAvailableCorrection()
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