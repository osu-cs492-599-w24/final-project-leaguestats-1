package com.league.leaguestats.ui.profile

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.league.leaguestats.R
import com.league.leaguestats.data.champion_rotation.CharacterDataRepository
import com.league.leaguestats.data.champion_rotation.CharacterDataService
import com.league.leaguestats.data.champion_rotation.tempData
import com.league.leaguestats.data.summoner.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private var profileService: ProfileService
    private var matchService: MatchService
    private var characterService: CharacterDataService

    private var summonerDataRepository: SummonerDataRepository
    private var matchHistoryRepository: MatchHistoryRepository
    private var matchDataRepository: MatchDataRepository
    private var characterDataRepository: CharacterDataRepository

    private val _titleText = MutableLiveData<String>().apply { value = "PROFILE" }
    val titleText: LiveData<String> = _titleText

    private val _summonerData = MutableLiveData<SummonerData?>(null)
    val summonerData: LiveData<SummonerData?> = _summonerData

    private val _champData = MutableLiveData<tempData?>(null)
    val champData: LiveData<tempData?> = _champData

    private val _summonerName = MutableLiveData<String?>(null)
    val summonerName: LiveData<String?> = _summonerName

    private val _matchHistoryData = MutableLiveData<List<String>?>(null)
    val matchHistoryData: LiveData<List<String>?> = _matchHistoryData

    private val _matchData = MutableLiveData<List<MatchData>?>(null)
    val matchData: LiveData<List<MatchData>?> = _matchData

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private var apiKey = ""
    private var region = "na1"

    init {
        profileService = ProfileService.create(region)
        matchService = MatchService.create(region)
        characterService = CharacterDataService.create()

        summonerDataRepository = SummonerDataRepository(profileService)
        characterDataRepository = CharacterDataRepository(characterService)
        matchHistoryRepository = MatchHistoryRepository(matchService)
        matchDataRepository = MatchDataRepository(matchService)
    }

    fun setKeyAndRegion(settingsApiKey: String, settingsRegion: String) {
        region = settingsRegion
        apiKey = settingsApiKey
    }

    fun loadSummonerData(name: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = summonerDataRepository.loadSummonerData(name, apiKey)
                _summonerData.value = result.getOrNull()
                _summonerName.value = name
                _error.value = result.exceptionOrNull()
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMatchHistoryData(name: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = matchHistoryRepository.loadMatchHistoryData(name, apiKey)
                _matchHistoryData.value = result.getOrNull()
                _error.value = result.exceptionOrNull()
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMatchData(matchIds: List<String>) {
        viewModelScope.launch {
            val result2 = characterDataRepository.loadCharacterData()
            _champData.value = result2.getOrNull()
            _loading.value = true
            try {
                val matchDataDeferred = matchIds.map { matchId ->
                    async {
                        matchDataRepository.loadMatchData(matchId, apiKey)
                    }
                }
                val results = matchDataDeferred.awaitAll()

                _matchData.value = results.mapNotNull { it.getOrNull() }
                _error.value = results.mapNotNull { it.exceptionOrNull() }.firstOrNull()
            } finally {
                _loading.value = false
            }
        }
    }
}
