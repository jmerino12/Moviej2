package com.jmb.moviej2.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jmb.moviej2.PermissionRequester
import com.jmb.moviej2.databinding.FragmentHomeBinding
import com.jmb.moviej2.ui.common.app
import com.jmb.moviej2.ui.common.getViewModel
import com.jmb.moviej2.ui.common.navigateTo

class HomeFragment : Fragment() {

    private lateinit var component: MainActivityComponent
    private val viewModel: MainViewModel by lazy { getViewModel { component.mainViewModel } }
    private lateinit var adapter: MoviesAdapter
    private lateinit var coarsePermissionRequester: PermissionRequester


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        coarsePermissionRequester =
            PermissionRequester(this.requireActivity(), ACCESS_COARSE_LOCATION)
        component = requireActivity().app.component.plus(MainActivityModule())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = MoviesAdapter(viewModel::onMovieClicked)
        binding.recycler.adapter = adapter
        viewModel.model.observe(viewLifecycleOwner, Observer(::updateUi))
        return binding.root
    }

    private fun updateUi(model: MainViewModel.UiModel) {
        binding.progress.visibility =
            if (model is MainViewModel.UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is MainViewModel.UiModel.Content -> adapter.movies = model.movies
            is MainViewModel.UiModel.Navigation -> {
                navigateTo<HomeFragment>(
                    HomeFragmentDirections.actionHomeToMovieDetail(
                        model.movie.id!!
                    )

                )
                viewModel.onNavigateDone()
            }
            MainViewModel.UiModel.RequestLocationPermission -> coarsePermissionRequester.request {
                viewModel.onCoarsePermissionRequested()
            }
            is MainViewModel.UiModel.Error -> Log.e(tag, model.error.toString())
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}