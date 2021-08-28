package com.jmb.moviej2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmb.moviej2.R
import com.jmb.moviej2.databinding.FragmentHomeBinding
import com.jmb.moviej2.model.MovieDB
import com.jmb.moviej2.ui.common.CoroutineScopeFragment
import kotlinx.coroutines.launch

class HomeFragment : CoroutineScopeFragment() {

    private val adapter = MoviesAdapter()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.recycler.adapter = adapter
        launch {
            val movies = MovieDB.service.listPopularMoviesAsync(getString(R.string.api_key))
            adapter.movies = movies.results
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}