package com.jmb.moviej2.model.server

import android.app.Application
import com.jmb.moviej2.R
import com.jmb.moviej2.model.RegionRepository

class MoviesRepository(application: Application) {
    private val apiKey = application.getString(R.string.api_key)
    private val regionRepository = RegionRepository(application)

    suspend fun findPopularMovies() =
        MovieDB.service
            .listPopularMoviesAsync(
                apiKey,
                regionRepository.findLastRegion()
            )
}