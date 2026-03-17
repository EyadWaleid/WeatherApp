package com.example.weatherapp.data.datasource.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.entity.FavCity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class FavDaoTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: AppDatabase
    private  lateinit var dao: FavDao
    @Before
    fun setUp(){
        database=Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao=database.favDao()

    }
    @After
    fun tearDown() {
        database.close()
    }
    @Test
    fun insertFavCity_cityInserted_returnsCityFromDB() = runTest {
        // given
        val city = FavCity(
            name = "Cairo",
            countryCode = "EG",
            lat = 30.0,
            long = 31.0,
            temp = 28.0,
            tempDescription = "Clear",
            createdAt = 1000L
        )

        // when
        dao.insertFavCity(city)
        val result = dao.getAllCitiesOnce()

        // then
        assert(result.contains(city))
    }

    @Test
    fun getAllCitiesOnce_multipleCities_returnedInDescOrder() = runTest {
        // given
        val city1 = FavCity( name = "Cairo", countryCode = "EG", lat = 30.0, long = 31.0, temp = 28.0, tempDescription = "Clear", createdAt = 1000L)
        val city2 = FavCity( name = "London", countryCode = "UK", lat = 51.0, long = 0.0, temp = 15.0, tempDescription = "Cloudy", createdAt = 2000L)

        // when
        dao.insertFavCity(city1)
        dao.insertFavCity(city2)
        val result = dao.getAllCitiesOnce()

        // then
        assert(result.first().name == "London")
        assert(result.last().name == "Cairo")
    }

    @Test
    fun insertFavCity_sameCityTwice_replacesNotDuplicates() = runTest {
        // given
        val city = FavCity( name = "Cairo", countryCode = "EG", lat = 30.0, long = 31.0, temp = 28.0, tempDescription = "Clear", createdAt = 1000L)
        val updatedCity = city.copy(temp = 35.0)

        // when
        dao.insertFavCity(city)
        dao.insertFavCity(updatedCity)
        val result = dao.getAllCitiesOnce()

        // then
        assert(result.size == 1)
        assert(result.first().temp == 35.0)
    }


}