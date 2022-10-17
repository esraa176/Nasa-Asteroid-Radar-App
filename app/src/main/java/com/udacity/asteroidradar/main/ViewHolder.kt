package com.udacity.asteroidradar.main

import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.databinding.NewAsteroidBinding

class ViewHolder(private val binding: NewAsteroidBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bindAsteroid(clickListener: (asteroid: AsteroidEntity) -> Unit, asteroid: AsteroidEntity) {
        binding.asteroid = asteroid
        binding.root.setOnClickListener {clickListener(asteroid)}
        binding.executePendingBindings()
    }
}