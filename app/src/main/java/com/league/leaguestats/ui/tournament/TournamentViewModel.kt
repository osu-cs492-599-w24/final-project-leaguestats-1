package com.league.leaguestats.ui.tournament

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.league.leaguestats.data.RiotGamesService
import com.league.leaguestats.data.summoner.SummonerData
import com.league.leaguestats.data.summoner.SummonerDataRepository
import kotlinx.coroutines.launch

class TournamentViewModel : ViewModel() {
    private val repository = SummonerDataRepository(RiotGamesService.create())

    private val _titleText = MutableLiveData<String>().apply {
        value = "This is tournament Fragment"
    }
    val titleText: LiveData<String> = _titleText

    /**
     * Use tournament query
     */
}