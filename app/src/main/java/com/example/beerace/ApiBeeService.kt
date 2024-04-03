package com.example.beerace

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiBeeService {
    @GET("status")
    fun getBeesStatus(): Call<BeeResponse>

    @GET("duration")
    fun getRaceDuration(): Call<RaceDurationReponse>

    companion object {
        private const val BASE_URL : String = "https://rtest.proxy.beeceptor.com/bees/race/"

        fun create(): ApiBeeService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiBeeService::class.java)
        }
    }
}