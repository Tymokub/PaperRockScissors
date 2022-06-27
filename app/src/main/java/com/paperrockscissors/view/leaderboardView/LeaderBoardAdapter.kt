package com.paperrockscissors.view.leaderboardView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.paperrockscissors.R
import com.paperrockscissors.model.UserLeaderBoardModel

class LeaderBoardAdapter (private var leaderBoardList: List<UserLeaderBoardModel>): RecyclerView.Adapter<LeaderBoardAdapter.Holder>() {
    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun setRow(rankPosition: Int, username: String, roundsWon: Int) {
            itemView.findViewById<TextView>(R.id.roundsWon).text = roundsWon.toString()
            itemView.findViewById<TextView>(R.id.username).text = username

            val realRankPosition = rankPosition + 1

            val rankBox = itemView.findViewById<TextView>(R.id.rankPosition)
            rankBox.text = realRankPosition.toString() + "."

            when (realRankPosition) {
                1 -> rankBox.background = ContextCompat.getDrawable(itemView.context, R.color.firstPlace)
                2 -> rankBox.background = ContextCompat.getDrawable(itemView.context, R.color.secondPlace)
                3 -> rankBox.background = ContextCompat.getDrawable(itemView.context, R.color.thirdPlace)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leader_board_item_fragment, parent, false) as View
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val user = leaderBoardList[position]
        holder.setRow(position, user.username!!, user.roundsWon!!)
    }

    override fun getItemCount() = leaderBoardList.count()

    fun updateList(newList: List<UserLeaderBoardModel>) {
        leaderBoardList = newList
        notifyDataSetChanged()
    }
}