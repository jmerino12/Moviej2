package com.jmb.moviej2.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jmb.moviej2.R
import com.jmb.moviej2.databinding.FragmentHomeBinding
import com.jmb.moviej2.model.MovieDB
import com.jmb.moviej2.ui.common.CoroutineScopeFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class HomeFragment : CoroutineScopeFragment() {

    private val adapter = MoviesAdapter()
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.recycler.adapter = adapter
        launch {
            val location = getLocation()
            val movies = MovieDB.service.listPopularMoviesAsync(
                getString(R.string.api_key),
                getRegionFromLocation(location)
            )
            adapter.movies = movies.results
        }
        return binding.root
    }

    private fun getRegionFromLocation(location: Location?): String {
        val geocoder = Geocoder(requireActivity())
        val fromLocation = location?.let {
            geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
        }
        return fromLocation?.firstOrNull()?.countryCode ?: "US"
    }

    @SuppressLint("MissingPermission")
    private suspend fun findLastLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
        }

    private suspend fun getLocation(): Location? {
        val success = requestCoarseLocationPermission()
        return if (success) findLastLocation() else null
    }

    private suspend fun requestCoarseLocationPermission(): Boolean =
        suspendCancellableCoroutine { continuation ->
            Dexter
                .withContext(requireActivity())
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(object : BasePermissionListener() {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        continuation.resume(true)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        continuation.resume(false)
                    }
                }
                ).check()
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}