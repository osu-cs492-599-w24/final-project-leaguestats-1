package com.league.leaguestats.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.league.leaguestats.R
import com.league.leaguestats.data.summoner.SummonerData
import com.league.leaguestats.data.CircleTransform

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val args: ProfileFragmentArgs by navArgs()
    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var summonerNameTV: TextView
    private lateinit var prefs: SharedPreferences
    private lateinit var region: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        Log.d("ProfileFragment", "Using argument ${args.searchQuery}")
        summonerNameTV = view.findViewById(R.id.text_profile)

        viewModel.summonerData.observe(viewLifecycleOwner) { summonerData ->
            if (summonerData != null) {
                bind(summonerData)
            }
        }
        region = prefs.getString(getString(R.string.key_region), "na1").toString()
        viewModel.loadSummonerData(args.searchQuery, getString(R.string.riotgames_api_key), region)
    }

    override fun onResume() {
        super.onResume()
        region = prefs.getString(getString(R.string.key_region), "na1").toString()
        viewModel.loadSummonerData(args.searchQuery, getString(R.string.riotgames_api_key), region,)
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
