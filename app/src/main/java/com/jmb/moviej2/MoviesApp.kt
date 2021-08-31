package com.jmb.moviej2

import android.app.Application

class MoviesApp : Application() {


    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}