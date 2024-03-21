package com.league.leaguestats.data.summoner

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class MatchDataRepository (
    private val service: MatchService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private var gameId: String? = null
    private var cachedData: MatchData? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    suspend fun loadMatchData(
        gameid: String,
        apiKey: String
    ) : Result<MatchData?> {
        return if (shouldFetch(gameid)) {
            withContext(ioDispatcher) {
                try {
                    Log.d("MatchDataRepository", "Attempting to call Retrofit service.")
                    val response = service.loadMatchData(gameid, apiKey)
                    Log.d("MatchDataRepository", "Response: ${response.raw()}")
                    if (response.isSuccessful) {
                        cachedData = response.body()
                        gameId = gameid
                        timeStamp = timeSource.markNow()
                        Result.success(cachedData)
                    } else {
                        Log.e("MatchDataRepository", "Error response: ${response.errorBody()?.string()}")
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

    private fun shouldFetch(gameid: String?): Boolean =
        cachedData == null
                || gameid != gameId
                || (timeStamp + cacheMaxAge).hasPassedNow()
}