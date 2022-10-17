package com.udacity.asteroidradar.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.AsteroidRepository
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDB
import org.json.JSONObject
import retrofit2.HttpException


class AsteroidWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "AsteroidRefreshWorker"
    }

    private val database = AsteroidDB.getInstance(applicationContext).asteroidDao
    private val repository = AsteroidRepository(database)


    override suspend fun doWork(): Result {
        return try {
            val asteroidList = repository.getAsteroids()
            repository.dao.insertAllAsteroids(asteroidList)

            //Log.i("worker", "doWork")

            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}