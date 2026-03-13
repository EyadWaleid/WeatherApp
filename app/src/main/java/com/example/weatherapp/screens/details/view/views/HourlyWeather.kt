package com.example.weatherapp.screens.details.view.views

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.HourlyWeather
import com.example.weatherapp.utils.WeatherIcon

@Composable
fun HourlyWeather(modifier: Modifier= Modifier,hourlyWeather: List<HourlyWeather> ,tempUnits: String){
    LazyRow (modifier = Modifier.height(150.dp)) {
        items(count = hourlyWeather.size){
            if(it==0){
                HourlyWeatherItem(Modifier, icon = hourlyWeather.get(it).icon, textColor = colorResource(R.color.white) ,weatherValue = hourlyWeather.get(it).temp.toString()+tempUnits, color = colorResource(R.color.blue), iconColor = colorResource(R.color.white))
            }
            else{
                HourlyWeatherItem(Modifier, icon = hourlyWeather.get(it).icon, hour = hourlyWeather.get(it).time ,weatherValue = hourlyWeather.get(it).temp.toString()+tempUnits)
            }
        }
    }
}
@Composable
fun HourlyWeatherItem(modifier: Modifier= Modifier, icon: String, hour: String=stringResource(R.string.now), weatherValue: String, color: Color=Color(0xFFF1F5F9).copy(0.1f), textColor:Color=colorResource(R.color.greyBlue), iconColor:Color=colorResource(R.color.greyBlue)){
    Card (
        modifier = Modifier
            .padding(16.dp)
            .width(70.dp)
            .height(126.dp),
        border=BorderStroke(1.dp ,Color(0xFF000000).copy(0.1f)),
        colors=CardDefaults.cardColors(
            color
        ),
        shape = RoundedCornerShape(999.dp),
        elevation = CardDefaults.cardElevation(0.dp)) {
        Column (Modifier
            .padding(vertical = 16.dp, horizontal = 12.dp)
            .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(hour, style = MaterialTheme.typography.labelMedium, color = textColor, modifier = modifier.weight(1f))
            WeatherIcon(icon = icon, width = 30.dp,height=30.dp)
            Spacer(modifier = modifier.height(8.dp))
            Text(weatherValue, style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.white),modifier = modifier.weight(1f))
        }
    }
}
