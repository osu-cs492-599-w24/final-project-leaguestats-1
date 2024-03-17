package com.league.leaguestats.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.league.leaguestats.data.RiotGamesService
import com.league.leaguestats.data.summoner.SummonerData
import com.league.leaguestats.data.summoner.SummonerDataRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = SummonerDataRepository(RiotGamesService.create())

    private val _titleText = MutableLiveData<String>().apply { value = "PROFILE" }
    val titleText: LiveData<String> = _titleText

    private val _summonerData = MutableLiveData<SummonerData?>(null)
    val summonerData: LiveData<SummonerData?> = _summonerData
    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading
    fun loadSummonerData(name: String, apiKey: String) {
        /*
         * Launch a new coroutine in which to execute the API call.  The coroutine is tied to the
         * lifecycle of this ViewModel by using `viewModelScope`.
         */
        Log.d("ProfileViewModel", "Loading SummonerData from Repository.")
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadSummonerData(name, apiKey)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _summonerData.value = result.getOrNull()
        }
    }
}