package com.league.leaguestats.ui.tft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.league.leaguestats.data.RiotGamesService
import com.league.leaguestats.data.summoner.SummonerDataRepository

class TFTViewModel : ViewModel() {
    private val repository = SummonerDataRepository(RiotGamesService.create())

    private val _titleText = MutableLiveData<String>().apply {
        value = "This is tournament Fragment"
    }
    val titleText: LiveData<String> = _titleText

    /**
     * Use tournament query
     */
}