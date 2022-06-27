package com.paperrockscissors.view.gameBordView

import android.transition.Visibility
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.paperrockscissors.R
import com.paperrockscissors.utils.Database

class GameBoardViewModel : ViewModel() {
    enum class Images(val resourceId: Int) {
        ROCK(R.drawable.rock),
        PAPER(R.drawable.paper),
        SCISSORS(R.drawable.scissors)
    }

    enum class UserInfoText(val resourceId: Int) {
        YOUR_TURN(R.string.yourTurn),
        YOU_WIN(R.string.youWin),
        YOU_WIN_GAME(R.string.youWinGame),
        YOU_LOSE_GAME(R.string.youLoseGame),
        DRAW_GAME(R.string.drawGame),
        YOU_LOSE(R.string.youLose),
        DRAW(R.string.draw)
    }

    enum class OpponentInfoText(val resourceId: Int) {
        WAITING_FOR_OPPONENT(R.string.waitingForOponent),
        WAITING_FOR_OPPONENT_TURN(R.string.waitingForOpponentTurn),
    }

    private val database = Database()
    private lateinit var firebase: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val _userInfoText = MutableLiveData(UserInfoText.YOUR_TURN)
    val userInfoText: LiveData<UserInfoText> = _userInfoText

    private val _opponentInfoText = MutableLiveData(OpponentInfoText.WAITING_FOR_OPPONENT)
    val opponentInfoText: LiveData<OpponentInfoText> = _opponentInfoText

    private val _round = MutableLiveData(1)
    val round: LiveData<Int> = _round

    private val _userPoints = MutableLiveData(0)
    val userPoints: LiveData<Int> = _userPoints

    private val _opponentPoints = MutableLiveData(0)
    val opponentPoints: LiveData<Int> = _opponentPoints

    private val _opponentChoice = MutableLiveData(Images.PAPER)
    val opponentChoice: LiveData<Images> = _opponentChoice

    private val _userChoice = MutableLiveData(Images.PAPER)
    val userChoice: LiveData<Images> = _userChoice

    private val _userIntoTextVisibility = MutableLiveData(true)
    val userIntoTextVisibility: LiveData<Boolean> = _userIntoTextVisibility

    private val _userChoiceBoardVisibility = MutableLiveData(true)
    val userChoiceBoardVisibility: LiveData<Boolean> = _userChoiceBoardVisibility

    private val _userSelectedImageVisibility = MutableLiveData(true)
    val userSelectedImageVisibility: LiveData<Boolean> = _userSelectedImageVisibility

    private val _opponentSelectedImageVisibility = MutableLiveData(true)
    val opponentSelectedImageVisibility: LiveData<Boolean> = _opponentSelectedImageVisibility

    private val _opponentInfoTextVisibility = MutableLiveData(true)
    val opponentInfoTextVisibility: LiveData<Boolean> = _opponentInfoTextVisibility

    fun showTextYourTurn() {
        _userInfoText.postValue(UserInfoText.YOUR_TURN)
    }

    fun showTextYouWin() {
        _userInfoText.postValue(UserInfoText.YOU_WIN)
    }

    fun showTextYouWinGame() {
        _userInfoText.postValue(UserInfoText.YOU_WIN_GAME)
    }

    fun showTextYouLoseGame() {
        _userInfoText.postValue(UserInfoText.YOU_LOSE_GAME)
    }

    fun showTextYouDrawGame() {
        _userInfoText.postValue(UserInfoText.DRAW_GAME)
    }

    fun showTextYouLose() {
        _userInfoText.postValue(UserInfoText.YOU_LOSE)
    }

    fun showTextDraw() {
        _userInfoText.postValue(UserInfoText.DRAW)
    }

    fun showTextWaitingForOpponent() {
        _opponentInfoText.postValue(OpponentInfoText.WAITING_FOR_OPPONENT)
    }

    fun showTextWaitingForOpponentTurn() {
        _opponentInfoText.postValue(OpponentInfoText.WAITING_FOR_OPPONENT_TURN)
    }

    fun showUserChoiceContainer(){
        _userChoiceBoardVisibility.postValue(true)
    }

    fun setOpponentChoice(value: Images) {
        _opponentChoice.postValue(value)
    }

    fun setUserChoice(value: Images) {
        _userChoice.postValue(value)
        database.setUserChoice(value.resourceId)
    }

    fun hideUserInfoText() {
        _userIntoTextVisibility.postValue(false)
    }

    fun hideUserSelectedImage(boolean: Boolean){
        _userSelectedImageVisibility.postValue(boolean)
    }

    fun hideOpponentSelectedImage(boolean: Boolean){
        _opponentSelectedImageVisibility.postValue(boolean)
    }

    fun hideOpponentInfoText(){
        _opponentInfoTextVisibility.postValue(false)
    }


    fun increaseUserPoints() {
        _userPoints.value?.let {
            _userPoints.postValue(it + 1)
        }
        increaseRoundCounter()
    }

    fun increaseOpponentPoints() {
        _opponentPoints.value?.let {
            _opponentPoints.postValue(it + 1)
        }
        increaseRoundCounter()
    }

    fun increaseRoundCounter() {
        _round.value?.let {
            _round.postValue(it + 1)
            database.increaseRoundCounter(it+1)
        }
    }
}