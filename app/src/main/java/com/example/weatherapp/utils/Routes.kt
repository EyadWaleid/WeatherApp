package com.example.weatherapp.utils

import kotlinx.serialization.Serializable

sealed class   Route {
    @Serializable
    object  HomeScreen: Route()
    @Serializable
    object  SettingsScreen: Route()
    @Serializable
    object  AlertScreen: Route()
    @Serializable
    object  DiscoverScreen: Route()
}