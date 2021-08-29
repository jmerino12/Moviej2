package com.jmb.usecases

import com.jmb.data.repository.MoviesRepository
import com.jmb.domain.Movie

class FindMovieById(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(id: Int): Movie = moviesRepository.findById(id)
}