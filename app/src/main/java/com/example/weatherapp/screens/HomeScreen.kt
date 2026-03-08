package com.example.weatherapp.screens

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.weatherapp.R
import com.example.weatherapp.data.model.DailyWeather
import com.example.weatherapp.data.model.HourlyWeather
import com.example.weatherapp.screens.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, weatherViewModel: WeatherViewModel){
    val  context=LocalContext.current
    val weatherState by weatherViewModel.locationFlow.collectAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        if (granted){
            weatherViewModel.refreshLocation()
        }
    }
    when(weatherState){
        is WeatherViewModel.WeatherState.PermissionDisabled-> {
            Log.d("Weather", "permission disabled")
            LaunchedEffect(Unit) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
        is WeatherViewModel.WeatherState.LocationDisabled ->{
            Log.d("Weather", "Location disabled")

            val  intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }
        is WeatherViewModel.WeatherState.WeatherData ->{
            ShowWeather(modifier, weatherState as WeatherViewModel.WeatherState.WeatherData)
        }

        WeatherViewModel.WeatherState.IsLoading -> {
            Log.d("Weather",  "Loading")

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Loading Products...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        is WeatherViewModel.WeatherState.OnError -> {
            val errorState = weatherState as WeatherViewModel.WeatherState.OnError
            Log.d("Weather", errorState.errorMessage)


        }

    }

}
@Composable
fun ShowWeather(modifier: Modifier = Modifier, weatherData: WeatherViewModel.WeatherState.WeatherData) {
    val weatherCountry = weatherData.weather
    val hourlyWeather = weatherData.hourlyWeather
    val dailyWeather = weatherData.dailyWeatherData
    val city = weatherData.city

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1C2F4E),
                            Color(0xFF112040),
                            Color(0xFF0A1628)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),

                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("${city.name}, ${city.country}", color = colorResource(R.color.white), style = MaterialTheme.typography.titleLarge)
            Text("Your Location", color = colorResource(R.color.greyBlue), style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.size(30.dp))
            Icon(
                painter = painterResource(R.drawable.weathersettings),
                tint = colorResource(R.color.white),
                contentDescription = "",
                modifier = Modifier.size(56.dp),
            )
            Text("${weatherCountry.main.temp}", style = MaterialTheme.typography.headlineLarge, color = colorResource(R.color.white))
            Spacer(Modifier.size(15.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                WeatherAttrItem(icon = R.drawable.humditiy_icon, weatherAttr = "HUMIDITY", value = weatherCountry.main.humidity.toString())
                Spacer(Modifier.size(16.dp))
                WeatherAttrItem(icon = R.drawable.wind, weatherAttr = "WIND", value = weatherCountry.wind.speed.toString())
            }
            Spacer(Modifier.size(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                WeatherAttrItem(icon = R.drawable.pressure, weatherAttr = "PRESSURE", value = weatherCountry.main.pressure.toString())
                Spacer(Modifier.size(16.dp))
                WeatherAttrItem(icon = R.drawable.weatherpage, weatherAttr = "CLOUD COVER", value = weatherCountry.clouds.all.toString())
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text("Hourly ForeCast", style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.blue), modifier = Modifier.align(Alignment.Start))
            HourlyWeather(hourlyWeather = hourlyWeather)
            Spacer(modifier = Modifier.size(16.dp))
            DaysForecst(dailyWeather = dailyWeather)
        }
    }
}
@Composable
fun WeatherAttrItem(modifier: Modifier= Modifier,icon:Int,weatherAttr:String , value: String){
    Card (
        modifier = Modifier
            .width(140.dp)
            .height(90.dp),
        border=BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
        colors=CardDefaults.cardColors(
            Color(0xFFF1F5F9).copy(0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)

    ) {
       Column(  modifier = Modifier
           .padding(16.dp)
           .fillMaxSize(),) {
           Row {
               Icon(
                   painter = painterResource(icon),
                   contentDescription = "",
                   tint = colorResource(R.color.blue)
               )
               Spacer(Modifier.size(5.dp))
               Text(weatherAttr, style = MaterialTheme.typography.labelMedium, color = colorResource(R.color.blue))

           }
           Spacer(Modifier.size(8.dp))
           Text(value, style = MaterialTheme.typography.headlineSmall, color = colorResource(R.color.white))

       }


    }
}
@Composable
fun HourlyWeather(modifier: Modifier= Modifier,hourlyWeather: List<HourlyWeather>){
    LazyRow (modifier = Modifier.height(150.dp)) {
        Log.d("Weather",hourlyWeather.size.toString())
        items(count = hourlyWeather.size){
            if(it==0){
                HourlyWeatherItem(Modifier, icon = R.drawable.weatherpage, hour = "Now", textColor = colorResource(R.color.white) ,weatherValue = hourlyWeather.get(it).temp.toString(), color = colorResource(R.color.blue), iconColor = colorResource(R.color.white))
            }
            else{
                HourlyWeatherItem(Modifier, icon = R.drawable.weatherpage, hour = hourlyWeather.get(it).time ,weatherValue = hourlyWeather.get(it).temp.toString())

            }
        }
    }
}
@Composable
fun HourlyWeatherItem(modifier: Modifier= Modifier,icon: Int,hour:String,weatherValue: String,color: Color=Color(0xFFF1F5F9).copy(0.1f),textColor:Color=colorResource(R.color.greyBlue),iconColor:Color=colorResource(R.color.greyBlue)){
  Card (
      modifier = Modifier
          .padding(16.dp)
          .width(80.dp)
          .height(140.dp),
      border=BorderStroke(1.dp ,Color(0xFF000000).copy(0.1f)),
      colors=CardDefaults.cardColors(
          color
      ),
      shape = RoundedCornerShape(999.dp),
      elevation = CardDefaults.cardElevation(0.dp)) {
      Column (Modifier
          .padding(vertical = 16.dp, horizontal = 20.dp)
          .fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally) {
          Text(hour, style = MaterialTheme.typography.labelMedium, color = textColor)
          Spacer(Modifier.size(16.dp))
          Icon(painter = painterResource(R.drawable.weatherpage),contentDescription = "", tint =iconColor )
          Spacer(Modifier.size(16.dp))
          Text(weatherValue, style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.white))
      }
  }
}
@Composable
fun DaysForecst(modifier: Modifier= Modifier,dailyWeather: List<DailyWeather>){
    Card (
        modifier = modifier
            .padding(16.dp)
            .width(342.dp)
            .height(294.dp),
        border=BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
        colors=CardDefaults.cardColors(
            Color(0xFFF1F5F9).copy(0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ){
        LazyColumn (modifier = modifier
            .fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center){
            item {
                Column {
                    Text("4-Day Forecast", color = colorResource(R.color.white),style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.size(25.dp))

                }
            }
            items(dailyWeather.size){
                if(it==0){
                    DayForecastItem(dailyWeather = dailyWeather.get(it), dayColor = colorResource(R.color.white))
                }
                else{
                    DayForecastItem(dailyWeather = dailyWeather.get(it))
                }
                Spacer(Modifier.size(24.dp))

            }

        }
    }
}

@Composable
fun DayForecastItem(
    modifier: Modifier = Modifier,
    dailyWeather: DailyWeather,
    dayColor: Color = colorResource(R.color.greyBlue),
    descriptionColor: Color = colorResource(R.color.greyBlue),
) {
    Row(
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(dailyWeather.date, color = dayColor, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.size(5.dp))
        Text(
            dailyWeather.condition,
            modifier = Modifier.align(Alignment.CenterVertically),
            color = descriptionColor,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
        Spacer(Modifier.size(5.dp))
        Row {
            Text("${dailyWeather.maxTemp}°", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.white))
            Spacer(Modifier.size(16.dp))
            Text("${dailyWeather.minTemp}°", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.greyBlue))
        }}}
@Preview(showBackground = true , showSystemUi = true)
@Composable
fun HomePerview() {
    WeatherAppTheme {
        Box (Modifier.fillMaxSize().background(color = colorResource(R.color.darkBlue)), contentAlignment = Alignment.Center){
            WeatherAttrItem(modifier = Modifier,R.drawable.wind,"Hi","60")
        }
    }
}