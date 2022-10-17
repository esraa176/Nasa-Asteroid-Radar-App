package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAsteroids(asteroid: List<AsteroidEntity>)

    @Query("SELECT * FROM asteroids_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate >= :startDay AND closeApproachDate <= :endDay ORDER BY closeApproachDate ASC")
    fun getAsteroidsFromThisWeek(startDay: String, endDay: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate = :today ")
    fun getAsteroidToday(today: String): LiveData<List<AsteroidEntity>>
}