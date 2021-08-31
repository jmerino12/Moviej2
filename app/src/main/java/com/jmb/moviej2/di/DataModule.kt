package com.jmb.moviej2.di

import com.jmb.data.repository.MoviesRepository
import com.jmb.data.repository.PermissionChecker
import com.jmb.data.repository.RegionRepository
import com.jmb.data.source.LocalDataSource
import com.jmb.data.source.LocationDataSource
import com.jmb.data.source.RemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DataModule {

    @Provides
    fun regionRepositoryProvider(
        locationDataSource: LocationDataSource,
        permissionChecker: PermissionChecker
    ) = RegionRepository(locationDataSource, permissionChecker)

    @Provides
    fun moviesRepositoryProvider(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
        regionRepository: RegionRepository,
        @Named("apiKey") apiKey: String
    ) = MoviesRepository(localDataSource, remoteDataSource, regionRepository, apiKey)
}