package com.league.leaguestats.ui.champion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.league.leaguestats.data.champion_rotation.ChampionRotationService
import com.league.leaguestats.data.champion_rotation.FreeRotation
import com.league.leaguestats.data.champion_rotation.FreeRotationRepository
import kotlinx.coroutines.launch

class ChampionViewModel : ViewModel() {
    private val repository = FreeRotationRepository(ChampionRotationService.create())
    private val _rotation = MutableLiveData<FreeRotation?>(null)
    val rotation: LiveData<FreeRotation?> = _rotation
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
            _loading.value = false
            _error.value = result.exceptionOrNull()
            _rotation.value = result.getOrNull()
        }
    }

//    private val _titleText = MutableLiveData<String>().apply {
//        value = "This is champion Fragment"
//    }
//    val titleText: LiveData<String> = _titleText

    /**
     * Use champion query
     */
}