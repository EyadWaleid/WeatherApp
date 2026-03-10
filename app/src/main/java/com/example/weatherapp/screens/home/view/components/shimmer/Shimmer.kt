package com.example.weatherapp.screens.home.view.components.shimmer

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
@Composable
fun ShowLoading(modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box  (modifier = Modifier.size(
            width = 100.dp,
            height = 10.dp
        ).shimmerEffect())
        Spacer(Modifier.size(3.dp))

        Box  (modifier = Modifier.size(
            width = 60.dp,
            height = 10.dp
        ).shimmerEffect())
        Spacer(Modifier.size(30.dp))

        Box  (modifier = Modifier.size(56.dp).shimmerEffect())
        Spacer(Modifier.size(8.dp))

        Box  (modifier = Modifier.shimmerEffect().size(
            width = 60.dp,
            height = 10.dp
        ))
        Spacer(Modifier.size(15.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            WeatherAttrItem()
            Spacer(Modifier.size(16.dp))
            WeatherAttrItem()
        }
        Spacer(Modifier.size(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            WeatherAttrItem()
            Spacer(Modifier.size(16.dp))
            WeatherAttrItem()
        }
        Spacer(modifier = Modifier.size(16.dp))
        HourlyWeather()
        Spacer(modifier = Modifier.size(16.dp))
        DaysForecst()
    }

}
@Composable
fun WeatherAttrItem(modifier: Modifier= Modifier){
    Card (
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .width(140.dp)
            .height(90.dp).clip(RoundedCornerShape(15.dp)).shimmerEffect(),
        elevation = CardDefaults.cardElevation(0.dp)

    ) {}
}
@Composable
fun HourlyWeather(modifier: Modifier= Modifier){

    LazyRow (modifier = Modifier.height(150.dp)) {
        items(count = 3){

            HourlyWeatherItem()

        }
    }
}
@Composable
fun HourlyWeatherItem(modifier: Modifier= Modifier){
    Card (
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),

        modifier = Modifier
            .padding(16.dp)
            .width(80.dp)
            .height(140.dp).clip(RoundedCornerShape(999.dp)).shimmerEffect(),

        elevation = CardDefaults.cardElevation(0.dp)) {

    }
}
@Composable
fun DaysForecst(modifier: Modifier= Modifier){
    Card (
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),

        modifier = modifier
            .padding(16.dp)
            .width(342.dp)
            .height(294.dp).clip(RoundedCornerShape(15.dp)).shimmerEffect(),
        elevation = CardDefaults.cardElevation(0.dp)
    ){


        }

}

fun Modifier.shimmerEffect(): Modifier=composed {
    var  size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue =  -2* size.width.toFloat(),
        targetValue = 2*size.width.toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(1000))
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                colorResource(R.color.greyBlue),
                colorResource(R.color.greyBlueLight),
                colorResource(R.color.greyBlue),
            ),
            start = Offset(startOffsetX,0f),
            end = Offset(startOffsetX+size.width.toFloat(),size.height.toFloat())
        )
    ).onGloballyPositioned{
        size=it.size
    }

}


