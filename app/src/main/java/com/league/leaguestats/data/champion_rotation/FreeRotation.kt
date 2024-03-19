package com.league.leaguestats.data.champion_rotation

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FreeRotation(
    val freeChampionIds : List<Int>
)
