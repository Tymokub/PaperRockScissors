package com.paperrockscissors.view.authView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Error

class AuthViewModel : ViewModel() {
    private val _currentAuthMode: MutableLiveData<AuthMode> = MutableLiveData<AuthMode>(AuthMode.SIGNIN)
    val currentAuthMode: LiveData<AuthMode> = _currentAuthMode
    var status = MutableLiveData<Boolean?>()

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    fun toggleAuthMode() {
        if (_currentAuthMode.value == AuthMode.SIGNIN) {
            _currentAuthMode.postValue(AuthMode.SIGNUP)
        } else {
            _currentAuthMode.postValue(AuthMode.SIGNIN)
        }
    }

    fun onActionBtnClick(username: String, password: String, onSuccess: () -> Unit,onFailure: () -> Unit) {
        when (currentAuthMode.value) {
            AuthMode.SIGNIN -> {
                singIn(username, password, onSuccess, onFailure)
            }
            AuthMode.SIGNUP -> {
                singUp(username, password, onSuccess,onFailure)
            }
            else -> {
                throw Error("Missing param")
            }
        }
    }

    private fun singIn(username: String, password: String, onSuccess: () -> Unit,onFailure: () -> Unit) {
        //rejestracja
        auth = Firebase.auth
        database = Firebase.database.reference
        if(username!=null && username!="" && password!="" && password!=null) {
            auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure
                }
            }
        }
        else{
            onFailure        }
    }

    private fun singUp(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        //logowanie
        auth = Firebase.auth
        database = Firebase.database.reference
        if(username!=null && username!="" && password!="" && password!=null){
            auth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                }
                else{
                    onFailure()
                }
            }
        }
        else{
            onFailure()
        }
    }
}