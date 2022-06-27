package com.paperrockscissors.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.paperrockscissors.model.GameModel
import com.paperrockscissors.model.UserLeaderBoardModel
import java.util.concurrent.ThreadLocalRandom

class Database {
    private val instance = Firebase.database.reference
    private val auth = Firebase.auth
    private val gamePath = "games"
    private val leaderboardPath = "leaderboard"

    fun createNewGame(rounds: Int, onSuccess: () -> Unit) {
        val gameCode = ThreadLocalRandom.current().nextInt(100000, 1000000)
        // TODO: zastąpić poprawną nazwa gracza
        val username = auth.currentUser?.email.toString()

        game.postValue(null)

        var listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               val newGame=dataSnapshot.getValue<GameModel>()
                game.postValue(newGame)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        instance.child(gamePath).child(gameCode.toString()).addValueEventListener(listener)

        instance.child(gamePath).child(gameCode.toString()).setValue(GameModel(username,"",rounds,0,gameCode,0,0)) { _, _ ->
            onSuccess()
        }
    }

    fun joinNewGame(id: String,onSuccess: () -> Unit) {
        // TODO: Dolaczenie do gry
        //game.postValue(null)
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newGame=dataSnapshot.getValue<GameModel>()
                game.postValue(newGame)
                //game.value?.maxRounds = dataSnapshot.child("games").child(id.toString()).child("maxRounds").value.toString().toInt()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        instance.child(gamePath).child(id.toString()).addValueEventListener(listener)
        instance.child(gamePath).child(id.toString()).child("username2").setValue(auth.currentUser?.email.toString()){ _, _ ->
            onSuccess()
        }
    }

    fun setUserChoice(int: Int){
        val currentGame=game.value
        if(currentGame?.username1==auth.currentUser?.email)
        {
            currentGame?.let{instance.child(gamePath).child(it.gameCode.toString()).setValue(GameModel(it.username1,it.username2,it.maxRounds,it.round,it.gameCode,int,it.user2choice))}
        }
        else if(currentGame?.username2==auth.currentUser?.email){
            currentGame?.let{instance.child(gamePath).child(it.gameCode.toString()).setValue(GameModel(it.username1,it.username2,it.maxRounds,it.round,it.gameCode,it.user1choice,int))}
        }
    }

    fun increaseRoundCounter(int: Int){
        val currentGame=game.value
        //instance.child(gamePath).child(currentGame?.gameCode.toString()).child("round").setValue(int)
    }

    fun addWinToUser(userId: String) {
        // TODO: Dodaj jednego winda dla gracza
        instance.child(leaderboardPath).child(userId)
    }

    fun watchOnLeaderboard(context: Context) {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val all = mutableListOf<UserLeaderBoardModel>()
                for (childSnapshot in dataSnapshot.children) {
                    val item = childSnapshot.getValue<UserLeaderBoardModel>()
                    item?.let {
                       all.add(it)
                    }
                }
                leaderboard.postValue(all.sortedByDescending { it.roundsWon })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(context, "Błąd odczytu z bazy",Toast.LENGTH_SHORT).show()
            }
        }
        instance.child(leaderboardPath).ref.addValueEventListener(listener)
    }

    companion object {
        val leaderboard: MutableLiveData<List<UserLeaderBoardModel>> = MutableLiveData()
        val game: MutableLiveData<GameModel> = MutableLiveData()
    }
}