package com.example.weatherapp.screens.splash

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weatherapp.R
import com.example.weatherapp.utils.constants.SplashLogo
import com.example.weatherapp.utils.routes.Route
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(modifier: Modifier = Modifier,navController: NavHostController) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true)
    {
        delay(3000)
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isFirstTime = prefs.getBoolean("is_first_time", true)

        if (isFirstTime) {
            prefs.edit().putBoolean("is_first_time", false).apply()
            navController.navigate(Route.OnBoardScreen) {
                popUpTo(Route.SplashScreen) { inclusive = true }
            }
        } else {
            navController.navigate(Route.HomeScreen) {
                popUpTo(Route.SplashScreen) { inclusive = true }
            }
        }

    }
    val darkBlue = colorResource(R.color.darkBlue)
    Box(modifier = Modifier.fillMaxSize()) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = darkBlue)

            val topRight = Offset(size.width, 0f)
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.5f to Color(0xFF137FEC).copy(0.2f),
                        1f to Color(0xFF101922),
                    ),
                    center = topRight,
                    radius = size.width * 0.6f
                ),
                radius = size.width * 0.6f,
                center = topRight
            )

            val bottomLeft = Offset(0f, size.height)
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.5f to Color(0xFF137FEC).copy(0.2f),
                        1f to Color(0xFF101922),
                    ),
                    center = bottomLeft,
                    radius = size.width * 0.6f
                ),
                radius = size.width * 0.6f,
                center = bottomLeft
            )

        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))
            SplashLogo()

            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Weather app",
                color = colorResource(R.color.greyBlue).copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 48.dp)
            )
        }
    }
}

