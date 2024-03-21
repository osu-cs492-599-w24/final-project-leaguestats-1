package com.league.leaguestats.ui.home

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.MobileAds
import com.league.leaguestats.BannerAd
import com.league.leaguestats.R

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var searchTitle: TextView

    /*
    Home page
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(requireContext())

        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            BannerAd(modifier = Modifier.fillMaxSize(), adUnitId = "ca-app-pub-3940256099942544/6300978111")
        }

        searchTitle = view.findViewById(R.id.text_home)
        searchTitle.text = "NOT OP.GG"
        
        val searchBoxET: EditText = view.findViewById(R.id.et_search_box)

        searchBoxET.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchBoxET.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun performSearch(query: String) {
        val searchBoxET: EditText = view?.findViewById(R.id.et_search_box) ?: return
        searchBoxET.text.clear()
        val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment(query)
        findNavController().navigate(action)
    }
}
