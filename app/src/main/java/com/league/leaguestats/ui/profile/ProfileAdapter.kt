package com.league.leaguestats.ui.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.league.leaguestats.R
import com.league.leaguestats.data.CircleTransform
import com.league.leaguestats.data.summoner.MatchData
import com.bumptech.glide.Glide
import com.league.leaguestats.data.champion_rotation.Champion
import com.league.leaguestats.data.champion_rotation.tempData


class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    private var summonerName: String? = null
    private var matchDataList: List<MatchData> = listOf()
    var champList: Map<String, Champion> = mapOf()

    fun loadMatchData(match: List<MatchData>?) {
        val oldSize = matchDataList.size
        matchDataList = match ?: listOf()
        notifyItemRangeRemoved(0, oldSize)
        notifyItemRangeInserted(0, matchDataList.size)
    }

    fun updateSummonerName(newName: String) {
        summonerName = newName
    }

    fun updateChampData(champData: tempData?){
        champList = champData?.data ?: mapOf()
    }

    override fun getItemCount() = matchDataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.match_history_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        summonerName?.let {
            holder.bindMatch(matchDataList[position], it, champList)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val killsTV: TextView = itemView.findViewById(R.id.match_kda)
        private val resultsTV: TextView = itemView.findViewById(R.id.match_win_loss)

        @SuppressLint("ResourceAsColor")
        fun bindMatch(matchData: MatchData, summonerName: String, champMap: Map<String, Champion>) {
            val participant = matchData.info.participants.firstOrNull { it.summonerName == summonerName }

            // edited answer from chatgpt from the prompt:
            // if the map is formatted like <x, y>
            // in kotlin, how can i find the value of x with a given y value
            fun findKeyByName(champions: Map<String, Champion>, name: String): String {
                champions.values.forEach { champion ->
                    if (champion.name == name) {
                        Log.d("findKeyByName", "Champion found: ${champion.name} with key: ${champion.key}")
                        return champion.key
                    }
                }
                Log.d("findKeyByName", "Champion not found: $name")
                return "-1"
            }

            participant?.let {
                killsTV.text = "KDA: ${it.kills}/${it.deaths}/${it.assists}"
                if (it.win) {
                    resultsTV.text = "W"
                    resultsTV.setBackgroundColor(Color.parseColor("#00CC00"))
                } else {
                    resultsTV.text = "L"
                    resultsTV.setBackgroundColor(Color.parseColor("#FF0000"))
                }

                var champName = it.championName
                var champId = findKeyByName(champMap, champName)
                Glide.with(itemView.context)
                    .load("https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/$champId.png")
                    .transform(CircleTransform())
                    .into(itemView.findViewById(R.id.image_match_champion))

            } ?: run {
                killsTV.text = "Name not found"
            }
        }
    }
}