package com.jmb.moviej2.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.jmb.moviej2.databinding.FragmentMovieDetailBinding
import com.jmb.moviej2.model.server.MoviesRepository
import com.jmb.moviej2.ui.common.app
import com.jmb.moviej2.ui.common.getViewModel
import com.jmb.moviej2.ui.common.loadUrl


class MovieDetail : Fragment() {

    private val args: MovieDetailArgs by navArgs()

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private var movie: Int? = null
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = if (args.movie == null) -1 else args.movie
        viewModel = getViewModel {
            DetailViewModel(
                movie!!,
                MoviesRepository(application = requireActivity().app)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        viewModel.model.observe(viewLifecycleOwner, Observer(::updateUi))
        return binding.root
    }

    private fun updateUi(model: DetailViewModel.UiModel) = with(binding) {
        val movie = model.movie
        movieDetailToolbar.title = movie.title
        movieDetailImage.loadUrl("https://image.tmdb.org/t/p/w780${movie.backdropPath}")
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
    }
}