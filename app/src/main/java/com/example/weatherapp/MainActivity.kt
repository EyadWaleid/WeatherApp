package com.example.weatherapp

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.datasource.local.datasource.fav.FavLocalDataSource
import com.example.weatherapp.data.datasource.local.datasource.usersettings.UserPreferences
import com.example.weatherapp.data.datasource.local.datasource.weather.WeatherLocalDatasource
import com.example.weatherapp.data.datasource.remote.WeatherDataSource
import com.example.weatherapp.data.repo.homeRepo.WeatherHomeRepo
import com.example.weatherapp.data.repo.settings.SettingsRepo
import com.example.weatherapp.screens.alert.viewmodel.AlertViewModel
import com.example.weatherapp.screens.alert.viewmodel.AlertViewModelFactory
import com.example.weatherapp.screens.discover.viewmodel.DiscoverFactoryModel
import com.example.weatherapp.screens.discover.viewmodel.DiscoverViewModel
import com.example.weatherapp.screens.settings.viewmodel.SettingViewModel
import com.example.weatherapp.screens.settings.viewmodel.SettingViewModelFactory
import com.example.weatherapp.screens.home.view_model.WeatherFactory
import com.example.weatherapp.screens.home.view_model.HomeViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.constants.Constants
import com.example.weatherapp.utils.routes.Route
import com.example.weatherapp.utils.connectivity.NetworkMonitor
import com.example.weatherapp.utils.geoCoder.GeocoderHelper
import com.example.weatherapp.utils.localization.AppLocalization
import com.example.weatherapp.utils.localization.IAppLocalization
import com.example.weatherapp.utils.location.LocationHelper
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            val app = application as WorkerApplication
            val localizationManager: IAppLocalization = AppLocalization(this)
            val discoverViewModel: DiscoverViewModel =
                viewModel(factory = DiscoverFactoryModel(
                    userSettings = app.settingsRepo,
                    repo =app.homeRepo,
                    networkMonitor = app.networkMonitor,
                ))
            val weahtherViewModel: HomeViewModel =
                viewModel(
                    factory = WeatherFactory(
                        networkMonitor = app.networkMonitor ,
                        repo = app. homeRepo ,
                        userSettingsRepo = app.settingsRepo,
                        locationProvider = app.locationHelper,
                    )
                )
            val settingViewModel: SettingViewModel = viewModel(
                factory = SettingViewModelFactory(
                    settingsRepo = app.settingsRepo,
                    networkMonitor = app.networkMonitor,
                    appLocalization = localizationManager
                )
            )
            val snackbarHostState = remember { SnackbarHostState() }
            val alertViewModel: AlertViewModel =
                viewModel(factory = AlertViewModelFactory(context = this.application))
            WeatherAppTheme {
                val navController = rememberNavController()
                val currentRoute by navController.currentBackStackEntryAsState()
                val currentDestination = currentRoute?.destination?.route
                val scope = rememberCoroutineScope()
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    floatingActionButton = {
                        if (currentDestination?.contains("DiscoverScreen") == true) {
                            FloatingActionButton(
                                containerColor = colorResource(R.color.blue),
                                onClick = {
                                    if (!NetworkMonitor(this).isInternetAvailable()) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Check your connectivity",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                        return@FloatingActionButton
                                    }

                                    navController.navigate(Route.FullUi("fav"))

                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.outline_map_24),
                                    "Floating action button"
                                )
                            }
                        }

                    },
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentDestination?.contains("FullUi") == false && currentDestination?.contains(
                                "DetailScreen"
                            ) == false
                        ) {
                            BottomNavigationBar(navController = navController)

                        }
                    }) { innerPadding ->
                    Navigation(
                        navController,
                        Modifier.padding(innerPadding),
                        weahtherViewModel,
                        settingViewModel = settingViewModel,
                        snackbarHostState = snackbarHostState,
                        discoverViewModel = discoverViewModel,
                        alertViewModel = alertViewModel,
                        context = this.application,
                        settingsRepo = app.settingsRepo,
                        geocoder = app.geocoderHelper,
                        weatherRepo = app.homeRepo,
                        locationHelper = app.locationHelper
                    )


                }

            }
        }
    }
    override fun attachBaseContext(base: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            super.attachBaseContext(base)
        } else {
            val prefs = base.getSharedPreferences("language_prefs", MODE_PRIVATE)
            val languageCode = prefs.getString("language", "en") ?: "en"
            val locale = Locale.forLanguageTag(languageCode)
            Locale.setDefault(locale)
            val config = Configuration(base.resources.configuration)
            config.setLocale(locale)
            super.attachBaseContext(base.createConfigurationContext(config))
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(

        containerColor = colorResource(R.color.darkBlue)
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Constants.BottomNavItems.forEach { navItem ->

            NavigationBarItem(

                selected = currentRoute == navItem.route::class.qualifiedName,

                onClick = {
                    if (currentRoute != navItem.route::class.qualifiedName) {
                        navController.navigate(navItem.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }


                        }
                    }
                },

                icon = {
                    Icon(
                        painter = painterResource(navItem.icon),
                        contentDescription = stringResource(navItem.name),
                        modifier = Modifier.size(20.dp)
                    )
                },

                label = {
                    Text(text = stringResource(navItem.name))
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.blue), // Icon color when selected
                    unselectedIconColor = colorResource(R.color.greyBlue), // Icon color when not selected
                    selectedTextColor = Color.White, // Label color when selected
                    indicatorColor = Color.Transparent,
                )
            )
        }
    }
}