package com.league.leaguestats.data.champion_rotation

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class tempData(
    val data: Map<String, Champion>
)
@JsonClass(generateAdapter = true)
data class Champion(
    val id: String,
    val key: String,
    val name: String
)
