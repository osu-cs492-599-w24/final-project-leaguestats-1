package com.league.leaguestats.ui.profile

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.league.leaguestats.R
import com.league.leaguestats.data.summoner.SummonerData
import com.league.leaguestats.data.CircleTransform

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val args: ProfileFragmentArgs by navArgs()
    private val viewModel: ProfileViewModel by viewModels()
    private val profileAdapter = ProfileAdapter()

    private lateinit var matchHistoryListRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    private lateinit var summonerNameTV: TextView
    private lateinit var prefs: SharedPreferences
    private lateinit var region: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        /*
         * Set up RecyclerView.
         */
        matchHistoryListRV = view.findViewById(R.id.recycler_match_history)
        matchHistoryListRV.layoutManager = LinearLayoutManager(requireContext())
        matchHistoryListRV.setHasFixedSize(true)
        matchHistoryListRV.adapter = profileAdapter

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        Log.d("ProfileFragment", "Using argument ${args.searchQuery}")
        summonerNameTV = view.findViewById(R.id.text_profile)

        viewModel.summonerData.observe(viewLifecycleOwner) { summonerData ->
            if (summonerData != null) {
                bind(summonerData)
            }
        }

        region = prefs.getString(getString(R.string.key_region), "na1").toString()
        viewModel.setKeyAndRegion(getString(R.string.riotgames_api_key), region)
        viewModel.loadSummonerData(args.searchQuery)

        viewModel.matchHistoryData.observe(viewLifecycleOwner) { matchHistory ->
            if (matchHistory != null) {
                viewModel.loadMatchData(matchHistory)
            }
        }

        viewModel.matchData.observe(viewLifecycleOwner) { matchData ->
            if (matchData != null) {
                profileAdapter.loadMatchData(matchData)
                matchHistoryListRV.visibility = View.VISIBLE
                matchHistoryListRV.scrollToPosition(0)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = "error"
                loadingErrorTV.visibility = View.VISIBLE
                Log.e(tag, "Error fetching data: ${error.message}")
                error.printStackTrace()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                matchHistoryListRV.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }

        viewModel.summonerData.observe(viewLifecycleOwner) { summonerData ->
            val summonerName = summonerData?.name
            val puuid = summonerData?.puuid
            if (summonerName != null) {
                profileAdapter.updateSummonerName(summonerName)
            }
            if (puuid != null) {
                viewModel.loadMatchHistoryData(puuid)
            }
        }

        viewModel.summonerName.observe(viewLifecycleOwner) { name ->
            if (name != null) {
                profileAdapter.updateSummonerName(name)
            }
        }

        viewModel.champData.observe(viewLifecycleOwner) {champData ->
            if (champData!=null){
                profileAdapter.updateChampData(champData)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //region = prefs.getString(getString(R.string.key_region), "na1").toString()
        //viewModel.loadSummonerData(args.searchQuery, getString(R.string.riotgames_api_key), region)
        //viewModel.loadMatchHistoryData("D5TENsrNRmN_7C0AzsV8j5r2rsSjtYiropqy_bIspU6u3ppnr49HKmxEGU8Ng5T5aElVOdFQbdIiiQ", getString(R.string.riotgames_api_key), region)
    }

    private fun bind(summonerData: SummonerData) {
        val iconUrl = "https://ddragon.leagueoflegends.com/cdn/14.5.1/img/profileicon/${summonerData.profileIconId}.png"
        Glide.with(this)
            .load(iconUrl)
            .transform(CircleTransform())
            .into(requireView().findViewById(R.id.image_profile_icon))
        summonerNameTV.text = summonerData.name
    }
}
