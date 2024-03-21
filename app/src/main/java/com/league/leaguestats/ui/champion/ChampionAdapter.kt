package com.league.leaguestats.ui.champion

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.league.leaguestats.data.champion_rotation.FreeRotation
import com.league.leaguestats.R
import com.league.leaguestats.data.CircleTransform
import com.league.leaguestats.data.champion_rotation.tempData
import com.league.leaguestats.data.champion_rotation.Champion


class ChampionAdapter: RecyclerView.Adapter<ChampionAdapter.ViewHolder>() {

    var freeRotationList: List<Int> = listOf()
    var ChampList: Map<String,Champion> = mapOf()

    /**
     * This method is used to update the five-day forecast data stored by this adapter class.
     */
    fun updateForecast(forecast: FreeRotation?) {
        notifyItemRangeRemoved(0, freeRotationList.size)
        freeRotationList = forecast?.freeChampionIds ?: listOf()
        notifyItemRangeInserted(0, freeRotationList.size)
    }
    fun updateChampData(champData: tempData?){
        ChampList = champData?.data ?: mapOf()
    }

    override fun getItemCount() = freeRotationList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.champion_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(freeRotationList[position], ChampList)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val champTV: TextView = itemView.findViewById(R.id.champion_id)
        private val champTitleTV: TextView = itemView.findViewById(R.id.champion_title)
        private var champName: String? = null
//        init {
//            val url = Uri.parse("https://u.gg/lol/champions/"+ champName +"/build")
//            val intent = Intent(Intent.ACTION_VIEW, url)
//
//            itemView.setOnClickListener {
//                itemView.context.startActivity(intent)
//                true
//            }
//        }

//        private val champIV: ImageView = itemView.findViewById(R.id.champion_image)
        fun bind(champRotation: Int, champMap: Map<String,Champion>) {
            champName = findXByY(champMap, champRotation.toString())
            champTV.text = champName
            Glide.with(itemView.context)
                .load("https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/" + champRotation + ".png")
                .transform(CircleTransform())
                .into(itemView.findViewById(R.id.champion_image))
            champTitleTV.text = ('"' + (champMap[champName]?.title ?: "") + '"') ?: "not found"

            val url = Uri.parse("https://u.gg/lol/champions/"+ champName +"/build")
            val intent = Intent(Intent.ACTION_VIEW, url)

            itemView.setOnClickListener {
                itemView.context.startActivity(intent)
                true
            }
        }
        // edited answer from chatgpt from the prompt:
        // if the map is formatted like <x, y>
        // in kotlin, how can i find the value of x with a given y value
        fun findXByY(map: Map<String, Champion>, y: String): String {
            for ((x, valueY) in map) {
                if (valueY.key == y) {
                    return x
                }
            }
            return "not found"
        }
    }
}