package com.league.leaguestats.ui.profile

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.league.leaguestats.R

class ProfileAdapter: RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    var matchHistoryList: List<String> = listOf()

    /**
     * This method is used to update the five-day forecast data stored by this adapter class.
     */
    fun loadMatchHistoryData(match: List<String>?) {
        notifyItemRangeRemoved(0, matchHistoryList.size)
        matchHistoryList = match ?: listOf()
        notifyItemRangeInserted(0, matchHistoryList.size)
    }

    override fun getItemCount() = matchHistoryList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.match_history_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("ProfileAdapter", "Binding ${matchHistoryList[position]}")
        holder.bind(matchHistoryList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val matchTV: TextView = itemView.findViewById(R.id.match_id)

        fun bind(matchData: String) {
            matchTV.text = matchData
        }
    }
}