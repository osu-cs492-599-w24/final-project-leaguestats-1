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
                try {
                    Log.d("SummonerDataRepository", "Attempting to call Retrofit service.")
                    val response = service.loadSummonerData(name, apiKey)
                    if (response.isSuccessful) {
                        Log.d("SummonerDataRepository", "Successful response: ${response.body()}")
                        cachedData = response.body()
                        summonerName = name
                        timeStamp = timeSource.markNow()
                        Result.success(cachedData)
                    } else {
                        Log.e("SummonerDataRepository", "Error response: ${response.errorBody()?.string()}")
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
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