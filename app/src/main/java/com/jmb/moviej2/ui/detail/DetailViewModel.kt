package com.jmb.moviej2.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jmb.moviej2.model.database.Movie
import com.jmb.moviej2.model.server.MoviesRepository
import com.jmb.moviej2.ui.common.ScopedViewModel
import kotlinx.coroutines.launch


class DetailViewModel(private val movieId: Int, private val moviesRepository: MoviesRepository) :
    ScopedViewModel() {

    class UiModel(val movie: Movie)

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) findMovie()
            return _model
        }

    private fun findMovie() {
        launch {
            _model.value = UiModel(moviesRepository.findById(movieId))
        }
    }

    fun onFavoriteClicked() = launch {
        _model.value?.movie?.let {
            val updatedMovie = it.copy(favorite = !it.favorite)
            _model.value = UiModel(updatedMovie)
            moviesRepository.update(updatedMovie)
        }
    }
}
