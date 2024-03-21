package com.league.leaguestats.data.rank

import android.util.Log
import com.league.leaguestats.data.champion_rotation.ChampionRotationService
import com.league.leaguestats.data.champion_rotation.FreeRotation
import com.league.leaguestats.data.rank.RankService
import com.league.leaguestats.data.rank.RankData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class RankDataRepository (
    private val service: RankService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val queueSelect: String? = null
    private var cachedData: RankLeaderboard? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    suspend fun loadRankData(
        queue: String,
        apiKey: String
    ) : Result<RankLeaderboard?> {
        return if (shouldFetch(queue)) {
            withContext(ioDispatcher) {
                try {
                    Log.d("FreeRankDataRepository", "Attempting to call Retrofit service.")
                    val response = service.loadRankData(queue, apiKey)

                    if (response.isSuccessful) {
                        Log.d("FreeRankRepository", "Successful response: ${response.body()}")
                        cachedData = response.body()
                        timeStamp = timeSource.markNow()
                        Result.success(cachedData)
                    } else {
                        Log.e("FreeRankRepository", "Error response: ${response.errorBody()?.string()}")
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

    private fun shouldFetch(queue: String?): Boolean =
        cachedData == null
                || queue != queueSelect
                || (timeStamp + cacheMaxAge).hasPassedNow()

}
