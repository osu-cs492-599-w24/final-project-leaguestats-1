package com.league.leaguestats.data.champion_rotation

import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface CharacterDataService {
    @GET("/cdn/14.6.1/data/en_US/champion.json")
    suspend fun loadChampionData() : Response<tempData>
    companion object {
        private const val BASE_URL = "https://ddragon.leagueoflegends.com"
        fun create(): CharacterDataService {
            val moshi = Moshi.Builder()
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(CharacterDataService::class.java)
        }
    }
}