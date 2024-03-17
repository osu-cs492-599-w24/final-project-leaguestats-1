package com.league.leaguestats.data.summoner

import android.util.Log
import com.league.leaguestats.data.RiotGamesService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class SummonerDataRepository (
    private val service: RiotGamesService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var summonerName: String? = null
    private var cachedData: SummonerData? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    suspend fun loadSummonerData(
        name: String,
        apiKey: String
    ) : Result<SummonerData?> {
        return if (shouldFetch(name)) {
            withContext(ioDispatcher) {
                    Log.d("SummonerDataRepository", "Attempting to call Retrofit service.")
                    val response = service.loadSummonerData(name, apiKey)
                    val body = response.body()
                    if (body != null) {
                        Log.d("SummonerDataRepository", "Successful response: $body")
                    } else {
                        Log.d("SummonerDataRepository", "Response was successful but the body is null")
                        Log.d("SummonerDataRepository", "Raw response: ${response.raw()}")
                    }
                    Log.d("SummonerDataRepository", "Successful response: ${response.body()}")
                    cachedData = response.body()
                    summonerName = name
                    timeStamp = timeSource.markNow()
                    Result.success(cachedData)

            }
        } else {
            Result.success(cachedData!!)
        }
    }

    private fun shouldFetch(name: String?): Boolean =
        cachedData == null
                || name != summonerName
                || (timeStamp + cacheMaxAge).hasPassedNow()
}