package com.league.leaguestats.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.league.leaguestats.data.summoner.MatchHistory
import com.league.leaguestats.data.summoner.MatchHistoryRepository
import com.league.leaguestats.data.summoner.MatchService
import com.league.leaguestats.data.summoner.ProfileService
import com.league.leaguestats.data.summoner.SummonerData
import com.league.leaguestats.data.summoner.SummonerDataRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private var profileService: ProfileService? = null
    private var matchService: MatchService? = null

    private lateinit var summonerDataRepository: SummonerDataRepository
    private lateinit var matchHistoryRepository: MatchHistoryRepository

    private val _titleText = MutableLiveData<String>().apply { value = "PROFILE" }
    val titleText: LiveData<String> = _titleText

    private val _summonerData = MutableLiveData<SummonerData?>(null)
    private val _matchHistoryData = MutableLiveData<List<String>?>(null)

    val summonerData: LiveData<SummonerData?> = _summonerData
    val matchHistoryData: LiveData<List<String>?> = _matchHistoryData

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun loadSummonerData(name: String, apiKey: String, region: String) {
        /*
         * Launch a new coroutine in which to execute the API call.  The coroutine is tied to the
         * lifecycle of this ViewModel by using `viewModelScope`.
         */
        if (profileService == null || !::summonerDataRepository.isInitialized) {
            profileService = ProfileService.create(region)
            summonerDataRepository = SummonerDataRepository(profileService!!)
        }

        Log.d("ProfileViewModel", "Loading SummonerData from Repository.")
        viewModelScope.launch {
            _loading.value = true
            val result = summonerDataRepository.loadSummonerData(name, apiKey)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _summonerData.value = result.getOrNull()
        }
    }

    fun loadMatchHistoryData(name: String, apiKey: String, region: String) {
        /*
         * Launch a new coroutine in which to execute the API call.  The coroutine is tied to the
         * lifecycle of this ViewModel by using `viewModelScope`.
         */
        if (matchService == null || !::matchHistoryRepository.isInitialized) {
            matchService = MatchService.create(region)
            matchHistoryRepository = MatchHistoryRepository(matchService!!)
        }

        Log.d("ProfileViewModel", "Loading MatchHistory from Repository.")
        viewModelScope.launch {
            _loading.value = true
            val result = matchHistoryRepository.loadMatchHistoryData(name, apiKey)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _matchHistoryData.value = result.getOrNull()
        }
    }
}