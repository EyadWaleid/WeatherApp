package com.example.weatherapp.utils.constants

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.weatherapp.R

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
    isLarge: Boolean = false
) {

    GlideImage(
        model = showImage(isLarge = isLarge, icon = icon),
        contentDescription = "weather icon",
        modifier = Modifier.size(width =width ,height=height),
        requestBuilderTransform = { requestBuilder ->
            requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        }

    )
}

fun showImage(isLarge: Boolean,icon: String ) :Int{
 if(icon=="01d"){
    if(isLarge ){
        return  R.drawable.sunlarge
    }
     else{
         return  R.drawable.sun
     }
 }
    else if (icon =="01n"){
     if(isLarge ){
         return  R.drawable.moonlarge
     }
     else{
         return  R.drawable.moon
     }
    }
    else if (icon=="02d"){
     if(isLarge ){
         return  R.drawable.cloudsunlarge
     }
     else{
         return  R.drawable.coludsun
     }
 }
 else if (icon=="02n"){
     if(isLarge ){
         return  R.drawable.cloudnightlarge
     }
     else{
         return  R.drawable.nightcloud
     }
 }
 else if (icon=="03d" || icon=="03n"||icon=="04d" || icon=="04n"||icon=="50d" || icon=="50n"){
     if(isLarge ){
         return  R.drawable.cloudylarge
     }
     else{
         return  R.drawable.cloudy
     }
 }
 else if (icon=="10d" || icon=="10n"||icon=="09d" || icon=="09n"){
     if(isLarge ){
         return  R.drawable.rainylarge
     }
     else{
         return  R.drawable.rainy
     }
 }
 else if (icon=="11d" || icon=="11n"){
     if(isLarge ){
         return  R.drawable.bigthunder
     }
     else{
         return  R.drawable.lightning
     }
 }
 else if (icon=="13d" || icon=="13n"){
     if(isLarge ){
         return  R.drawable.snowlarge
     }
     else{
         return  R.drawable.snow
     }
 }

return R.drawable.sun

}