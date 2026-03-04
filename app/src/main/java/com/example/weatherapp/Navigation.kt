package com.example.weatherapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.weatherapp.screens.AlertScreen
import com.example.weatherapp.screens.DiscoverScreen
import com.example.weatherapp.screens.HomeScreen
import com.example.weatherapp.screens.SettingScreen
import com.example.weatherapp.utils.Route
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.weatherapp.screens.viewmodel.WeatherViewModel

@SuppressLint("RestrictedApi")
@Composable
fun Navigation(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel
) {
    Log.d("Screen","The backStack size :- ${navHostController.currentBackStack.collectAsState().value.size}")

    NavHost(
        startDestination = Route.HomeScreen,
        navController = navHostController,

        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        }
    ) {

        composable<Route.HomeScreen> { HomeScreen(modifier = modifier,weatherViewModel) }
        composable<Route.SettingsScreen> { SettingScreen(modifier=modifier) }
        composable<Route.AlertScreen> { AlertScreen(modifier=modifier) }
        composable<Route.DiscoverScreen> { DiscoverScreen(modifier = modifier) }
    }
}