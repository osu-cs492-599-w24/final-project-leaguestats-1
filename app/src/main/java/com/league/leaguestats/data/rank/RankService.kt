package com.league.leaguestats.data.rank

import android.util.Log
import com.league.leaguestats.data.champion_rotation.ChampionRotationService
import com.league.leaguestats.data.champion_rotation.FreeRotation
import com.league.leaguestats.data.summoner.ProfileService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RankService {
    @GET("/lol/league/v4/challengerleagues/by-queue/{queue}")
    suspend fun loadRankData(
        @Path("queue") queueSelect: String,
        @Query("api_key") apiKey: String
    ) : Response<RankLeaderboard>

    companion object {
        private fun getBaseUrl(userRegion: String): String {
            return when (userRegion) {
                "na1", "jp1", "kr" -> "https://$userRegion.api.riotgames.com"
                else -> "https://na1.api.riotgames.com"
            }
        }

        /**
         * This method can be called as `RiotGamesService.create()` to create an object
         * implementing the RiotGamesService interface and which can be used to make calls to
         * the RiotGames API.
         */

        fun create(region: String): RankService {
            val baseUrl = getBaseUrl(region)
            Log.d("RiotGamesService", "Querying with baseUrl: $baseUrl")
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            val moshi = Moshi.Builder()
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(RankService::class.java)
        }
    }

}