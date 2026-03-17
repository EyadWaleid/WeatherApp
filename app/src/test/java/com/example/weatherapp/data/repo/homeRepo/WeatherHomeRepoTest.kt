package com.example.weatherapp.data.repo.homeRepo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.datasource.local.datasource.fav.IFavLocalDataSource
import com.example.weatherapp.data.datasource.local.datasource.weather.IWeatherLocalDatasource
import com.example.weatherapp.data.datasource.local.datasource.weather.WeatherLocalDatasource
import com.example.weatherapp.data.datasource.remote.IWeatherRemoteDataSource
import com.example.weatherapp.data.model.dto.ForecastData
import com.example.weatherapp.data.model.entity.CountryForecast
import com.example.weatherapp.utils.AppException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherHomeRepoTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    lateinit var homeRepo: WeatherHomeRepo
     lateinit var weatherDataSource: IWeatherRemoteDataSource
       lateinit var localDatasource: IWeatherLocalDatasource
     lateinit var favLocalDataSource: IFavLocalDataSource


    @Before
    fun start(){
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

         weatherDataSource = mockk()
         localDatasource = mockk()
         favLocalDataSource = mockk()

      homeRepo= WeatherHomeRepo(
          weatherDataSource = weatherDataSource,
         localDatasource= localDatasource,
         favLocalDataSource= favLocalDataSource)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
@Test
fun loadCountryWeatherData_networkSuccess_returnsForecastAndSavesToDB() = runTest {
        // given
        val fakeForecastData = mockk<ForecastData>(relaxed = true)

        coEvery {
            weatherDataSource.getWeatherCountryInfo(any(), any(), any(), any())
        } returns Result.success(fakeForecastData)

        coEvery {
            localDatasource.insertForecast(any())
        } just Runs

        // when
        val result = homeRepo.loadCountryWeatherData(
            lat = 30.0,
            lon = 31.0,
            units = "metric",
            lang = "en"
        )

        // then
        assert(result.isSuccess)
        coVerify { localDatasource.insertForecast(any()) }
    }

@Test
fun loadCountryWeatherData_unexpectedError_returnsAppException() = runTest {
    // given
    coEvery {
        weatherDataSource.getWeatherCountryInfo(any(), any(), any(), any())
    } returns Result.success(mockk(relaxed = true))
    coEvery {
        localDatasource.insertForecast(any())
    } throws NullPointerException("unexpected error")

    // when
    val result = homeRepo.loadCountryWeatherData(
        lat = 30.0,
        lon = 31.0,
        units = "metric",
        lang = "en"
    )

    // then
    assert(result.isFailure)
    assert(result.exceptionOrNull() is AppException) 
}

@Test
fun loadCountryWeatherData_networkError_cacheExists_returnsSuccessFromCache() = runTest {
    // given
    val fakeCachedForecast = mockk<CountryForecast>(relaxed = true)


    coEvery {
        weatherDataSource.getWeatherCountryInfo(any(), any(), any(), any())
    } throws java.io.IOException("No internet")


    coEvery {
        localDatasource.getForecast()
    } returns fakeCachedForecast

    // when
val result = homeRepo.loadCountryWeatherData(
    lat = 30.0,
    lon = 31.0,
    units = "metric",
    lang = "en")
    assert(result.isSuccess)
    coVerify(exactly = 0) { localDatasource.insertForecast(any()) }
}

}