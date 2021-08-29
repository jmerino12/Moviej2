package com.jmb.moviej2

import android.app.Application
import androidx.room.Room
import com.jmb.moviej2.model.database.MovieDatabase

class MoviesApp : Application() {

    lateinit var db: MovieDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            MovieDatabase::class.java, "movie-db"
        ).build()
    }
}