package com.paperrockscissors.view.gameBordView

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.paperrockscissors.R
import com.paperrockscissors.databinding.GameBoardFragmentBinding
import com.paperrockscissors.utils.Database


class GameBoardFragment : Fragment() {
    private lateinit var viewModel: GameBoardViewModel
    private lateinit var binding: GameBoardFragmentBinding

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var user_key: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        database = Firebase.database.reference
        user_key= auth.currentUser!!.uid
        binding = GameBoardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GameBoardViewModel::class.java]

        var check=false
        binding.gameCodeTextView.text= Database.game.value?.gameCode.toString()

        viewModel.userInfoText.observe(viewLifecycleOwner) {
            binding.userInfo.text = getString(it.resourceId)
            binding.userInfo.visibility = View.VISIBLE
        }

        viewModel.opponentInfoText.observe(viewLifecycleOwner) {
            binding.oponentInfo.text = getString(it.resourceId)
            binding.oponentInfo.visibility = View.VISIBLE
            binding.oponentSelectedImage.visibility = View.INVISIBLE
        }

        viewModel.opponentChoice.observe(viewLifecycleOwner) {
            binding.oponentInfo.visibility = View.INVISIBLE
            binding.oponentSelectedImage.setImageResource(it.resourceId)
            binding.oponentSelectedImage.visibility = View.VISIBLE
        }

        viewModel.userChoice.observe(viewLifecycleOwner) {
            binding.userSelectContainer.visibility = View.INVISIBLE
            binding.userSelectedImage.setImageResource(it.resourceId)
            binding.userSelectedImage.visibility = View.VISIBLE
        }

        viewModel.userIntoTextVisibility.observe(viewLifecycleOwner) {
            binding.userInfo.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        viewModel.userChoiceBoardVisibility.observe(viewLifecycleOwner){
            binding.userSelectContainer.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        viewModel.userSelectedImageVisibility.observe(viewLifecycleOwner){
            binding.userSelectedImage.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        viewModel.opponentSelectedImageVisibility.observe(viewLifecycleOwner){
            binding.oponentSelectedImage.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        viewModel.opponentInfoTextVisibility.observe(viewLifecycleOwner){
            binding.oponentInfo.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        viewModel.opponentPoints.observe(viewLifecycleOwner) {
            binding.oponentPoints.text = getString(R.string.points, it)
        }

        viewModel.userPoints.observe(viewLifecycleOwner) {
            binding.userPoints.text = getString(R.string.points, it)
        }

        // Round counter
        viewModel.round.observe(viewLifecycleOwner) {
            binding.roundsCount.text = getString(R.string.roundInfo, it, Database.game.value?.maxRounds ?: 15)
        }

        Database.game.observe(viewLifecycleOwner) {
            binding.roundsCount.text = getString(R.string.roundInfo, viewModel.round.value, it.maxRounds)

                    //jezeli jestes wlascicielem
            if(it.username1==auth.currentUser?.email)
            {
                binding.userName.text = it.username1
                binding.oponentName.text = it.username2

                if(it.user1choice==0){viewModel.showTextYourTurn()}
                if(it.user2choice==0 && it.user1choice!=0){viewModel.showTextWaitingForOpponentTurn()}
                if(it.user1choice!=0){viewModel.hideUserInfoText()}

                //Jezeli jest przeciwnik to pokaz jego nazwe
                if(it.username2!="" && check==false){
                    check=true
                    viewModel.hideOpponentInfoText()
                    binding.oponentName.text = it.username2
                    binding.gameCodeTextView.text=""
                }
                //jezeli oboje juz wybrali
                if(it.user1choice!=0 && it.user2choice!=0){
                    val nozyczki=2131165360
                    val kamien=2131165359
                    val papier=2131165358
                    //remis
                    if(it.user1choice==it.user2choice){
                        if(it.user2choice==nozyczki){viewModel.setOpponentChoice(GameBoardViewModel.Images.SCISSORS)}
                        if(it.user2choice==kamien){viewModel.setOpponentChoice(GameBoardViewModel.Images.ROCK)}
                        if(it.user2choice==papier){viewModel.setOpponentChoice(GameBoardViewModel.Images.PAPER)}
                        viewModel.showTextDraw()
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                viewModel.increaseRoundCounter()
                                viewModel.hideUserSelectedImage(false)
                                viewModel.hideOpponentSelectedImage(false)
                                viewModel.showUserChoiceContainer()
                                viewModel.showTextYourTurn()
                                it.user1choice=0
                                it.user2choice=0
                                checkWin()
                            },
                            2000 // value in milliseconds
                        )
                    }
                    //wygranie
                    else if(it.user1choice==kamien && it.user2choice==nozyczki || it.user1choice==nozyczki && it.user2choice==papier || it.user1choice==papier && it.user2choice==kamien){
                        if(it.user2choice==nozyczki){viewModel.setOpponentChoice(GameBoardViewModel.Images.SCISSORS)}
                        if(it.user2choice==kamien){viewModel.setOpponentChoice(GameBoardViewModel.Images.ROCK)}
                        if(it.user2choice==papier){viewModel.setOpponentChoice(GameBoardViewModel.Images.PAPER)}
                        viewModel.showTextYouWin()
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                viewModel.increaseUserPoints()
                                viewModel.hideUserSelectedImage(false)
                                viewModel.hideOpponentSelectedImage(false)
                                viewModel.showUserChoiceContainer()
                                viewModel.showTextYourTurn()
                                it.user1choice=0
                                it.user2choice=0
                                checkWin()
                            },
                            2000 // value in milliseconds
                        )
                    }
                    //przegranie
                    else{
                        if(it.user2choice==nozyczki){viewModel.setOpponentChoice(GameBoardViewModel.Images.SCISSORS)}
                        if(it.user2choice==kamien){viewModel.setOpponentChoice(GameBoardViewModel.Images.ROCK)}
                        if(it.user2choice==papier){viewModel.setOpponentChoice(GameBoardViewModel.Images.PAPER)}
                        viewModel.showTextYouLose()
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                viewModel.increaseOpponentPoints()
                                viewModel.hideUserSelectedImage(false)
                                viewModel.hideOpponentSelectedImage(false)
                                viewModel.showUserChoiceContainer()
                                viewModel.showTextYourTurn()
                                it.user1choice=0
                                it.user2choice=0
                                checkWin()
                            },
                            2000 // value in milliseconds
                        )
                    }
                }
            }
            //jezeli jestes dolaczajacym
            else if(it.username2==auth.currentUser?.email)
            {
                binding.userName.text = it.username2
                binding.oponentName.text = it.username1

                //Jezeli jest przeciwnik to pokaz jego nazwe
                if(it.username1!="" && check==false){
                    check=true
                    viewModel.hideOpponentInfoText()
                    binding.oponentName.text = it.username1
                    binding.gameCodeTextView.text=""
                }

                if(it.user2choice==0){viewModel.showTextYourTurn()}
                if(it.user1choice==0 && it.user2choice!=0){viewModel.showTextWaitingForOpponentTurn()}
                if(it.user2choice!=0){viewModel.hideUserInfoText()}

                //jezeli oboje juz wybrali
                if(it.user1choice!=0 && it.user2choice!=0){
                    val nozyczki=2131165360
                    val kamien=2131165359
                    val papier=2131165358
                    //remis
                    if(it.user1choice==it.user2choice){
                        if(it.user1choice==nozyczki){viewModel.setOpponentChoice(GameBoardViewModel.Images.SCISSORS)}
                        if(it.user1choice==kamien){viewModel.setOpponentChoice(GameBoardViewModel.Images.ROCK)}
                        if(it.user1choice==papier){viewModel.setOpponentChoice(GameBoardViewModel.Images.PAPER)}
                        viewModel.showTextDraw()
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                viewModel.increaseRoundCounter()
                                viewModel.hideUserSelectedImage(false)
                                viewModel.hideOpponentSelectedImage(false)
                                viewModel.showUserChoiceContainer()
                                viewModel.showTextYourTurn()
                                it.user1choice=0
                                it.user2choice=0
                                checkWin()
                            },
                            2000 // value in milliseconds
                        )
                    }
                    //przegranie
                    else if(it.user1choice==kamien && it.user2choice==nozyczki || it.user1choice==nozyczki && it.user2choice==papier || it.user1choice==papier && it.user2choice==kamien){
                        if(it.user1choice==nozyczki){viewModel.setOpponentChoice(GameBoardViewModel.Images.SCISSORS)}
                        if(it.user1choice==kamien){viewModel.setOpponentChoice(GameBoardViewModel.Images.ROCK)}
                        if(it.user1choice==papier){viewModel.setOpponentChoice(GameBoardViewModel.Images.PAPER)}
                        viewModel.showTextYouLose()
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                viewModel.increaseOpponentPoints()
                                viewModel.hideUserSelectedImage(false)
                                viewModel.hideOpponentSelectedImage(false)
                                viewModel.showUserChoiceContainer()
                                viewModel.showTextYourTurn()
                                it.user1choice=0
                                it.user2choice=0
                                checkWin()
                            },
                            2000 // value in milliseconds
                        )
                    }
                    //wygranie
                    else{
                        if(it.user1choice==nozyczki){viewModel.setOpponentChoice(GameBoardViewModel.Images.SCISSORS)}
                        if(it.user1choice==kamien){viewModel.setOpponentChoice(GameBoardViewModel.Images.ROCK)}
                        if(it.user1choice==papier){viewModel.setOpponentChoice(GameBoardViewModel.Images.PAPER)}
                        viewModel.showTextYouWin()
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                viewModel.increaseUserPoints()
                                viewModel.hideUserSelectedImage(false)
                                viewModel.hideOpponentSelectedImage(false)
                                viewModel.showUserChoiceContainer()
                                viewModel.showTextYourTurn()
                                it.user1choice=0
                                it.user2choice=0
                                checkWin()
                            },
                            2000 // value in milliseconds
                        )
                    }

                }
            }
        }

        binding.rockBtn.setOnClickListener {
            viewModel.setUserChoice(GameBoardViewModel.Images.ROCK)
            // TODO: logika dla guzika
        }

        binding.paperBtn.setOnClickListener {
            viewModel.setUserChoice(GameBoardViewModel.Images.PAPER)
            // TODO: logika dla guzika
        }

        binding.scissorsBtn.setOnClickListener {
            viewModel.setUserChoice(GameBoardViewModel.Images.SCISSORS)
            // TODO: logika dla guzika
        }
        startGame()
    }

    private fun startGame(){
        viewModel.hideUserSelectedImage(false)
        viewModel.hideOpponentSelectedImage(false)
        viewModel.showUserChoiceContainer()
        viewModel.showTextWaitingForOpponent()
    }

    private fun checkWin(){
        var tmp=Database.game.value
        if(viewModel.round.value.toString().toInt() >= tmp?.maxRounds!! || viewModel.userPoints.value!! > (tmp?.maxRounds!!/2) || viewModel.opponentPoints.value!! > (tmp?.maxRounds!!/2)){
            //wygrales
            if(viewModel.userPoints.value.toString().toInt()>viewModel.opponentPoints.value.toString().toInt()){
                viewModel.showTextYouWinGame()
                database.child("leaderboard").child(auth.currentUser?.uid.toString()).child("roundsWon").setValue(ServerValue.increment(1))
                database.child("leaderboard").child(auth.currentUser?.uid.toString()).child("username").setValue(auth.currentUser?.email)
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        view?.findNavController()?.navigate(R.id.action_gameBoardFragment_to_gameMenuFragment)
                        database.child("games").child(tmp?.gameCode.toString()).removeValue()
                        Database.game.postValue(null)
                    },
                    2000 // value in milliseconds
                )
            }
            else if(viewModel.userPoints.value.toString().toInt()<viewModel.opponentPoints.value.toString().toInt()){
                viewModel.showTextYouLoseGame()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        view?.findNavController()?.navigate(R.id.action_gameBoardFragment_to_gameMenuFragment)
                        database.child("games").child(tmp?.gameCode.toString()).removeValue()
                        Database.game.postValue(null)
                    },
                    2000 // value in milliseconds
                )
            }
            else if (viewModel.userPoints.value.toString().toInt()==viewModel.opponentPoints.value.toString().toInt()){
                viewModel.showTextYouDrawGame()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        view?.findNavController()?.navigate(R.id.action_gameBoardFragment_to_gameMenuFragment)
                        database.child("games").child(tmp?.gameCode.toString()).removeValue()
                        Database.game.postValue(null)
                    },
                    2000 // value in milliseconds
                )
            }
        }

    }


}