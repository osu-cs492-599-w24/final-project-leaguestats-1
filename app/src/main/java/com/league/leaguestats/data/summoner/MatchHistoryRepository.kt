package com.league.leaguestats.data.summoner

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class MatchHistoryRepository (
    private val service: MatchService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private var puuId: String? = null
    private var cachedData: List<String>? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    suspend fun loadMatchHistoryData(
        puuid: String,
        apiKey: String
    ) : Result<List<String>?> {
        return if (shouldFetch(puuid)) {
            withContext(ioDispatcher) {
                try {
                    Log.d("MatchHistoryRepository", "Attempting to call Retrofit service.")
                    val response = service.loadMatchHistoryData(puuid, apiKey)
                    Log.d("MatchHistoryRepository", "Response: ${response.raw()}")
                    if (response.isSuccessful) {
                        cachedData = response.body()
                        puuId = puuid
                        timeStamp = timeSource.markNow()
                        Result.success(cachedData)
                    } else {
                        Log.e("MatchHistoryRepository", "Error response: ${response.errorBody()?.string()}")
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

    private fun shouldFetch(puuid: String?): Boolean =
        cachedData == null
                || puuid != puuId
                || (timeStamp + cacheMaxAge).hasPassedNow()
}