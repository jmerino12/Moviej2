package com.jmb.moviej2.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jmb.testshared.mockedMovie
import com.jmb.usecases.FindMovieById
import com.jmb.usecases.ToggleMovieFavorite
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
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
class DetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var findMovieById: FindMovieById

    @Mock
    lateinit var toggleMovieFavorite: ToggleMovieFavorite

    @Mock
    lateinit var observer: Observer<DetailViewModel.UiModel>

    private lateinit var vm: DetailViewModel

    private val dispatcher = TestCoroutineDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        vm = DetailViewModel(1, findMovieById, toggleMovieFavorite)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `observing LiveData finds the movie`() {

        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(findMovieById.invoke(1)).thenReturn(movie)

            vm.model.observeForever(observer)

            verify(observer).onChanged(DetailViewModel.UiModel(movie))
        }
    }

    @Test
    fun `when favorite clicked, the toggleMovieFavorite use case is invoked`() {
        runBlocking {
            val movie = mockedMovie.copy(id = 1)
            whenever(findMovieById.invoke(1)).thenReturn(movie)
            whenever(toggleMovieFavorite.invoke(movie)).thenReturn(movie.copy(favorite = !movie.favorite))
            vm.model.observeForever(observer)

            vm.onFavoriteClicked()

            verify(toggleMovieFavorite).invoke(movie)
        }
    }


}