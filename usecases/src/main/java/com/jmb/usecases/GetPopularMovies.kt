package com.jmb.usecases

import com.jmb.data.repository.MoviesRepository
import com.jmb.domain.Movie

class GetPopularMovies(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(): List<Movie> = moviesRepository.getPopularMovies()
}