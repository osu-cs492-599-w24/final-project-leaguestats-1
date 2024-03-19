package com.league.leaguestats.data.rank

import android.util.Log
import com.league.leaguestats.data.RiotGamesService
import com.league.leaguestats.data.rank.RankData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class RankDataRepository (
    private val service: RiotGamesService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var queue: String? = null
    private var cachedData: RankData? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    suspend fun loadRankData(
        queueSelect: String,
        apiKey: String
    ) : Result<RankData?> {
        return if (shouldFetch(queueSelect)) {
            withContext(ioDispatcher) {
                try {
                    Log.d("RankDataRepository", "Attempting to call Retrofit service.")
                    val response = service.loadRankData(queueSelect, apiKey)
                    if (response.isSuccessful) {
                        Log.d("RankDataRepository", "Successful response: ${response.body()}")
                        cachedData = response.body()
                        queue = queueSelect
                        timeStamp = timeSource.markNow()
                        Result.success(cachedData)
                    } else {
                        Log.e(
                            "RankDataRepository",
                            "Error response: ${response.errorBody()?.string()}"
                        )
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

    private fun shouldFetch(queueSelect: String?): Boolean =
        cachedData == null
                || queueSelect != queue
                || (timeStamp + cacheMaxAge).hasPassedNow()
}
