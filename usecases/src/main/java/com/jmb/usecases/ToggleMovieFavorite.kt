package com.jmb.usecases

import com.jmb.data.repository.MoviesRepository
import com.jmb.domain.Movie

class ToggleMovieFavorite(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(movie: Movie): Movie = with(movie) {
        copy(favorite = favorite).also { moviesRepository.update(movie) }
    }
}