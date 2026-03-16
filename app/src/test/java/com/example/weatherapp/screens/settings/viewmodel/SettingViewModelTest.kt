package com.example.weatherapp.screens.settings.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.repo.settings.ISettingsRepo
import com.example.weatherapp.utils.connectivity.NetworkMonitor
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.constants.TempUnits
import com.example.weatherapp.utils.constants.Units
import com.example.weatherapp.utils.localization.AppLocalization
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
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

class SettingViewModelTest {

    @get:Rule
    val  instantExecutorRule = InstantTaskExecutorRule()
    lateinit var repo: ISettingsRepo
    lateinit var localization: AppLocalization
    lateinit var networkMonitor: NetworkMonitor
    lateinit var settingViewModel: SettingViewModel
    @Before
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        repo= mockk()
        localization=mockk()
        networkMonitor=mockk()
        localization=mockk()
        every { repo.getTempUnit() } returns flowOf(TempUnits.CELSIUS.displayName)
        every { repo.getWindUnit() } returns flowOf(Units.METERS_PER_SECOND.displayName)
        every { repo.getLanguage() } returns flowOf("en")
        every { repo.getLocationSource() } returns flowOf(LocationSource.GPS.displayName)

        settingViewModel=SettingViewModel(networkMonitor = networkMonitor, appLocalization = localization, settingsRepo = repo)

     }
    @After
    fun tearDown(){
        Dispatchers.resetMain()

    }



    @Test
    fun setTempUnit_provideValueWithNetworkOff_tempUnitSubmitted() = runTest {
        // given
        every { networkMonitor.isInternetAvailable() } returns false
        coEvery { repo.setTempUnit(any()) } just Runs

        // when
        settingViewModel.setTempUnit(TempUnits.FAHRENHEIT.displayName)
        advanceUntilIdle()

        // then
        assert(settingViewModel.snackbarEvent.value=="No internet connection")
    }

    @Test
    fun setTempUnit_provideValueWithNetworkOn_tempUnitSubmitted() = runTest {
        // given
        every { networkMonitor.isInternetAvailable() } returns true
        coEvery { repo.setTempUnit(any()) } just Runs

        // when
        settingViewModel.setTempUnit(TempUnits.FAHRENHEIT.displayName)
        advanceUntilIdle()

        // then
        coVerify { repo.setTempUnit(TempUnits.FAHRENHEIT.displayName) }
    }

    @Test
    fun setLanguage_makeTheLanguageEnglishWithNetworkOn_LanguageIsSubmitted() = runTest {
        // given
        every { networkMonitor.isInternetAvailable() } returns true
        coEvery { repo.setLanguage(any()) } just Runs
        every { localization.changeLanguage(any()) } just Runs
        // when
        settingViewModel.setLanguage("en")
        advanceUntilIdle()
        // then
        coVerify { repo.setLanguage("en") }
        advanceUntilIdle()
        verify { localization.changeLanguage("en") }

    }



}