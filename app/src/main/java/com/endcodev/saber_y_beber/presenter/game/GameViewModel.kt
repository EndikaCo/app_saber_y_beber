package com.endcodev.saber_y_beber.presenter.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.saber_y_beber.R
import com.endcodev.saber_y_beber.data.model.ChallengeModel
import com.endcodev.saber_y_beber.data.model.GameUiModel
import com.endcodev.saber_y_beber.data.model.OptionModel
import com.endcodev.saber_y_beber.data.model.PlayersModel
import com.endcodev.saber_y_beber.data.model.QuestModel
import com.endcodev.saber_y_beber.domain.GetChallengeUseCase
import com.endcodev.saber_y_beber.domain.GetPlayersUseCase
import com.endcodev.saber_y_beber.domain.GetQuestUseCase
import com.endcodev.saber_y_beber.domain.GetRandomChallengeUseCase
import com.endcodev.saber_y_beber.domain.GetRandomQuestUseCase
import com.endcodev.saber_y_beber.presenter.utils.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getQuestUseCase: GetQuestUseCase,
    private val getChallengeUseCase: GetChallengeUseCase,
    private val getRandomQuestUseCase: GetRandomQuestUseCase,
    private val getRandomChallengeUseCase: GetRandomChallengeUseCase,
    private val resources: ResourcesProvider,
    private val playersUseCase: GetPlayersUseCase

) : ViewModel() {

    companion object {
        const val TAG = "GameViewModel ***"
    }

    val isLoading = MutableLiveData<Boolean>()

    private val _gameModel = MutableLiveData<GameUiModel?>()
    val gameModel: LiveData<GameUiModel?> get() = _gameModel

    private val _playerList = MutableLiveData<MutableList<PlayersModel>>()
    val playerList: LiveData<MutableList<PlayersModel>> get() = _playerList

    private var _round = 1
    private var _actualPlayer = -1
    private var questCounter: Int = 0
    private var answer: Int = -1

    init {
        viewModelScope.launch {
            isLoading.postValue(true)
            _playerList.value = playersUseCase.invoke().toMutableList()

            if (getQuestUseCase().isNotEmpty() && getChallengeUseCase().isNotEmpty()) {
                getRandomQuestUseCase()
                getRandomChallengeUseCase()
                startRoundChallenge()
                isLoading.postValue(false)
            }
        }
    }

    private fun startRoundChallenge() {
        _gameModel.value = challengeToGame(getRandomChallengeUseCase.startChallenge(), 1)
    }

    private fun randomChallenge() {
        viewModelScope.launch {
            val randomNum = (_playerList.value!!.indices).random(Random(System.currentTimeMillis()))
            val randPlayer = _playerList.value!![randomNum]
            val randChallenge = getRandomChallengeUseCase.nextChallenge()

            if (randChallenge != null)
                randChallenge.challenge =
                    randChallenge.challenge.replace("#player", randPlayer.name)
            _gameModel.value = randChallenge?.let { challengeToGame(it, 1) }
        }
    }

    //get the name of the player with the highest score
    private fun leaderPlayer(players: MutableList<PlayersModel>): String? {
        if (players.isEmpty()) {
            return null // Return null if the list is empty
        }
        // Find the player with the highest score
        val leaderPlayer = players.maxByOrNull { it.points }
        return leaderPlayer?.name
    }

    private fun finalRoundChallenge() {
        val finRonda =
            getRandomChallengeUseCase.finalChallenge(_round, leaderPlayer(_playerList.value!!)!!)
        _gameModel.value = challengeToGame(finRonda, 3)
        _actualPlayer += 1
        _round += 1
    }

    private fun randomQuest() {
        viewModelScope.launch {
            isLoading.postValue(true)
            _gameModel.value = questToGame(getRandomQuestUseCase.nextQuest()!!)

            isLoading.postValue(false)
        }
    }

    private fun questToGame(model: QuestModel): GameUiModel {

        val shuffledList = arrayListOf(model.option1, model.option2, model.option3).shuffled()

        for (item in shuffledList) {
            if (item.contains(model.option1)) {
                answer = shuffledList.indexOf(item) + 1
            }
        }

        return GameUiModel(
            quest = model.challenge,
            author = model.author,
            option1 = OptionModel(shuffledList[0], false, isCorrect = false),
            option2 = OptionModel(shuffledList[1], false, isCorrect = false),
            option3 = OptionModel(shuffledList[2], false, isCorrect = false),
            difficulty = model.difficulty,
            title = playerList.value!![_actualPlayer].name,
            round = _round,
            answered = false
        )
    }

    private fun challengeToGame(challengeModel: ChallengeModel, difficulty: Int): GameUiModel {
        with(challengeModel) {
            return GameUiModel(
                challenge,
                author,
                null,
                null,
                null,
                difficulty,
                title,
                _round,
                answered = true
            )
        }
    }

    fun checkedOption(checkedId: Int) {

        val checked = when (checkedId) {
            R.id.bt_option1 -> 1
            R.id.bt_option2 -> 2
            R.id.bt_option3 -> 3
            else -> -1
        }
        returnModel(checked)
    }

    private fun returnModel(checked: Int) {
        val model = _gameModel.value
        when (checked) {
            1 -> model?.option1?.isSelected = true
            2 -> model?.option2?.isSelected = true
            3 -> model?.option3?.isSelected = true
        }

        when (answer) {
            1 -> model?.option1?.isCorrect = true
            2 -> model?.option2?.isCorrect = true
            3 -> model?.option3?.isCorrect = true
        }

        if (checked == answer && model?.answered == false) {
            model.quest = resources.getString(R.string.game_feedback_correct, model.difficulty)
            val playerList = _playerList.value
            if (playerList != null && _actualPlayer > -1)
                playerList[_actualPlayer].points += 1
        } else
            model?.quest = resources.getString(
                R.string.game_feedback_incorrect,
                drinkInverter(model!!.difficulty)
            )
        model.answered = true
        _gameModel.value = model
    }

    private fun drinkInverter(drinkNum: Int): Int {
        return when (drinkNum) {
            3 -> 1
            2 -> 2
            else -> 3
        }
    }

    fun nextQuest() {

        val playerList = _playerList.value

        if (playerList != null) {
            if (isFinalRound(playerList)) {
                finalRoundChallenge()
            } else if (questCounter == 3) //middle round -> every 3 quests
            {
                randomChallenge()
                questCounter = 0
            } else {   // normal round
                _actualPlayer += 1 // next player
                questCounter++
                if (_actualPlayer > playerList.size - 1) {
                    _actualPlayer = 0 // reset player to 1st
                }
                randomQuest()
            }
        }
    }

    private fun isFinalRound(playerList: MutableList<PlayersModel>): Boolean {
        if (_actualPlayer == playerList.size - 1)
            return true
        return false
    }
}

