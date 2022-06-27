package com.paperrockscissors.view.gameMenuView

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paperrockscissors.R
import com.paperrockscissors.databinding.GameMenuFragmentBinding

class GameMenuFragment : Fragment() {
    private lateinit var viewModel: GameMenuViewModel
    private lateinit var binding: GameMenuFragmentBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GameMenuFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GameMenuViewModel::class.java]

        val navController = view.findNavController()

        binding.newGameBtn.setOnClickListener {
            navController.navigate(R.id.action_gameMenuFragment_to_newGameFragment)
        }

        binding.joinGameBtn.setOnClickListener {
            navController.navigate(R.id.action_gameMenuFragment_to_joinGameFragment)
        }

        binding.leaderboardBtn.setOnClickListener {
            navController.navigate(R.id.action_gameMenuFragment_to_leaderBoardFragment)
        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            navController.navigate(R.id.action_gameMenuFragment_to_authFragment)
        }
    }
}