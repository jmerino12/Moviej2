package com.jmb.moviej2.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jmb.moviej2.ui.main.MainViewModel.UiModel
import com.jmb.testshared.mockedMovie
import com.jmb.usecases.GetPopularMovies
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getPopularMovies: GetPopularMovies

    @Mock
    lateinit var observer: Observer<UiModel>

    private lateinit var vm: MainViewModel

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        vm = MainViewModel(getPopularMovies)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `observing LiveData launches location permission request`() {

        vm.model.observeForever(observer)

        verify(observer).onChanged(UiModel.RequestLocationPermission)
    }

    @Test
    fun `after requesting the permission, loading is shown`() = runBlockingTest {

        val movies = listOf(mockedMovie.copy(id = 1))
        whenever(getPopularMovies.invoke()).thenReturn(movies)
        vm.model.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(UiModel.Loading)
    }

    @Test
    fun `after requesting the permission, getPopularMovies is called`() = runBlockingTest {

        val movies = listOf(mockedMovie.copy(id = 1))
        whenever(getPopularMovies.invoke()).thenReturn(movies)

        vm.model.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(UiModel.Content(movies))
    }
}