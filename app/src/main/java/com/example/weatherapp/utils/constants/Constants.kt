package com.example.weatherapp.utils.constants

import com.example.weatherapp.BottomNaveItem
import com.example.weatherapp.R
import com.example.weatherapp.utils.routes.Route

object Constants{
    val  BottomNavItems=listOf<BottomNaveItem>(
        BottomNaveItem(
            name = R.string.home,
            icon = R.drawable.weatherpage,
            route = Route.HomeScreen
        ),
        BottomNaveItem(
            name = R.string.dicover,
            icon = R.drawable.earth,
            route = Route.DiscoverScreen
        ),
        BottomNaveItem(
            name = R.string.alert,
            icon = R.drawable.alert,
            route = Route.AlertScreen
        ),
        BottomNaveItem(
            name = R.string.setting,
            icon = R.drawable.weathersettings,
            route = Route.SettingsScreen
        ),
    )

}