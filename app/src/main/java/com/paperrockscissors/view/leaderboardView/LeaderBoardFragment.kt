package com.paperrockscissors.view.leaderboardView

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.paperrockscissors.R
import com.paperrockscissors.databinding.LeaderBoardFragmentBinding
import com.paperrockscissors.utils.Database

class LeaderBoardFragment : Fragment() {
    private lateinit var viewModel: LeaderBoardViewModel
    private lateinit var binding: LeaderBoardFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  LeaderBoardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LeaderBoardViewModel::class.java]

        binding.leaderboardView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModel.adapter
        }

        binding.backMenuBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_leaderBoardFragment_to_gameMenuFragment)
        }

        Database.leaderboard.observe(viewLifecycleOwner) {
            viewModel.adapter.updateList(it)
        }
    }
}