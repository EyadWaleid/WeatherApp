package com.example.weatherapp.utils.constants

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun WeatherIcon(
    icon: String,
    width: Dp,
    height: Dp,
) {
    val iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"

    GlideImage(
        model = iconUrl,
        contentDescription = "weather icon",
        modifier = Modifier.size(width =width ,height=height),
        requestBuilderTransform = { requestBuilder ->
            requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        }

    )
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable

fun WeatherIconLarge(
    icon: String,
    width: Dp,
    height: Dp,
) {
    val iconUrl = "https://openweathermap.org/img/wn/${icon}@4x.png"

    GlideImage(
        model = iconUrl,
        contentDescription = "weather icon",
        modifier = Modifier.size(width =width ,height=height),
        requestBuilderTransform = { requestBuilder ->
            requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        }

    )
}