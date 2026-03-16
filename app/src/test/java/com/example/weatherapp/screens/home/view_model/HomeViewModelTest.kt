package com.example.weatherapp.screens.home.view_model

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.model.entity.CountryForecast
import com.example.weatherapp.data.model.entity.DailyWeather
import com.example.weatherapp.data.repo.homeRepo.WeatherHomeRepo
import com.example.weatherapp.data.repo.settings.SettingsRepo
import com.example.weatherapp.utils.connectivity.INetworkMonitor
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.constants.TempUnits
import com.example.weatherapp.utils.constants.Units
import com.example.weatherapp.utils.location.ILocationHelper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    lateinit var repo: WeatherHomeRepo
    lateinit var locationHelper: ILocationHelper
    lateinit var networkMonitor: INetworkMonitor
    lateinit var homeViewModel: HomeViewModel

    lateinit var userSettingsRepo: SettingsRepo

    @Before
    fun startUp() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        repo = mockk()
        userSettingsRepo = mockk()
        locationHelper = mockk()
        networkMonitor = mockk()
        repo = mockk(relaxed = true)
        userSettingsRepo = mockk(relaxed = true)
        locationHelper = mockk(relaxed = true)
        networkMonitor = mockk(relaxed = true)
        every { userSettingsRepo.getLocationSource() } returns flowOf(LocationSource.MAP.displayName)
        every { userSettingsRepo.getMapLocation() } returns flowOf(Pair(0.0, 0.0))
        homeViewModel = HomeViewModel(
            repo = repo,
            networkMonitor = networkMonitor,
            userSettingsRepo = userSettingsRepo,
            locationProvider = locationHelper
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun refreshData_networkIsOff_snackBarNetworkIsConnectivity() = runTest {
        // given
        every { networkMonitor.isInternetAvailable() } returns false
        // when
        homeViewModel.refreshLocation()
        advanceUntilIdle()
        // then

        assert(homeViewModel.snackbarEvent.value == "Please check your connectivity")


    }

    @Test
    fun refreshData_networkIsOn_CountryForecastReceived() = runTest {
        // given
        every { userSettingsRepo.getLanguage() } returns flowOf("en")
        every { userSettingsRepo.getTempUnit() } returns flowOf(TempUnits.CELSIUS.displayName)
        every { userSettingsRepo.getWindUnit() } returns flowOf(Units.METERS_PER_SECOND.displayName)
        every { userSettingsRepo.getLocationSource() } returns flowOf(LocationSource.MAP.displayName)
        every { userSettingsRepo.getMapLocation() } returns flowOf(Pair(30.0, 31.0))

        val fakeDailyWeather = mockk<DailyWeather>(relaxed = true)
        every { fakeDailyWeather.hoursOfDayForecast } returns emptyList()
        val fakeResponse = mockk<CountryForecast>(relaxed = true)
        every { fakeResponse.weatherOfDays } returns listOf(fakeDailyWeather)

        coEvery {
            repo.loadCountryWeatherData(any(), any(), any(), any())
        } returns Result.success(fakeResponse)

        val freshViewModel = HomeViewModel(
            repo = repo,
            networkMonitor = networkMonitor,
            userSettingsRepo = userSettingsRepo,
            locationProvider = locationHelper
        )
        advanceUntilIdle()

        // when
        freshViewModel.refreshLocation()
        advanceUntilIdle()

        // then
        assert(freshViewModel.weatherState.value is HomeViewModel.WeatherState.WeatherData)
    }

    @Test
    fun observeSettings_gpsSource_permissionsDisabled_showsPermissionDisabled() = runTest {
        // given
        every { userSettingsRepo.getLanguage() } returns flowOf("en")
        every { userSettingsRepo.getTempUnit() } returns flowOf(TempUnits.CELSIUS.displayName)
        every { userSettingsRepo.getWindUnit() } returns flowOf(Units.METERS_PER_SECOND.displayName)
        every { userSettingsRepo.getLocationSource() } returns flowOf(LocationSource.GPS.displayName)
        every { locationHelper.checkPermissions() } returns false

        //when

        val freshViewModel = HomeViewModel(
            repo = repo,
            networkMonitor = networkMonitor,
            userSettingsRepo = userSettingsRepo,
            locationProvider = locationHelper
        )
        advanceUntilIdle()

        // then
        assert(freshViewModel.weatherState.value is HomeViewModel.WeatherState.PermissionDisabled)
    }
}