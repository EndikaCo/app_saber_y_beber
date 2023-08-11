package com.endcodev.saber_y_beber.presenter.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.GameUiModel
import com.endcodev.saber_y_beber.data.model.OptionModel
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment(R.layout.fragment_game) {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding get() = _binding!!
    private lateinit var adapter: GameAdapter
    private val gameVM: GameViewModel by viewModels()

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

            //OnClick option button in Radio group
            gameOptions.setOnCheckedChangeListener { _, checkedId ->
                gameVM.checkedOption(checkedId)
            }
        }
    }

    private fun initObservers() {
        gameVM.playerList.observe(viewLifecycleOwner) {
            initAdapter(it)
        }

        //ProgressBar visibility
        gameVM.isLoading.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
        }

        //normal round questModel
        gameVM.gameModel.observe(viewLifecycleOwner) {
            updateUi(it)
        }
    }

    private fun updateUi(ui: GameUiModel?) {
        if (ui != null)
            with(binding) {
                tvChallenge.text = ui.quest
                tvAuthor.text = ui.author
                tvTitle.text = ui.title

                setOption(ui.option1, btOption1)
                setOption(ui.option2, btOption2)
                setOption(ui.option3, btOption3)

                enableButtons(ui.answered)

                report.visibility = View.VISIBLE

                if (!ui.answered)
                    animateOptions()
                //report.alpha = ui.report
                setDifficulty(ui.difficulty)
                gameRound.text = resources.getString(R.string.round_n, ui.round)
                adapter.sortListByPoints()
            }
    }

    private fun enableButtons(ans: Boolean) {
        binding.btOption1.isEnabled = !ans
        binding.btOption2.isEnabled = !ans
        binding.btOption3.isEnabled = !ans
        binding.background.isEnabled = ans
    }

    private fun setOption(option: OptionModel?, button: RadioButton) {
        if (option == null) {
            button.visibility = View.INVISIBLE
        } else {
            button.visibility = View.VISIBLE
            button.text = option.text
        }

        if (option?.isSelected == true)
            button.setBackgroundResource(R.drawable.answer_option_selected)

        if (option?.isCorrect == true)
            button.setBackgroundResource(R.drawable.answer_optio_correctn)
    }


    private fun resetDrawables() {
        binding.gameOptions.clearCheck()
        binding.btOption1.setBackgroundResource(R.drawable.answer_option)
        binding.btOption2.setBackgroundResource(R.drawable.answer_option)
        binding.btOption3.setBackgroundResource(R.drawable.answer_option)
    }

    /** resets answered option [gameVM] and calls to next random quest*/
    private fun nextQuestion(gameModel: GameUiModel) {
        resetDrawables()
        gameVM.nextQuest()
    }


    /** animates answer options pop up*/
    private fun animateOptions() {
        val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.animation1)
        binding.gameOptions.startAnimation(anim)
        binding.tvChallenge.startAnimation(anim)
        binding.tvTitle.startAnimation(anim)
    }

    private fun setDifficulty(difficulty: Int) {
        when (difficulty) {
            0 -> binding.difficulty.setBackgroundResource(R.drawable.difficulty_0)
            1 -> binding.difficulty.setBackgroundResource(R.drawable.difficulty_1)
            2 -> binding.difficulty.setBackgroundResource(R.drawable.difficulty_2)
            3 -> binding.difficulty.setBackgroundResource(R.drawable.difficulty_3)
        }
    }

    private fun openDialogRanking() {
        //findNavController().navigate(R.id.rankingDialogFragment)
    }

    //Recycler adapter
    private fun initAdapter(value: List<PlayersModel>?) {
        value?.let {
            adapter = GameAdapter(it, onClickListener = {
                openDialogRanking()
            })
            binding.gameRank.layoutManager = LinearLayoutManager(this.activity)
            binding.gameRank.adapter = adapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
