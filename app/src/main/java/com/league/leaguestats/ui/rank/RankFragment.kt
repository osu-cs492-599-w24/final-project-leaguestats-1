package com.league.leaguestats.ui.rank

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.league.leaguestats.R

class RankFragment : Fragment(R.layout.fragment_rank) {
    private val viewModel: RankViewModel by viewModels()
    private val rankAdapter = RankAdapter()

    private lateinit var rankListRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var region: String
    private lateinit var queue: String
    private lateinit var prefs: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        /*
         * Set up RecyclerView.
         */
        rankListRV = view.findViewById(R.id.rank_list)
        rankListRV.layoutManager = LinearLayoutManager(requireContext())
        rankListRV.setHasFixedSize(true)
        rankListRV.adapter = rankAdapter

        /*
         * Set up an observer on the current forecast data.  If the forecast is not null, display
         * it in the UI.
         */
        viewModel.rotation.observe(viewLifecycleOwner) { rotation ->
            if (rotation != null) {
                rankAdapter.updateLeaderboard(rotation)
                rankListRV.visibility = View.VISIBLE
                rankListRV.scrollToPosition(0)
            }
        }

        /*
         * Set up an observer on the error associated with the current API call.  If the error is
         * not null, display the error that occurred in the UI.
         */
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                loadingErrorTV.text = "error"
                loadingErrorTV.visibility = View.VISIBLE
                Log.e(tag, "Error fetching rank leaderboard: ${error.message}")
                error.printStackTrace()
            }
        }

        /*
         * Set up an observer on the loading status of the API query.  Display the correct UI
         * elements based on the current loading status.
         */
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                loadingErrorTV.visibility = View.INVISIBLE
                rankListRV.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        Log.d("RankFragment", "Attempting to update region.")
        region = prefs.getString(getString(R.string.key_region), "na1").toString()
        queue = prefs.getString(getString(R.string.rank_queue), "RANKED_SOLO_5x5").toString()
        viewModel.loadRankData(queue,getString(R.string.riotgames_api_key), region)
    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "Attempting to update region.")
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        region = prefs.getString(getString(R.string.key_region), "na1").toString()
        queue = prefs.getString(getString(R.string.rank_queue), "RANKED_SOLO_5x5").toString()
        viewModel.loadRankData(queue, getString(R.string.riotgames_api_key), region)
    }
}
