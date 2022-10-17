package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.databinding.NewAsteroidBinding
import com.udacity.asteroidradar.models.Asteroid

class AsteroidAdapter(private val clickListener: (asteroid: AsteroidEntity) -> Unit) :
    RecyclerView.Adapter<ViewHolder>() {

    private lateinit var binding: NewAsteroidBinding
    private var asteroids: List<AsteroidEntity> = emptyList()

    fun setAsteroids(asteroidsList: List<AsteroidEntity>) {
        this.asteroids = asteroidsList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = NewAsteroidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid = asteroids[position]
        holder.bindAsteroid(clickListener, asteroid)
    }

    override fun getItemCount() = asteroids.size
}