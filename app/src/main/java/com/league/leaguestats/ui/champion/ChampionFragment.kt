package com.league.leaguestats.ui.champion

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.league.leaguestats.R

class ChampionFragment : Fragment(R.layout.fragment_champion) {
    private val viewModel: ChampionViewModel by viewModels()
    private val championAdapter = ChampionAdapter()

    private lateinit var championListRV: RecyclerView
    private lateinit var loadingErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingErrorTV = view.findViewById(R.id.tv_loading_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        /*
         * Set up RecyclerView.
         */
        championListRV = view.findViewById(R.id.free_champion_rotation_list)
        championListRV.layoutManager = LinearLayoutManager(requireContext())
        championListRV.setHasFixedSize(true)
        championListRV.adapter = championAdapter

        /*
         * Set up an observer on the current forecast data.  If the forecast is not null, display
         * it in the UI.
         */
        viewModel.rotation.observe(viewLifecycleOwner) { rotation ->
            if (rotation != null) {
                championAdapter.updateForecast(rotation)
                championListRV.visibility = View.VISIBLE
                championListRV.scrollToPosition(0)
            }
        }

        viewModel.champData.observe(viewLifecycleOwner) {champData ->
            if (champData!=null){
                championAdapter.updateChampData(champData)
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
                Log.e(tag, "Error fetching forecast: ${error.message}")
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
                championListRV.visibility = View.INVISIBLE
            } else {
                loadingIndicator.visibility = View.INVISIBLE
            }
        }
        viewModel.loadRotationData(getString(R.string.riotgames_api_key))
//        viewModel.loadChampionData()
    }
}
