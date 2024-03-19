package com.league.leaguestats.data.champion_rotation

import com.league.leaguestats.data.summoner.SummonerData


import android.util.Log
import com.league.leaguestats.data.RiotGamesService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class FreeRotationRepository (
    private val service: ChampionRotationService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var cachedData: FreeRotation? = null

    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    suspend fun loadRotationData(
        apiKey: String
    ) : Result<FreeRotation?> {
        return if (shouldFetch()) {
            withContext(ioDispatcher) {
                try {
                    Log.d("FreeRotationDataRepository", "Attempting to call Retrofit service.")
                    val response = service.loadChampionRotationData(apiKey)
                    if (response.isSuccessful) {
                        Log.d("FreeRotationDataRepository", "Successful response: ${response.body()}")
                        cachedData = response.body()
                        timeStamp = timeSource.markNow()
                        Result.success(cachedData)
                    } else {
                        Log.e("FreeRotationDataRepository", "Error response: ${response.errorBody()?.string()}")
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

    private fun shouldFetch(): Boolean =
        cachedData == null
                || (timeStamp + cacheMaxAge).hasPassedNow()
}