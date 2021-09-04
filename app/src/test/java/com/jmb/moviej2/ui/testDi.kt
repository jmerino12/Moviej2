package com.jmb.moviej2.ui

import com.jmb.data.repository.PermissionChecker
import com.jmb.data.source.LocalDataSource
import com.jmb.data.source.LocationDataSource
import com.jmb.data.source.RemoteDataSource
import com.jmb.domain.Movie
import com.jmb.moviej2.dataModule
import com.jmb.testshared.mockedMovie
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun initMockedDi(vararg modules: Module) {
    startKoin {
        modules(listOf(mockedAppModule, dataModule) + modules)
    }
}

private val mockedAppModule = module {
    single(named("apiKey")) { "12456" }
    single<LocalDataSource> { FakeLocalDataSource() }
    single<RemoteDataSource> { FakeRemoteDataSource() }
    single<LocationDataSource> { FakeLocationDataSource() }
    single<PermissionChecker> { FakePermissionChecker() }
    single { Dispatchers.Unconfined }
}

val defaultFakeMovies = listOf(
    mockedMovie.copy(1),
    mockedMovie.copy(2),
    mockedMovie.copy(3),
    mockedMovie.copy(4)
)

class FakeLocalDataSource : LocalDataSource {

    var movies: List<Movie> = emptyList()

    override suspend fun isEmpty() = movies.isEmpty()

    override suspend fun saveMovies(movies: List<Movie>) {
        this.movies = movies
    }

    override suspend fun getPopularMovies(): List<Movie> = movies

    override suspend fun findById(id: Int): Movie = movies.first { it.id == id }

    override suspend fun update(movie: Movie) {
        movies = movies.filterNot { it.id == movie.id } + movie
    }
}

class FakeRemoteDataSource : RemoteDataSource {

    var movies = defaultFakeMovies

    override suspend fun getPopularMovies(apiKey: String, region: String) = movies
}

class FakeLocationDataSource : LocationDataSource {
    var location = "US"

    override suspend fun findLastRegion(): String? = location
}

class FakePermissionChecker : PermissionChecker {
    var permissionGranted = true

    override suspend fun check(permission: PermissionChecker.Permission): Boolean =
        permissionGranted
}