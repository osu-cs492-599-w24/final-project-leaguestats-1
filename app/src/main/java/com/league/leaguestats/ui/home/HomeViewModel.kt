package com.league.leaguestats.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _titleText = MutableLiveData<String>().apply { value = "NOT OP.GG" }
    val titleText: LiveData<String> = _titleText
}
