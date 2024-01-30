package com.endcodev.saber_y_beber.presentation.game

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
import com.endcodev.saber_y_beber.databinding.FragmentGameBinding
import com.endcodev.saber_y_beber.domain.model.ErrorModel
import com.endcodev.saber_y_beber.domain.model.GameUiModel
import com.endcodev.saber_y_beber.domain.model.OptionModel
import com.endcodev.saber_y_beber.domain.model.PlayersModel
import com.endcodev.saber_y_beber.presentation.dialogs.ErrorDialogFragment
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
        initViews()
        initListeners()
        initObservers()
        onBackPressed()
    }

    private fun initViews() {
        binding.options.visibility = View.GONE
    }

    private fun initListeners() {
        with(binding) {

            //OnClick background
            background.setOnClickListener {
                nextQuestion(gameVM.gameModel.value)
            }

            //OnClick report button
            report.setOnClickListener {
                //reportQuest(gameVM.gameModel.value!!)
            }

            //OnClick option button in Radio group
            options.setOnCheckedChangeListener { _, checkedId ->
                gameVM.checkedOption(checkedId)
            }
        }
    }

    /**
     * Initialize the observers.
     */
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

        gameVM.error.observe(viewLifecycleOwner) {
            showError()
        }
    }

    /** shows error dialog*/
    private fun showError() {

        val dialog = ErrorDialogFragment(
            onAcceptClickLister = {
                findNavController().navigate(R.id.homeFragment)
            }, ErrorModel(
                getString(R.string.no_quest_available),
                getString(R.string.game_reset),
                getString(R.string.player_accept),
                getString(R.string.cancel)
            )
        )
        dialog.isCancelable = false
        dialog.show(parentFragmentManager, "dialog")

    }

    /** resets answered option [gameVM] and calls to next random quest*/
    private fun nextQuestion(gameModel: GameUiModel?) {
        if (gameModel != null) {
            resetDrawables()
            gameVM.nextQuest()
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

                report.alpha = 0.5f

                if (!ui.answered)
                    animateOptions()
                setDifficulty(ui.difficulty)
                tvRound.text = resources.getString(R.string.round_n, ui.round)
                adapter.sortListByPoints()
            }
    }

    /** enables or disables buttons
     * @param ans is the state to change*/
    private fun enableButtons(ans: Boolean) {
        binding.options.visibility = View.VISIBLE
        binding.btOption1.isEnabled = !ans
        binding.btOption2.isEnabled = !ans
        binding.btOption3.isEnabled = !ans
        binding.background.isEnabled = ans
    }


    /** sets the option in the button
     * @param option is the option to change the text
     * @param button is the radiobutton View to modify*/
    private fun setOption(option: OptionModel?, button: RadioButton) {

        // if null hide button
        if (option == null) {
            button.visibility = View.INVISIBLE
        } else {
            button.visibility = View.VISIBLE
            button.text = option.text
        }

        // if selected but wrong
        if (option?.isCorrect == false && option.isSelected) {
            button.setBackgroundResource(R.drawable.answer_wrong)
        }
        if (option?.isCorrect == true)
            button.setBackgroundResource(R.drawable.answer_option_correct)

    }

    /** resets all drawables to default*/
    private fun resetDrawables() {
        binding.options.clearCheck()
        binding.btOption1.setBackgroundResource(R.drawable.round_corners)
        binding.btOption2.setBackgroundResource(R.drawable.round_corners)
        binding.btOption3.setBackgroundResource(R.drawable.round_corners)
    }

    /** animates answer options pop up*/
    private fun animateOptions() {
        val anim: Animation = AnimationUtils.loadAnimation(context, R.anim.animation1)
        binding.options.startAnimation(anim)
        binding.tvChallenge.startAnimation(anim)
        binding.tvTitle.startAnimation(anim)
    }

    /** sets the difficulty background
     * @param difficulty is the difficulty to set*/
    private fun setDifficulty(difficulty: Int) {
        when (difficulty) {
            0 -> binding.gameDifficulty.setBackgroundResource(R.drawable.d1)
            1 -> binding.gameDifficulty.setBackgroundResource(R.drawable.d1)
            2 -> binding.gameDifficulty.setBackgroundResource(R.drawable.d2)
            3 -> binding.gameDifficulty.setBackgroundResource(R.drawable.d3)
        }
    }

    /** initializes the adapter with [value]*/
    private fun initAdapter(value: List<PlayersModel>?) {
        value?.let {
            adapter = GameAdapter(it, onClickListener = {})
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
