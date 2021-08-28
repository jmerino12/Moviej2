package com.jmb.moviej2.ui.main

import com.jmb.moviej2.model.Movie
import com.jmb.moviej2.model.MoviesRepository
import com.jmb.moviej2.ui.common.Scope
import kotlinx.coroutines.launch

class MainPresenter(private val moviesRepository: MoviesRepository) : Scope by Scope.Impl() {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun updateData(movies: List<Movie>)
        fun navigateTo(movie: Movie)
    }

    private var view: View? = null

    fun onCreate(view: View) {
        initScope()
        this.view = view

        launch {
            view.showProgress()
            view.updateData(moviesRepository.findPopularMovies().results)
            view.hideProgress()
        }
    }

    fun onMovieClicked(movie: Movie) {
        view?.navigateTo(movie)
    }

    fun onDestroy() {
        this.view = null
        destroyScope()
    }
}