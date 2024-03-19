package com.league.leaguestats.ui.rank

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.league.leaguestats.R
import com.league.leaguestats.data.CircleTransform
import com.league.leaguestats.data.rank.RankData

class RankFragment : Fragment(R.layout.fragment_rank) {
    private val args: RankFragmentArgs by navArgs()
    private val viewModel: RankViewModel by viewModels()

    private lateinit var summonerNameTV: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ProfileFragment", "Using argument ${args.searchQuery}")
        summonerNameTV = view.findViewById(R.id.text_rank)

        viewModel.rankData.observe(viewLifecycleOwner) { rankData ->
            if (rankData != null) {
                bind(rankData)
            }
        }
        viewModel.loadRankData(args.searchQuery, getString(R.string.riotgames_api_key))
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadRankData(args.searchQuery, getString(R.string.riotgames_api_key))
    }

    private fun bind(rankData: RankData) {
        summonerNameTV.text = rankData.summonerName
    }
}
