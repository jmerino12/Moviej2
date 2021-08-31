package com.jmb.moviej2

import android.app.Application
import com.jmb.moviej2.di.DaggerMyMoviesComponent
import com.jmb.moviej2.di.MyMoviesComponent

class MoviesApp : Application() {

    lateinit var component: MyMoviesComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerMyMoviesComponent
            .factory()
            .create(this)
    }
}