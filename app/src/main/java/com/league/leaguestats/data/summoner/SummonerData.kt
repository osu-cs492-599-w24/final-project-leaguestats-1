package com.league.leaguestats.data.summoner

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

/**
 * This class encapsulates data about a summoner fetched by their name from the Riot Games API.
 */

@JsonClass(generateAdapter = true)
data class SummonerData(
    val id: String,
    val accountId: String,
    val puuid: String,
    val name: String,
    val profileIconId: Int,
    val revisionDate: Long,
    val summonerLevel: Int
)
