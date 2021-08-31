package com.jmb.moviej2.di

import android.app.Application
import com.jmb.moviej2.ui.detail.DetailActivityComponent
import com.jmb.moviej2.ui.detail.DetailActivityModule
import com.jmb.moviej2.ui.main.MainActivityComponent
import com.jmb.moviej2.ui.main.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface MyMoviesComponent {

    fun plus(module: MainActivityModule): MainActivityComponent
    fun plus(module: DetailActivityModule): DetailActivityComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): MyMoviesComponent
    }
}