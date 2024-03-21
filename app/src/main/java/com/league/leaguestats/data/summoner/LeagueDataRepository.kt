package com.league.leaguestats.data.summoner

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class LeagueDataRepository (
    private val service: ProfileService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private var encId: String? = null
    private var cachedData: List<LeagueData>? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    suspend fun loadLeagueData(
        id: String,
        apiKey: String
    ) : Result<List<LeagueData>?> {
        return if (shouldFetch(id)) {
            withContext(ioDispatcher) {
                try {
                    Log.d("MatchDataRepository", "Attempting to call Retrofit service.")
                    val response = service.loadLeagueData(id, apiKey)
                    Log.d("MatchDataRepository", "Response: ${response.raw()}")
                    if (response.isSuccessful) {
                        cachedData = response.body()
                        encId = id
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

    private fun shouldFetch(id: String?): Boolean =
        cachedData == null
                || id != encId
                || (timeStamp + cacheMaxAge).hasPassedNow()
}