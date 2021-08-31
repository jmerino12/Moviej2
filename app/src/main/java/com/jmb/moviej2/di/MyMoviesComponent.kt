package com.jmb.moviej2.di

import android.app.Application
import com.jmb.moviej2.ui.detail.DetailViewModel
import com.jmb.moviej2.ui.main.MainViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, UseCaseModule::class, ViewModelsModule::class])
interface MyMoviesComponent {

    val mainViewModel: MainViewModel
    val detaiViewModel: DetailViewModel

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): MyMoviesComponent
    }
}