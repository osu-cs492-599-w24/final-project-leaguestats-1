package com.league.leaguestats.ui.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.league.leaguestats.data.summoner.ProfileService
import com.league.leaguestats.data.summoner.SummonerDataRepository

class RankViewModel : ViewModel() {
    private lateinit var repository: SummonerDataRepository
    private var profileService: ProfileService? = null

    private val _titleText = MutableLiveData<String>().apply {
        value = "This is rank Fragment"
    }
    val titleText: LiveData<String> = _titleText

    /**
     * Use tournament query
     */
}