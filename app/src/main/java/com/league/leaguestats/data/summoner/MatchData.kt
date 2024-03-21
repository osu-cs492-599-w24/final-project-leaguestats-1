package com.league.leaguestats.data.summoner

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchData(
    @Json(name = "metadata") val metadata: MatchMetadata,
    @Json(name = "info") val info: MatchInfo
)

@JsonClass(generateAdapter = true)
data class MatchMetadata(
    @Json(name = "matchId") val matchId: String
)

@JsonClass(generateAdapter = true)
data class MatchInfo(
    @Json(name = "gameMode") val gameMode: String,
    @Json(name = "participants") val participants: List<Participant>
)

@JsonClass(generateAdapter = true)
data class Participant(
    @Json(name = "summonerName") val summonerName: String,
    @Json(name = "championName") val championName: String,
    @Json(name = "timePlayed") val timePlayed: Int,
    @Json(name = "win") val win: Boolean,
    @Json(name = "kills") val kills: Int,
    @Json(name = "deaths") val deaths: Int,
    @Json(name = "assists") val assists: Int
)
