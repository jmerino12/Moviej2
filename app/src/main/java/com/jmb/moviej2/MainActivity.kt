package com.jmb.moviej2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jmb.moviej2.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = MoviesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.adapter = adapter

        GlobalScope.launch(Dispatchers.Main) {
            val movies = MovieDB.service.listPopularMoviesAsync(getString(R.string.api_key))
            adapter.movies = movies.results
        }
    }
}