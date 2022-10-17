package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidDB
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.models.PictureOfDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

enum class ApiFilter(val value: String) { SHOW_WEEK("week"), SHOW_TODAY("today"), SHOW_SAVED("saved") }

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AsteroidRepository(AsteroidDB.getInstance(app).asteroidDao)
    var asteroids: LiveData<List<AsteroidEntity>> = MutableLiveData(emptyList())

    private val _asteroidsUpdateLiveData = MutableLiveData<ApiFilter>()
    val asteroidsUpdateLiveData: LiveData<ApiFilter>
        get() = _asteroidsUpdateLiveData

    private val _pictureLiveData = MutableLiveData<PictureOfDay>()
    val pictureLiveData: LiveData<PictureOfDay>
        get() = _pictureLiveData

    init {
        viewModelScope.launch {
            asteroids = repository.getAsteroidsFromDB(ApiFilter.SHOW_SAVED)
            _asteroidsUpdateLiveData.value = ApiFilter.SHOW_SAVED

            _pictureLiveData.value = repository.getPictureOfDay()
        }
    }

    fun updateFilter(filter: ApiFilter) {
        asteroids = repository.getAsteroidsFromDB(filter)
        _asteroidsUpdateLiveData.value = filter
    }
}