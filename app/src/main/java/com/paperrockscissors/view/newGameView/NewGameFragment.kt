package com.paperrockscissors.view.newGameView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.paperrockscissors.R
import com.paperrockscissors.databinding.NewGameFragmentBinding


class NewGameFragment : Fragment() {
    private lateinit var viewModel: NewGameViewModel
    private lateinit var binding: NewGameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewGameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NewGameViewModel::class.java]

        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, viewModel.roundsValues)
        binding.gameRoundsDropdown.setAdapter(adapter)
        binding.gameRoundsDropdown.setText(adapter.getItem(0).toString(), false)

        binding.newGameBtn.setOnClickListener {
            val rounds = binding.gameRoundsDropdown.text.toString().toInt()
            viewModel.createNewGame(rounds) {
                it.findNavController().navigate(R.id.action_newGameFragment_to_gameBoardFragment)
            }
         }

        binding.backMenuBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_newGameFragment_to_gameMenuFragment)
        }
    }
}