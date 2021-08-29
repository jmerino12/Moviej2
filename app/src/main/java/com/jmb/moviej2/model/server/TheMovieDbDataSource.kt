package com.jmb.moviej2.model.server


import com.jmb.data.source.RemoteDataSource
import com.jmb.domain.Movie
import com.jmb.moviej2.model.toDomainMovie

class TheMovieDbDataSource : RemoteDataSource {

    override suspend fun getPopularMovies(apiKey: String, region: String): List<Movie> =
        MovieDB.service
            .listPopularMoviesAsync(apiKey, region)
            .results
            .map { it.toDomainMovie() }
}