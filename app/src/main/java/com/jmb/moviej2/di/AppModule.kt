package com.jmb.moviej2.di

import android.app.Application
import androidx.room.Room
import com.jmb.data.repository.PermissionChecker
import com.jmb.data.source.LocalDataSource
import com.jmb.data.source.LocationDataSource
import com.jmb.data.source.RemoteDataSource
import com.jmb.moviej2.R
import com.jmb.moviej2.model.AndroidPermissionChecker
import com.jmb.moviej2.model.PlayServicesLocationDataSource
import com.jmb.moviej2.model.database.MovieDatabase
import com.jmb.moviej2.model.database.RoomDataSource
import com.jmb.moviej2.model.server.TheMovieDbDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @Named("apiKey")
    fun apiKeyProvider(app: Application): String = app.getString(R.string.api_key)

    @Provides
    @Singleton
    fun databaseProvider(app: Application) = Room.databaseBuilder(
        app,
        MovieDatabase::class.java,
        "movie-db"
    ).build()

    @Provides
    fun localDataSourceProvider(db: MovieDatabase): LocalDataSource = RoomDataSource(db)

    @Provides
    fun remoteDataSourceProvider(): RemoteDataSource = TheMovieDbDataSource()

    @Provides
    fun locationDataSourceProvider(app: Application): LocationDataSource =
        PlayServicesLocationDataSource(app)

    @Provides
    fun permissionCheckerProvider(app: Application): PermissionChecker =
        AndroidPermissionChecker(app)
}