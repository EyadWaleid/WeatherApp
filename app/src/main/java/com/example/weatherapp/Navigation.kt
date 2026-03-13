package com.example.weatherapp

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.collection.longIntMapOf
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.weatherapp.screens.alert.AlertScreen
import com.example.weatherapp.screens.discover.view.DiscoverScreen
import com.example.weatherapp.screens.home.view.HomeScreen
import com.example.weatherapp.screens.settings.view.SettingScreen
import com.example.weatherapp.utils.Route
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.example.weatherapp.screens.details.view.views.DetialScreen
import com.example.weatherapp.screens.details.viewmodel.DetailViewModel
import com.example.weatherapp.screens.details.viewmodel.DetialFactoryViewModel
import com.example.weatherapp.screens.discover.viewmodel.DiscoverViewModel
import com.example.weatherapp.screens.home.view_model.WeatherViewModel
import com.example.weatherapp.screens.settings.viewmodel.SettingViewModel
import com.example.weatherapp.screens.map.view.FullUi
import com.example.weatherapp.screens.map.viewmodel.MapFactory
import com.example.weatherapp.screens.map.viewmodel.MapViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RestrictedApi")
@Composable
fun Navigation(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel,
    discoverViewModel: DiscoverViewModel,
    settingViewModel: SettingViewModel,
    context: Application,
    snackbarHostState: SnackbarHostState,

    ) {
    Log.d(
        "Screen",
        "The backStack size :- ${navHostController.currentBackStack.collectAsState().value.size}"
    )

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
        composable<Route.HomeScreen> { HomeScreen(modifier = modifier, weatherViewModel) }
        composable<Route.SettingsScreen> {
            SettingScreen(
                modifier = modifier,
                settingViewModel = settingViewModel,
                snackbarHostState = snackbarHostState,
                onMapClick = {
                    navHostController.navigate(Route.FullUi(mode = "settings"))
                }
            )
        }
        composable<Route.AlertScreen> { AlertScreen(modifier = modifier) }
        composable<Route.DiscoverScreen> {
            DiscoverScreen(
                modifier = modifier,
                discoverViewModel = discoverViewModel,
                onClickItem = {
                    navHostController.navigate(Route.DetailScreen(lat = it.lat, long = it.long))
                })
        }
        composable<Route.FullUi> { backStackEntry ->
            val mode = backStackEntry.toRoute<Route.FullUi>().mode
            val mapViewModel: MapViewModel = viewModel(
                factory = MapFactory(context)
            )
            FullUi(
                modifier = modifier,
                mode = mode,
                mapViewModel = mapViewModel,
                onClick = { position, address ->
                    when (mode) {
                        "fav" -> discoverViewModel.saveFavCity(position, address)
                        "settings" -> settingViewModel.setMapLocation(
                            lon = position.longitude,
                            lat = position.latitude
                        )
                    }
                    navHostController.popBackStack()
                }
            )
        }
        composable<Route.DetailScreen> { backStackEntry ->
            val lat = backStackEntry.toRoute<Route.DetailScreen>().lat
            val long = backStackEntry.toRoute<Route.DetailScreen>().long
            val detailViewModel: DetailViewModel = viewModel(
                factory = DetialFactoryViewModel(context = context, lat = lat, long = long)
            )
            DetialScreen(modifier = modifier, detailsViewModel = detailViewModel)
        }
    }
}

