package com.udacity.asteroidradar.api

import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.common.Constants
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.main.ApiFilter
import com.udacity.asteroidradar.models.PictureOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class AsteroidRepository(val dao: AsteroidDao) {

    private val service: ApiServices

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    init {
        val retrofit = Retrofit.Builder()
            .client(OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            ).build())
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()

        service = retrofit.create(ApiServices::class.java)
    }

    suspend fun getPictureOfDay(): PictureOfDay? = withContext(
        Dispatchers.IO
    ) {
        try {
            service.getPicture()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAsteroids(): List<AsteroidEntity> = withContext(Dispatchers.IO)
    {
        try {
            val asteroid = service.getAsteroids(getToday(), getSeventhDay())

            val asteroidList = JSONObject(asteroid)
            parseAsteroidsJsonResult(asteroidList)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getAsteroidsFromDB(filter: ApiFilter): LiveData<List<AsteroidEntity>> {
        return when (filter) {
            ApiFilter.SHOW_WEEK -> {
                dao.getAsteroidsFromThisWeek(getToday(), getSeventhDay())
            }
            ApiFilter.SHOW_TODAY -> {
                dao.getAsteroidToday(getToday())
            }
            else -> {
                dao.getAllAsteroids()
            }
        }
    }
}