package com.league.leaguestats.data.summoner

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchHistory(
    val matchIds : List<String>
)
