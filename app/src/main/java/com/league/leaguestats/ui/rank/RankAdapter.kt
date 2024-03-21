package com.league.leaguestats.ui.rank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.league.leaguestats.R
import com.league.leaguestats.data.rank.RankData
import com.league.leaguestats.data.rank.RankLeaderboard
import com.league.leaguestats.ui.rank.RankAdapter
import org.w3c.dom.Text

class RankAdapter: RecyclerView.Adapter<RankAdapter.ViewHolder>()  {
    var rankList: List<RankData> = listOf()

    /**
     * This method is used to update the five-day forecast data stored by this adapter class.
     */
    fun updateLeaderboard(leaderboard: RankLeaderboard?) {
        notifyItemRangeRemoved(0, rankList.size)
        rankList = leaderboard?.entries ?: listOf()
        notifyItemRangeInserted(0, rankList.size)
    }

    override fun getItemCount() = rankList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rank_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rankList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val summonerTV: TextView = itemView.findViewById(R.id.rank_summoner_name)
        private val winsTV: TextView = itemView.findViewById(R.id.rank_wins)
        private val lossTV: TextView = itemView.findViewById(R.id.rank_losses)
        private val leaguePointsTV: TextView = itemView.findViewById(R.id.rank_leaguepoints)

        fun bind(rankData: RankData) {
            summonerTV.text = rankData.summonerName
            winsTV.text = "Wins: " + rankData.wins.toString()
            lossTV.text = "Losses: " + rankData.losses.toString()
            leaguePointsTV.text = "LP: " + rankData.leaguePoints.toString()
        }
    }
}