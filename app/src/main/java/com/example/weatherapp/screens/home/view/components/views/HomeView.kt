package com.example.weatherapp.screens.home.view.components.views
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.weatherapp.R
import com.example.weatherapp.screens.home.view_model.HomeViewModel
import com.example.weatherapp.utils.WeatherIconLarge

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowWeather(modifier: Modifier = Modifier, weatherData: HomeViewModel.WeatherState.WeatherData) {
    val weatherCountry = weatherData.weather
    val hourlyWeather = weatherData.hourlyWeather
    val dailyWeather = weatherData.dailyWeatherData
    val city = weatherData.city
    val tempUnits=if(weatherData.tempUnit=="metric" || weatherData.tempUnit=="imperial") "°" else "K"
    val windUnit=if (weatherData.windUnit=="m/s") "m/s" else "mph"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("${city}, ${weatherData.country}", color = colorResource(R.color.white), style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.your_location), color = colorResource(R.color.greyBlue), style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.size(30.dp))
        WeatherIconLarge(weatherCountry.icon, width = 110.dp, height = 100.dp)
        Text("${weatherCountry.temp}$tempUnits", style = MaterialTheme.typography.displayLarge, color = colorResource(R.color.white))
        Spacer(Modifier.size(4.dp))
        Text(weatherCountry.tempDescription, style = MaterialTheme.typography.bodyLarge, color = colorResource(R.color.white))
        Spacer(Modifier.size(15.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            WeatherAttrItem(icon = R.drawable.humditiy_icon, weatherAttr = R.string.HUMIDITY, value = weatherCountry.humidity.toString(), subvalue = weatherCountry.humidityCondition)
            WeatherAttrItem(icon = R.drawable.wind, weatherAttr = R.string.WIND, value = weatherCountry.wind.toInt().toString()+" "+windUnit, subvalue = weatherCountry.windDirction)
        }
        Spacer(Modifier.size(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            WeatherAttrItem(icon = R.drawable.pressure, weatherAttr = R.string.PRESSURE, value = weatherCountry.pressure.toString(), subvalue = weatherCountry.pressureCondition)
            WeatherAttrItem(icon = R.drawable.weatherpage, weatherAttr = R.string.CLOUDCOVER, value = weatherCountry.cloudCover.toString(), subvalue = " ")
        }

        Spacer(modifier = Modifier.size(16.dp))
        Text(stringResource(R.string.hourly_forecast), style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.blue), modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.size(8.dp))

        HourlyWeather(hourlyWeather = hourlyWeather, tempUnits = tempUnits)

        Spacer(modifier = Modifier.size(16.dp))
        DaysForecst(dailyWeather = dailyWeather, modifier = Modifier.fillMaxWidth(), tempUnits = tempUnits)
    }
}@Composable
fun WeatherAttrItem(modifier: Modifier= Modifier,icon:Int,weatherAttr:Int , value: String,subvalue:String){
    Card (
        modifier = Modifier
            .width(163.dp)
            .height(113.dp),
        border=BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
        colors=CardDefaults.cardColors(
            Color(0xFFF1F5F9).copy(0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(  modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween) {
            Row {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "",
                    tint = colorResource(R.color.blue)
                )
                Spacer(Modifier.size(5.dp))
                Text(stringResource(weatherAttr), style = MaterialTheme.typography.labelMedium, color = colorResource(R.color.blue))

            }
            Spacer(Modifier.size(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold), color = colorResource(R.color.white))
            Text(subvalue, style = MaterialTheme.typography.titleSmall.copy(fontWeight=FontWeight.Normal), color = colorResource(R.color.whiteBlue))

        }


    }
}
