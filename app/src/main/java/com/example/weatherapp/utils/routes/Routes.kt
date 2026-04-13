package com.example.weatherapp.utils.routes

import kotlinx.serialization.Serializable

sealed class   Route {
    @Serializable
    object HomeScreen : Route()

    @Serializable
    object SettingsScreen : Route()

    @Serializable
    object AlertScreen : Route()

    @Serializable
    object DiscoverScreen : Route()

    @Serializable
    data class FullUi(val mode: String) : Route()

    @Serializable
    data class DetailScreen(val long: Double, val lat: Double)
    @Serializable
    object SplashScreen : Route()
 @Serializable
    object OnBoardScreen:Route(){

 }
}