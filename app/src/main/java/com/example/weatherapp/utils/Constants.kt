package com.example.weatherapp.utils

import androidx.compose.ui.res.stringResource
import com.example.weatherapp.BottomNavItem
import com.example.weatherapp.R

object Constants{
    val  BottomNavItems=listOf<BottomNavItem>(
        BottomNavItem(
            name = R.string.home,
            icon = R.drawable.weatherpage,
            route = Route.HomeScreen
        ),
        BottomNavItem(
            name = R.string.setting,
            icon = R.drawable.weathersettings,
            route = Route.SettingsScreen
        ),
        BottomNavItem(
            name = R.string.alert,
            icon = R.drawable.alert,
            route = Route.AlertScreen
        ),
        BottomNavItem(
            name = R.string.dicover,
            icon = R.drawable.earth,
            route = Route.DiscoverScreen
        )


    )
    val ApiKey="a71745f051e01ebdb134fb48009e6027"
}