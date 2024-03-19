package com.league.leaguestats.ui.rank

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.league.leaguestats.data.RiotGamesService
import com.league.leaguestats.data.rank.RankData
import com.league.leaguestats.data.rank.RankDataRepository
import kotlinx.coroutines.launch

class RankViewModel : ViewModel() {
    private val repository = RankDataRepository(RiotGamesService.create())

    private val _titleText = MutableLiveData<String>().apply {
        value = "This is rank Fragment"
    }
    val titleText: LiveData<String> = _titleText

    private val _rankData = MutableLiveData<RankData?>(null)
    val rankData: LiveData<RankData?> = _rankData
    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading
    fun loadRankData(queueSelect: String, apiKey: String) {
        /*
         * Launch a new coroutine in which to execute the API call.  The coroutine is tied to the
         * lifecycle of this ViewModel by using `viewModelScope`.
         */
        Log.d("RankViewModel", "Loading RankData from Repository.")
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadRankData(queueSelect, apiKey)
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _rankData.value = result.getOrNull()
        }
    }
    /**
     * Use tournament query
     */
}