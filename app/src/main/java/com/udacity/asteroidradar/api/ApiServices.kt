package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.models.PictureOfDay
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("neo/rest/v1/feed/")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): String

    @GET("planetary/apod/")
    suspend fun getPicture(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): PictureOfDay
}
