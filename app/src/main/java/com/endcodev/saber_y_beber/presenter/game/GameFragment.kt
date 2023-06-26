package com.endcodev.saber_y_beber.presenter.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint
import com.endcodev.saber_y_beber.data.model.GameModel

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding get() = _binding!!
    private lateinit var adapter: GameAdapter
    private val gameVM: GameViewModel by viewModels()
    lateinit var optionButtons: OptionButtons

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentGameBinding.inflate(inflater, container, false)
            .apply { _binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
        onBackPressed()
        initOptions()
    }

    private fun initObservers() {
        gameVM.rankingReady.observe(viewLifecycleOwner) {
            initAdapter()
        }

        //ProgressBar visibility
        gameVM.isLoading.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
        }

        //normal round questModel
        gameVM.gameModel.observe(viewLifecycleOwner) {
            normalRound(it)
        }
    }

    /**On BACK button pressed*/
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

    private fun setDifficulty(difficulty: Int) {
        when (difficulty) {
            0 -> binding.difficulty.setBackgroundResource(R.drawable.difficulty_0)
            1 -> binding.difficulty.setBackgroundResource(R.drawable.difficulty_1)
            2 -> binding.difficulty.setBackgroundResource(R.drawable.difficulty_2)
            3 -> binding.difficulty.setBackgroundResource(R.drawable.difficulty_3)
        }
    }

    private fun normalRound(it: GameModel?) {
        with(binding) {
            optionButtons.optionColor(it!!.optionsColor, it.optionSelected)
            tvTitle.text = it.title
            btOption1.text = it.option1
            btOption2.text = it.option2
            btOption3.text = it.option3
            tvAuthor.text = it.author
            report.visibility = View.VISIBLE
            report.alpha = it.report
            setDifficulty(it.difficulty)

            when (it.answered) {
                FINAL -> tvChallenge.text = it.challenge
                NO_ANSWER -> tvChallenge.text = it.challenge
                CORRECT_ANSWER -> {
                    tvChallenge.text =
                        resources.getString(R.string.game_feedback_correct, it.difficulty)
                    adapter.notifyDataSetChanged()
                    report.alpha = 1F
                }

                INCORRECT_ANSWER -> {
                    tvChallenge.text = it.fail
                    report.alpha = 1F
                }
            }
        }
        animateOptions(it)
    }

    /** animates answer options pop up*/
    private fun animateOptions(it: GameModel?) {
        val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.animation1)
        if (binding.options.visibility == View.VISIBLE && it!!.answered == NO_ANSWER) {
            binding.options.startAnimation(anim)
        }
        if (it!!.answered == FINAL) {
            binding.tvChallenge.startAnimation(anim)
            binding.tvTitle.startAnimation(anim)
        }
    }

    private fun initListeners() {
        with(binding) {

            //OnClick background
            background.setOnClickListener {
                if (gameVM.gameModel.value != null)
                    nextQuestion(gameVM.gameModel.value!!)
            }

            //OnClick report button
            binding.report.setOnClickListener {
                //reportQuest(gameVM.gameModel.value!!)
            }

            //OnClick any option button in Radio group
            options.setOnCheckedChangeListener { _, checkedId ->
                gameVM.optionClick(checkedId)
            }
        }
    }

    private fun initOptions() {
        optionButtons = OptionButtons(
            binding.options,
            binding.btOption1,
            binding.btOption2,
            binding.btOption3,
            context
        )
    }

    /** resets answered option [gameVM] and calls to next random quest*/
    private fun nextQuestion(gameModel: GameModel) {
        if (gameVM.gameModel.value != null) {
            if (binding.options.checkedRadioButtonId != -1) {
                binding.options.clearCheck()
                setDifficulty(0)
                gameVM.nextRound()
            } else if (gameModel.answered == FINAL)
                gameVM.nextRound()
        }
    }

    /**
    private fun reportQuest(gameModel: GameModel) {
    if (Firebase.auth.currentUser != null && Firebase.auth.currentUser!!.isEmailVerified) {
    if (gameModel.answered == CORRECT_ANSWER || gameModel.answered == INCORRECT_ANSWER) {
    val bundle = Bundle()
    bundle.putParcelable("report", gameModel)
    findNavController().navigate(R.id.reportDialogFragment, bundle)
    }
    } else
    Toast.makeText(
    requireContext(),
    resources.getText(R.string.need_login),
    Toast.LENGTH_SHORT
    ).show()
    }
     **/
    private fun openDialogRanking() {
        //val bundle = Bundle()
        //bundle.putParcelableArrayList("ranking", gameVM.playerList.value!!)
        //findNavController().navigate(R.id.rankingDialogFragment, bundle)
    }

    //Recycler adapter
    private fun initAdapter() {
        adapter = GameAdapter(gameVM.playerList.value!!, onClickListener = {
            openDialogRanking()
        })
        binding.gameRank.layoutManager = LinearLayoutManager(this.activity)
        binding.gameRank.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}