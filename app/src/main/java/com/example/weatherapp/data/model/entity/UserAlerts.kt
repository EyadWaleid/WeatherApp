package com.example.weatherapp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_alarm")
data class UserAlerts(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0,
    val from: String,
    val to: String,
    var isOn: Boolean,
    val name: String,
    val type: String
)