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
import com.jmb.moviej2.ui.common.loadUrl


class MovieDetail : Fragment() {

    private val args: MovieDetailArgs by navArgs()

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.movie.run {
            binding.movieDetailToolbar.title = title
            val background = backdropPath ?: posterPath
            binding.movieDetailImage.loadUrl("https://image.tmdb.org/t/p/w780$background")
            binding.movieDetailSummary.text = overview

            binding.movieDetailInfo.text = buildSpannedString {
                bold { append("Original language: ") }
                appendLine(originalLanguage)

                bold { append("Original title: ") }
                appendLine(originalTitle)

                bold { append("Release date: ") }
                appendLine(releaseDate)

                bold { append("Popularity: ") }
                appendLine(popularity.toString())

                bold { append("Vote Average: ") }
                append(voteAverage.toString())
            }
        }
    }
}