package com.paperrockscissors.view.joinGameView

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.paperrockscissors.utils.Database

class JoinGameViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var user_key: String

    fun joinGame(gameCode: Number, onSuccess: () -> Unit, onFailure: () -> Unit) {

        auth = Firebase.auth
        database = Firebase.database.reference
        user_key= auth.currentUser!!.uid
        database.child("games").child(gameCode.toString()).get().addOnSuccessListener {
            if(!it.exists()){onFailure()}
            else{
                //Database.game.postValue(null)
                Database().joinNewGame(gameCode.toString(),onSuccess) }
        }.addOnFailureListener { onFailure() }
    }
}