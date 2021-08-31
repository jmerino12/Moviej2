package com.jmb.moviej2.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jmb.domain.Movie
import com.jmb.moviej2.ui.common.ScopedViewModel
import com.jmb.usecases.FindMovieById
import com.jmb.usecases.ToggleMovieFavorite
import kotlinx.coroutines.launch


class DetailViewModel(
    private val movieId: Int,
    private val findMovieById: FindMovieById,
    private val toggleMovieFavorite: ToggleMovieFavorite
) :
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
            try {
                _model.value = UiModel(findMovieById.invoke(movieId))
            } catch (e: Exception) {
                Log.e("DetailViewmodel", e.message.toString())
            }
        }
    }

    fun onFavoriteClicked() = launch {
        try {
            _model.value?.movie?.let {
                val updatedMovie = it.copy(favorite = !it.favorite)
                _model.value = UiModel(updatedMovie)
                toggleMovieFavorite.invoke(updatedMovie)
            }
        } catch (e: Exception) {
            Log.e(e.toString(), e.toString())
        }

    }
}
