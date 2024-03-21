package com.league.leaguestats.ui.rank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.league.leaguestats.data.rank.RankDataRepository
import com.league.leaguestats.data.rank.RankService
import com.league.leaguestats.data.rank.RankData
import com.league.leaguestats.data.rank.RankLeaderboard
import com.league.leaguestats.data.summoner.SummonerDataRepository
import kotlinx.coroutines.launch

class RankViewModel : ViewModel() {
    private lateinit var repository: RankDataRepository
    private var rankService: RankService? = null

    private val _rotation = MutableLiveData<RankLeaderboard>(null)
    val rotation: LiveData<RankLeaderboard> = _rotation
    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)

    val loading: LiveData<Boolean> = _loading

    fun loadRankData(queue: String, apiKey: String, region: String) {
        /*
         * Launch a new coroutine in which to execute the API call.  The coroutine is tied to the
         * lifecycle of this ViewModel by using `viewModelScope`.
         */
        rankService = RankService.create(region)
        repository = RankDataRepository(rankService!!)

        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadRankData(queue, apiKey)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _rotation.value = result.getOrNull()
        }
    }
}