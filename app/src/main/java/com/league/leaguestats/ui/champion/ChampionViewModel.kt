package com.league.leaguestats.ui.champion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.league.leaguestats.data.summoner.ProfileService
import com.league.leaguestats.data.summoner.SummonerDataRepository

class ChampionViewModel : ViewModel() {
    private lateinit var repository: SummonerDataRepository
    private var profileService: ProfileService? = null
    
    private val _titleText = MutableLiveData<String>().apply {
        value = "This is champion Fragment"
    }
    val titleText: LiveData<String> = _titleText

    /**
     * Use champion query
     */
}