package com.paperrockscissors.view.leaderboardView

import androidx.lifecycle.ViewModel
import com.paperrockscissors.utils.Database

class LeaderBoardViewModel : ViewModel() {
    val adapter: LeaderBoardAdapter by lazy {
        val list = Database.leaderboard.value
        LeaderBoardAdapter(list ?: emptyList())
    }
}