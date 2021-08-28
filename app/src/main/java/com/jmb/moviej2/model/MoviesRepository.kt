package com.jmb.moviej2.model

import android.app.Activity
import com.jmb.moviej2.R

class MoviesRepository(activity: Activity) {
    private val apiKey = activity.getString(R.string.api_key)
    private val regionRepository = RegionRepository(activity)

    suspend fun findPopularMovies() =
        MovieDB.service
            .listPopularMoviesAsync(
                apiKey,
                regionRepository.findLastRegion()
            )
}