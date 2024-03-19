package com.league.leaguestats.ui.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.league.leaguestats.data.RiotGamesService
import com.league.leaguestats.data.summoner.SummonerDataRepository

class RankViewModel : ViewModel() {
    private val repository = SummonerDataRepository(RiotGamesService.create())

    private val _titleText = MutableLiveData<String>().apply {
        value = "This is rank Fragment"
    }
    val titleText: LiveData<String> = _titleText

    /**
     * Use tournament query
     */
}