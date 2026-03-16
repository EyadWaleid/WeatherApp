package com.example.weatherapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_country")
data class FavCity(
    val name: String,
    val countryCode: String,
    @PrimaryKey
    val long: Double,
    val lat: Double,
    val temp: Double,
    val tempDescription: String,
    val createdAt: Long = System.currentTimeMillis()
)