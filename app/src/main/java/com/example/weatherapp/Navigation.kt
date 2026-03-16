package com.example.weatherapp
import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.weatherapp.screens.alert.view.AlertScreen
import com.example.weatherapp.screens.discover.view.DiscoverScreen
import com.example.weatherapp.screens.home.view.HomeScreen
import com.example.weatherapp.screens.settings.view.SettingScreen
import com.example.weatherapp.utils.Route
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.toRoute
import com.example.weatherapp.screens.alert.viewmodel.AlertViewModel
import com.example.weatherapp.screens.details.view.views.DetialScreen
import com.example.weatherapp.screens.details.viewmodel.DetailViewModel
import com.example.weatherapp.screens.details.viewmodel.DetialFactoryViewModel
import com.example.weatherapp.screens.discover.viewmodel.DiscoverViewModel
import com.example.weatherapp.screens.home.view_model.HomeViewModel
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
    homeViewModel: HomeViewModel,
    discoverViewModel: DiscoverViewModel,
    settingViewModel: SettingViewModel,
    context: Application,
    snackbarHostState: SnackbarHostState,
    alertViewModel: AlertViewModel) {
    NavHost(
        startDestination = Route.HomeScreen,
        navController = navHostController,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable<Route.HomeScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
            }
        ) { HomeScreen(modifier = modifier, homeViewModel,snackbarHostState) }

        composable<Route.AlertScreen>(
            enterTransition = {
                when (initialState.destination.route) {
                    Route.HomeScreen::class.qualifiedName -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
                    Route.DiscoverScreen::class.qualifiedName -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
                }
            },
            exitTransition = {
                when (initialState.destination.route) {
                    Route.HomeScreen::class.qualifiedName -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
                    Route.DiscoverScreen::class.qualifiedName -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
                    else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
                }
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
            }
        ) { AlertScreen(modifier = modifier, alertViewModel = alertViewModel) }

        composable<Route.DiscoverScreen>(
            enterTransition = {
                when (initialState.destination.route) {
                    Route.HomeScreen::class.qualifiedName -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
                    Route.AlertScreen::class.qualifiedName -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
                    else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
                }
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
            }
        ) {
            DiscoverScreen(
                modifier = modifier,
                snackbarHostState = snackbarHostState,
                discoverViewModel = discoverViewModel,
                onClickItem = {
                    navHostController.navigate(Route.DetailScreen(lat = it.lat, long = it.long))
                }
            )
        }

        composable<Route.SettingsScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(250))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(250))
            }
        ) {
            SettingScreen(
                modifier = modifier,
                settingViewModel = settingViewModel,
                snackbarHostState = snackbarHostState,
                onMapClick = { navHostController.navigate(Route.FullUi(mode = "settings")) }
            )
        }

        composable<Route.FullUi> { backStackEntry ->
            val mode = backStackEntry.toRoute<Route.FullUi>().mode
            val mapViewModel: MapViewModel = viewModel(factory = MapFactory(context))
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

