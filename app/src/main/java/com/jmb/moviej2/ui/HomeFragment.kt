package com.jmb.moviej2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmb.moviej2.databinding.FragmentHomeBinding
import com.jmb.moviej2.model.MoviesRepository
import com.jmb.moviej2.ui.common.CoroutineScopeFragment
import kotlinx.coroutines.launch

class HomeFragment : CoroutineScopeFragment() {

    private val adapter = MoviesAdapter {
        navigateTo<HomeFragment>(HomeFragmentDirections.actionHomeToMovieDetail(it))
    }
    private val moviesRepository: MoviesRepository by lazy { MoviesRepository(requireActivity()) }


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.recycler.adapter = adapter
        launch {
            adapter.movies = moviesRepository.findPopularMovies().results
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}