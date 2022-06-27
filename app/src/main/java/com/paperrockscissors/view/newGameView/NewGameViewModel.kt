package com.paperrockscissors.view.newGameView

import androidx.lifecycle.ViewModel
import com.paperrockscissors.utils.Database

class NewGameViewModel : ViewModel() {
    var roundsValues = arrayListOf(5, 10, 15)

    fun createNewGame(rounds: Int, onSuccess: () -> Unit) {
        Database().createNewGame(rounds, onSuccess)
    }
}