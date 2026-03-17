package com.example.weatherapp.data.datasource.local.datasource.fav

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.example.weatherapp.data.datasource.local.dao.FavDao
import com.example.weatherapp.data.db.AppDatabase
import com.example.weatherapp.data.model.entity.FavCity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
@RunWith(JUnit4::class)
@MediumTest
class FavLocalDataSourceTest {
    lateinit var favLocalDataSource: FavLocalDataSource
    lateinit var favDao: FavDao
    lateinit var appDatabase: AppDatabase
    @Before
    fun setUp() {
        appDatabase=Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        favDao=appDatabase.favDao()
        favLocalDataSource= FavLocalDataSource(favDao)
    }

    @After
    fun tearDown() {
    appDatabase.close()
    }
    @Test
    fun insertData_cityInserted_returnsCityFromDB() = runTest {
        // given
        val city = FavCity(
             name = "Cairo", countryCode = "EG",
            lat = 30.0, long = 31.0, temp = 28.0,
            tempDescription = "Clear", createdAt = 1000L
        )

        // when
        favLocalDataSource.insertData(city)
        val result = favLocalDataSource.getAllCountryOnce()

        // then
        assert(result.contains(city))
    }

    @Test
    fun deleteData_cityDeleted_notInDB() = runTest {
        // given
        val city = FavCity(
            name = "Cairo", countryCode = "EG",
            lat = 30.0, long = 31.0, temp = 28.0,
            tempDescription = "Clear", createdAt = 1000L
        )

        // when
        favLocalDataSource.insertData(city)
        favLocalDataSource.deleteData(city)
        val result = favLocalDataSource.getAllCountryOnce()

        // then
        assert(result.isEmpty())
    }


    @Test
    fun getAllCountryOnce_emptyDB_returnsEmptyList() = runTest {
        // when
        val result = favLocalDataSource.getAllCountryOnce()

        // then
        assert(result.isEmpty())
    }

}