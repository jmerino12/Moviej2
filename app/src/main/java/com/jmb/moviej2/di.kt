package com.jmb.moviej2

import android.app.Application
import com.jmb.data.repository.MoviesRepository
import com.jmb.data.repository.PermissionChecker
import com.jmb.data.repository.RegionRepository
import com.jmb.data.source.LocalDataSource
import com.jmb.data.source.LocationDataSource
import com.jmb.data.source.RemoteDataSource
import com.jmb.moviej2.model.AndroidPermissionChecker
import com.jmb.moviej2.model.PlayServicesLocationDataSource
import com.jmb.moviej2.model.database.MovieDatabase
import com.jmb.moviej2.model.database.RoomDataSource
import com.jmb.moviej2.model.server.TheMovieDbDataSource
import com.jmb.moviej2.ui.detail.DetailViewModel
import com.jmb.moviej2.ui.detail.MovieDetail
import com.jmb.moviej2.ui.main.HomeFragment
import com.jmb.moviej2.ui.main.MainViewModel
import com.jmb.usecases.FindMovieById
import com.jmb.usecases.GetPopularMovies
import com.jmb.usecases.ToggleMovieFavorite
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger(Level.NONE)
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, scopesModule))
    }
}

private val appModule = module {
    single(named("apiKey")) { androidApplication().getString(R.string.api_key) }
    single { MovieDatabase.build(get()) }
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> { TheMovieDbDataSource() }
    factory<LocationDataSource> { PlayServicesLocationDataSource(get()) }
    factory<PermissionChecker> { AndroidPermissionChecker(get()) }
}

private val dataModule = module {
    factory { RegionRepository(get(), get()) }
    factory { MoviesRepository(get(), get(), get(), get(named("apiKey"))) }
}

private val scopesModule = module {
    scope(named<HomeFragment>()) {
        viewModel { MainViewModel(get()) }
        scoped { GetPopularMovies(get()) }
    }

    scope(named<MovieDetail>()) {
        viewModel { (id: Int) -> DetailViewModel(id, get(), get()) }
        scoped { FindMovieById(get()) }
        scoped { ToggleMovieFavorite(get()) }
    }
}