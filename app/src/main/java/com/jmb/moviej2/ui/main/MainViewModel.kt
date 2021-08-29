package com.jmb.moviej2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jmb.moviej2.model.database.Movie
import com.jmb.moviej2.model.server.MoviesRepository
import com.jmb.moviej2.ui.common.ScopedViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val moviesRepository: MoviesRepository) : ScopedViewModel() {

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    sealed class UiModel {
        object Loading : UiModel()
        class Content(val movies: List<Movie>) : UiModel()
        class Navigation(val movie: Movie) : UiModel()
        object RequestLocationPermission : UiModel()
        class Error(val error: Exception) : UiModel()
    }

    init {
        initScope()
    }

    private fun refresh() {
        _model.value = UiModel.RequestLocationPermission
    }

    fun onCoarsePermissionRequested() {
        launch {
            _model.value = UiModel.Loading
            try {
                _model.value = UiModel.Content(moviesRepository.findPopularMovies())
            } catch (e: Exception) {
                _model.value = UiModel.Error(e)
            }
        }
    }

    fun onMovieClicked(movie: Movie) {
        _model.value = UiModel.Navigation(movie)
    }

    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }
}
