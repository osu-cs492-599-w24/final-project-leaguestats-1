package com.league.leaguestats.data.rank

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json
@JsonClass(generateAdapter = true)
data class RankLeaderboard (
    val entries: List<RankData>
)

@JsonClass(generateAdapter = true)
data class RankData (
    val wins: Int,
    val losses: Int,
    val summonerName: String,
    val leaguePoints: Int
)