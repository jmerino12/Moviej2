package com.jmb.moviej2.di

import com.jmb.moviej2.ui.detail.DetailViewModel
import com.jmb.moviej2.ui.main.MainViewModel
import com.jmb.usecases.FindMovieById
import com.jmb.usecases.GetPopularMovies
import com.jmb.usecases.ToggleMovieFavorite
import dagger.Module
import dagger.Provides

@Module
class ViewModelsModule {

    @Provides
    fun mainViewModelProvider(getPopularMovies: GetPopularMovies) = MainViewModel(getPopularMovies)

    @Provides
    fun detailViewModelProvider(
        findMovieById: FindMovieById,
        toggleMovieFavorite: ToggleMovieFavorite
    ): DetailViewModel {
        // TODO the id needs to be provided. We'll see it with scoped graphs
        return DetailViewModel(-1, findMovieById, toggleMovieFavorite)
    }
}