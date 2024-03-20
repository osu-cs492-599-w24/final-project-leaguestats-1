package com.league.leaguestats.ui.champion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.league.leaguestats.data.champion_rotation.FreeRotation
import com.league.leaguestats.R
import com.league.leaguestats.data.champion_rotation.ChampData
import java.io.File
import java.net.URL



class ChampionAdapter: RecyclerView.Adapter<ChampionAdapter.ViewHolder>() {

    var freeRotationList: List<Int> = listOf()

    /**
     * This method is used to update the five-day forecast data stored by this adapter class.
     */
    fun updateForecast(forecast: FreeRotation?) {
        notifyItemRangeRemoved(0, freeRotationList.size)
        freeRotationList = forecast?.freeChampionIds ?: listOf()
        notifyItemRangeInserted(0, freeRotationList.size)
    }

    override fun getItemCount() = freeRotationList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.champion_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(freeRotationList[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val champTV: TextView = itemView.findViewById(R.id.champion_id)

        fun bind(champRotation: Int) {
            champTV.text = champRotation.toString()
        }
    }
}