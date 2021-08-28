package com.jmb.moviej2.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jmb.moviej2.databinding.FragmentMovieDetailBinding
import com.jmb.moviej2.model.Movie
import com.jmb.moviej2.ui.common.loadUrl


class MovieDetail : Fragment(), DetailPresenter.View {

    private val args: MovieDetailArgs by navArgs()

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val presenter = DetailPresenter()
    private var movie: Movie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = args.movie
            ?: throw (IllegalStateException("Movie not found"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        presenter.onCreate(this@MovieDetail, movie!!)
        return binding.root
    }


    override fun updateUI(movie: Movie) = with(binding) {
        movieDetailToolbar.title = movie.title
        val background = movie.backdropPath ?: movie.posterPath
        movieDetailImage.loadUrl("https://image.tmdb.org/t/p/w780$background")
        movieDetailSummary.text = movie.overview

        movieDetailInfo.text = buildSpannedString {
            bold { append("Original language: ") }
            appendLine(movie.originalLanguage)

            bold { append("Original title: ") }
            appendLine(movie.originalTitle)

            bold { append("Release date: ") }
            appendLine(movie.releaseDate)

            bold { append("Popularity: ") }
            appendLine(movie.popularity.toString())

            bold { append("Vote Average: ") }
            append(movie.voteAverage.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        presenter.onDestroy()
    }
}