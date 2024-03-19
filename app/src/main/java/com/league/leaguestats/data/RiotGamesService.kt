package com.league.leaguestats.data

import com.league.leaguestats.data.summoner.SummonerData
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

/**
 * Riot Games API Retrofit Service
 */
interface RiotGamesService {
    /**
     * This method is used to query the Riot Games API summoner-by-name method:
     * https://na1.api.riotgames.com/lol/summoner/v4/summoners/by-name.
     * This is a suspending function, so it must be called
     * in a coroutine or within another suspending function.
     *
     * @param summonerName Specifies the user/summoner name used for the API call.
     * @param apiKey Should be a valid Riot Games API key.
     *
     * @return Returns a Retrofit `Response<>` object that will contain a [SummonerData] object
     *   if the API call was successful.
     */

    @GET("/lol/summoner/v4/summoners/by-name/{summonerName}")
    suspend fun loadSummonerData(
        @Path("summonerName") summonerName: String,
        @Query("api_key") apiKey: String
    ) : Response<SummonerData>

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

        fun create(): RiotGamesService {
            val moshi = Moshi.Builder()
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(RiotGamesService::class.java)
        }
    }
}