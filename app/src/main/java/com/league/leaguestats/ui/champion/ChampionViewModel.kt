package com.league.leaguestats.ui.champion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.league.leaguestats.data.RiotGamesService
import com.league.leaguestats.data.summoner.SummonerData
import com.league.leaguestats.data.summoner.SummonerDataRepository
import kotlinx.coroutines.launch

class ChampionViewModel : ViewModel() {
    private val repository = SummonerDataRepository(RiotGamesService.create())

    private val _titleText = MutableLiveData<String>().apply {
        value = "This is champion Fragment"
    }
    val titleText: LiveData<String> = _titleText

    /**
     * Use champion query
     */
}