package com.league.leaguestats.data.champion_rotation

import com.league.leaguestats.data.RiotGamesService
import com.league.leaguestats.data.summoner.SummonerData
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChampionRotationService {
    @GET("/lol/platform/v3/champion-rotations")
    suspend fun loadChampionRotationData(
        @Query("api_key") apiKey: String
    ) : Response<FreeRotation>

    companion object {
        private var userRegion = "na1"

        private fun getBaseUrl(userRegion: String): String {
            val region = when (userRegion) {
                "na1" -> "na1"
                "jp1" -> "jp1"
                else -> "na1" // Default region
            }
            return "https://$region.api.riotgames.com"
        }

        private val BASE_URL = getBaseUrl(userRegion)

        /**
         * This method can be called as `RiotGamesService.create()` to create an object
         * implementing the RiotGamesService interface and which can be used to make calls to
         * the RiotGames API.
         */


        fun create(): ChampionRotationService {
            val moshi = Moshi.Builder()
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ChampionRotationService::class.java)
        }
    }
}