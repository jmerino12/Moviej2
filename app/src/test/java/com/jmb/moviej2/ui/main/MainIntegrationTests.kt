package com.jmb.moviej2.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jmb.data.source.LocalDataSource
import com.jmb.moviej2.ui.FakeLocalDataSource
import com.jmb.moviej2.ui.defaultFakeMovies
import com.jmb.moviej2.ui.initMockedDi
import com.jmb.testshared.mockedMovie
import com.jmb.usecases.GetPopularMovies
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainIntegrationTests : KoinTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<MainViewModel.UiModel>

    private lateinit var vm: MainViewModel

    private val dispatcher = TestCoroutineDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        val vmModule = module {
            factory { MainViewModel(get()) }
            factory { GetPopularMovies(get()) }
        }

        initMockedDi(vmModule)
        vm = get()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `data is loaded from server when local source is empty`() {
        vm.model.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(MainViewModel.UiModel.Content(defaultFakeMovies))
    }

    @Test
    fun `data is loaded from local source when available`() {
        val fakeLocalMovies = listOf(mockedMovie.copy(10), mockedMovie.copy(11))
        val localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        localDataSource.movies = fakeLocalMovies
        vm.model.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(MainViewModel.UiModel.Content(fakeLocalMovies))
    }
}