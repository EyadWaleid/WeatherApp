package com.example.weatherapp

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.screens.viewmodel.WeatherFactory
import com.example.weatherapp.screens.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.Constants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val weahtherViewModel: WeatherViewModel= viewModel(factory = WeatherFactory(context = this.application))

            WeatherAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController = navController) }) { innerPadding ->
                    Navigation(navController, Modifier.padding(innerPadding),weahtherViewModel)

                }
            }
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
        Greeting("Android")
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(

        // set background color
        containerColor = colorResource(R.color.darkBlue)
    ) {

        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // observe current route to change the icon
        // color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route

        // Bottom nav items we declared
        Constants.BottomNavItems.forEach { navItem ->

            // Place the bottom nav items
            NavigationBarItem(

                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.route::class.qualifiedName,

                // navigate on click
                onClick = {
                    if (currentRoute != navItem.route::class.qualifiedName){
                        navController.navigate(navItem.route){
                            launchSingleTop = true

                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }

                            /*         // Restore previous state
                                     restoreState = true*/

                        }
                    }
                },

                // Icon of navItem
                icon = {
                    Icon(painter = painterResource(navItem.icon), contentDescription = stringResource( navItem.name), modifier = Modifier.size(20.dp))
                },

                // label
                label = {
                    Text(text = stringResource( navItem.name))
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.blue), // Icon color when selected
                    unselectedIconColor =colorResource(R.color.greyBlue) , // Icon color when not selected
                    selectedTextColor = Color.White, // Label color when selected
                    indicatorColor=Color.Transparent,
                )
            )
        }
    }
}