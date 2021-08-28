package com.jmb.moviej2.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jmb.moviej2.databinding.FragmentHomeBinding
import com.jmb.moviej2.model.Movie
import com.jmb.moviej2.model.MoviesRepository
import com.jmb.moviej2.ui.common.navigateTo

class HomeFragment : Fragment(), MainPresenter.View {


    private val presenter by lazy { MainPresenter(MoviesRepository(requireActivity())) }
    private val adapter = MoviesAdapter { presenter.onMovieClicked(it) }


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.apply {
            recycler.adapter = adapter
            presenter.onCreate(this@HomeFragment)
        }

        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        _binding = null
    }

    override fun showProgress() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progress.visibility = View.GONE
    }

    override fun updateData(movies: List<Movie>) {
        adapter.movies = movies
    }

    override fun navigateTo(movie: Movie) {
        navigateTo<HomeFragment>(HomeFragmentDirections.actionHomeToMovieDetail(movie))
    }
}