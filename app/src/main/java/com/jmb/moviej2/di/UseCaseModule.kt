package com.jmb.moviej2.di

import com.jmb.data.repository.MoviesRepository
import com.jmb.usecases.FindMovieById
import com.jmb.usecases.GetPopularMovies
import com.jmb.usecases.ToggleMovieFavorite
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun getPopularMoviesProvider(moviesRepository: MoviesRepository) =
        GetPopularMovies(moviesRepository)

    @Provides
    fun findMovieByIdProvider(moviesRepository: MoviesRepository) = FindMovieById(moviesRepository)

    @Provides
    fun toggleMovieFavoriteProvider(moviesRepository: MoviesRepository) =
        ToggleMovieFavorite(moviesRepository)
}