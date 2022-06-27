package com.paperrockscissors.view.joinGameView

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.paperrockscissors.R
import com.paperrockscissors.databinding.JoinGameFragmentBinding

class JoinGameFragment : Fragment() {
    private lateinit var viewModel: JoinGameViewModel
    private lateinit var binding: JoinGameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = JoinGameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[JoinGameViewModel::class.java]

        binding.joingameBtn.setOnClickListener {
            val gameCode = binding.gameCode.text.toString()

            if (gameCode.length != 6) {
                showWrongCodeToast()
            }
            else{
                viewModel.joinGame(
                    gameCode.toInt(),
                    { it.findNavController().navigate(R.id.action_joinGameFragment_to_gameBoardFragment) },
                    { showWrongCodeToast() }
                )
            }
        }

        binding.backMenuBtnBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_joinGameFragment_to_gameMenuFragment)
        }
    }

    private fun showWrongCodeToast() {
        Toast.makeText(requireContext(), "Niepoprawny kod gry", Toast.LENGTH_SHORT).show()
    }
}