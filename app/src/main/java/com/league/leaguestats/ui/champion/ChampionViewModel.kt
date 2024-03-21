package com.league.leaguestats.ui.champion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.league.leaguestats.data.champion_rotation.ChampionRotationService
import com.league.leaguestats.data.champion_rotation.CharacterDataRepository
import com.league.leaguestats.data.champion_rotation.CharacterDataService
import com.league.leaguestats.data.champion_rotation.FreeRotation
import com.league.leaguestats.data.champion_rotation.FreeRotationRepository
import com.league.leaguestats.data.champion_rotation.tempData
import kotlinx.coroutines.launch

class ChampionViewModel : ViewModel() {
    private val repository = FreeRotationRepository(ChampionRotationService.create())
    private val repository2 = CharacterDataRepository(CharacterDataService.create())
    private val _rotation = MutableLiveData<FreeRotation?>(null)
    private val _champData = MutableLiveData<tempData?>(null)
    val rotation: LiveData<FreeRotation?> = _rotation
    val champData: LiveData<tempData?> = _champData
    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?> = _error

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun loadRotationData(apiKey: String) {
        /*
         * Launch a new coroutine in which to execute the API call.  The coroutine is tied to the
         * lifecycle of this ViewModel by using `viewModelScope`.
         */
        viewModelScope.launch {
            _loading.value = true
            val result = repository.loadRotationData(apiKey)
            val result2 = repository2.loadCharacterData()
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _rotation.value = result.getOrNull()
            _champData.value = result2.getOrNull()
        }
    }
//    fun loadChampionData(){
//        viewModelScope.launch{
//            _loading.value = true
//            val result2 = repository2.loadCharacterData()
//            _loading.value = false
//            _error.value = result2.exceptionOrNull()
//            _champData.value = result2.getOrNull()
//        }
//    }

//    private val _titleText = MutableLiveData<String>().apply {
//        value = "This is champion Fragment"
//    }
//    val titleText: LiveData<String> = _titleText

    /**
     * Use champion query
     */
}