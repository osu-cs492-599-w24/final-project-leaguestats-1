package com.league.leaguestats.data.summoner

import android.util.Log
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

/**
 * Riot Games API Retrofit Service
 */
interface ProfileService {
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

    @GET("/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    suspend fun loadLeagueData(
        @Path("encryptedSummonerId") encryptedSummonerId: String,
        @Query("api_key") apiKey: String
    ) : Response<List<LeagueData>>

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

        fun create(region: String): ProfileService {
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
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ProfileService::class.java)
        }
    }
}