package com.endcodev.saber_y_beber.presenter.game

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.presenter.utils.ResourcesProvider
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.domain.GetChallengeUseCase
import com.endcodev.saber_y_beber.domain.GetPlayersUseCase
import com.endcodev.saber_y_beber.domain.GetQuestUseCase
import com.endcodev.saber_y_beber.domain.GetRandomChallengeUseCase
import com.endcodev.saber_y_beber.domain.GetRandomQuestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.endcodev.saber_y_beber.data.model.ChallengeModel
import com.endcodev.saber_y_beber.data.model.GameModel
import com.endcodev.saber_y_beber.data.model.QuestModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

const val CORRECT_ANSWER = 0
const val INCORRECT_ANSWER = 1
const val NO_ANSWER = -1
const val FINAL = 2

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getQuestUseCase: GetQuestUseCase,
    private val getChallengeUseCase: GetChallengeUseCase,
    private val getRandomQuestUseCase: GetRandomQuestUseCase,
    private val getRandomChallengeUseCase: GetRandomChallengeUseCase,
    private val resources: ResourcesProvider,
    private val playersUseCase: GetPlayersUseCase

) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()

    private val _gameModel = MutableLiveData<GameModel?>()
    val gameModel: LiveData<GameModel?> get() = _gameModel

    private val _playerList = MutableLiveData<MutableList<PlayersModel>>()
    val playerList: LiveData<MutableList<PlayersModel>> get() = _playerList

    val rankingReady = MutableLiveData<Boolean>()

    private var _round = 1
    private var _actualPlayer = -1
    private var questCounter: Int = 0

    init {
        viewModelScope.launch {
            isLoading.postValue(true)
            _playerList.value = playersUseCase.invoke().toMutableList()
            rankingReady.value = true

            if (getQuestUseCase().isNotEmpty() && getChallengeUseCase().isNotEmpty()) {

                getRandomQuestUseCase()
                getRandomChallengeUseCase()

                startRoundChallenge()
                isLoading.postValue(false)
            }
        }
    }

    private fun startRoundChallenge() {
        _gameModel.value = challengeToGame(
            ChallengeModel(
                resources.getString(R.string.game_start_round_title),
                resources.getString(R.string.game_start_round_text),
                resources.getString(R.string.game_start_round_author),
                0
            ),
            1
        )
        _gameModel.postValue(_gameModel.value)
    }

    private fun middleRoundChallenge() {
        viewModelScope.launch {
            val randomNum = (_playerList.value!!.indices).random(Random(System.currentTimeMillis()))
            val player = _playerList.value!![randomNum]
            val aux = getRandomChallengeUseCase.nextChallenge()
            aux!!.challenge = aux.challenge.replace("#player", player.name)
            _gameModel.value = challengeToGame(aux, 0)
        }
    }

    private fun finalRoundChallenge() {
        val finRonda = ChallengeModel(
            resources.getString(R.string.fin_ronda, _round),
            resources.getString(R.string.final_ranking_lead, _playerList.value?.get(0)?.name!!),
            "Developer",
            0
        )
        _gameModel.value = challengeToGame(finRonda, 3)
    }

    private fun randomQuest() {
        viewModelScope.launch {
            isLoading.postValue(true)
            _gameModel.value = questToGame(getRandomQuestUseCase.nextQuest()!!)
            _gameModel.postValue(_gameModel.value)
            isLoading.postValue(false)
        }
    }

    private fun questToGame(questModel: QuestModel): GameModel {
        with(questModel) {
            val optionList = arrayListOf(option1, option2, option3)
            val randInts = generateSequence { Random.nextInt(0..2) }.distinct().take(3).toSet()

            answer = when (option1) {
                optionList[randInts.elementAt(0)] -> 1
                optionList[randInts.elementAt(1)] -> 2
                else -> 3
            }
            return GameModel(
                "${playerList.value!![_actualPlayer].name}, $challenge",
                author,
                answer,
                optionList[randInts.elementAt(0)],
                optionList[randInts.elementAt(1)],
                optionList[randInts.elementAt(2)],
                difficulty,
                fail,
                resources.getString(R.string.round_n, _round),
                _round,
                -1,
                NO_ANSWER,
                _actualPlayer,
                0.3F,
                View.VISIBLE,
                -1
            )
        }
    }

    private fun challengeToGame(challengeModel: ChallengeModel, difficulty: Int): GameModel {
        with(challengeModel) {
            return GameModel(
                challenge,
                author,
                -1,
                null,
                null,
                null,
                difficulty,
                null,
                title,
                _round,
                -2,
                FINAL,
                _actualPlayer,
                0.3F,
                View.GONE,
                -1
            )
        }
    }

    fun optionClick(checkedId: Int) {

        val button = when (checkedId) {

            R.id.bt_option1 -> 1
            R.id.bt_option2 -> 2
            R.id.bt_option3 -> 3
            else -> {
                -1
            }
        }
        _gameModel.value?.optionSelected = button

        if (_gameModel.value != null && _gameModel.value!!.answered == NO_ANSWER)
            checkAnswer(button)
    }

    fun nextRound() {
        if (_actualPlayer == _playerList.value!!.size - 1) {   // final round
            finalRoundChallenge()
            _actualPlayer += 1
            _round += 1
        } else if (questCounter == 3) //middle round -> every 3 quests
        {
            middleRoundChallenge()
            questCounter = 0
        } else {   // normal round
            _actualPlayer += 1 // next player
            questCounter++
            if (_actualPlayer > playerList.value!!.size - 1) {
                _actualPlayer = 0 // reset player to 1st
            }
            randomQuest()
        }
    }

    private fun checkAnswer(option: Int) {
        val optionColors = arrayListOf(0, 0, 0)
        optionColors[_gameModel.value!!.answer - 1] = 1
        _gameModel.value!!.optionsColor = _gameModel.value!!.answer
        _gameModel.postValue(_gameModel.value)

        if (option == _gameModel.value!!.answer) { //correct answer
            _gameModel.value!!.answered = CORRECT_ANSWER
            _playerList.value!![_actualPlayer].points += _gameModel.value!!.difficulty
            _playerList.value!!.sortWith(compareByDescending { it.points })
        } else
            _gameModel.value!!.answered = INCORRECT_ANSWER
    }
}

