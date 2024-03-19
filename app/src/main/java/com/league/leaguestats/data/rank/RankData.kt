package com.league.leaguestats.data.rank

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RankData (
    val wins: Int,
    val losses: Int,
    val summonerName: String,
    val summonerId: String,
    val leaguePoints: Int,
)