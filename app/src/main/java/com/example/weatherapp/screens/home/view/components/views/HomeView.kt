package com.example.weatherapp.screens.home.view.components.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.weatherapp.R
import com.example.weatherapp.screens.home.view_model.HomeViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.constants.WeatherIconLarge

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowWeather(
    modifier: Modifier = Modifier,
    weatherData: HomeViewModel.WeatherState.WeatherData
) {
    val weatherCountry = weatherData.weather
    val hourlyWeather = weatherData.hourlyWeather
    val dailyWeather = weatherData.dailyWeatherData
    val city = weatherData.city
    val tempUnits =
        if (weatherData.tempUnit == "metric" || weatherData.tempUnit == "imperial") "°" else "K"
    val windUnit = if (weatherData.windUnit == "m/s") "m/s" else "mph"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "${city}, ${weatherData.country}",
            color = colorResource(R.color.white),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            stringResource(R.string.your_location),
            color = colorResource(R.color.greyBlue),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(Modifier.size(30.dp))
        WeatherIconLarge(weatherCountry.icon, width = 120.dp, height = 120.dp, isLarge = true)
        Text(
            "${weatherCountry.temp}$tempUnits",
            style = MaterialTheme.typography.displayLarge,
            color = colorResource(R.color.white)
        )
        Spacer(Modifier.size(4.dp))
        Text(
            weatherCountry.tempDescription,
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(R.color.white)
        )
        Spacer(Modifier.size(15.dp))

        WeatherAttrBanner(
            modifier,
            windAttrValue = weatherData.weather.wind.toString(),
            humditiyAttrValue = weatherData.weather.humidity.toString(),
            pressureAttrValue = weatherData.weather.pressure.toInt().toString(),
            cloudAttrValue = weatherData.weather.cloudCover.toString(),
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            stringResource(R.string.hourly_forecast),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.blue),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.size(8.dp))

        HourlyWeather(hourlyWeather = hourlyWeather, tempUnits = tempUnits)

        Spacer(modifier = Modifier.size(16.dp))
        DaysForecst(
            dailyWeather = dailyWeather,
            modifier = Modifier.fillMaxWidth(),
            tempUnits = tempUnits
        )
    }
}

@Composable
fun WeatherAttrItem(
    modifier: Modifier = Modifier,
    icon: Int,
    weatherAttr: Int,
    value: String,
    subvalue: String
) {
    Card(
        modifier = Modifier
            .width(163.dp)
            .height(113.dp),
        border = BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
        colors = CardDefaults.cardColors(
            Color(0xFFF1F5F9).copy(0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "",
                    tint = colorResource(R.color.blue)
                )
                Spacer(Modifier.size(5.dp))
                Text(
                    stringResource(weatherAttr),
                    style = MaterialTheme.typography.labelMedium,
                    color = colorResource(R.color.blue)
                )

            }
            Spacer(Modifier.size(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                color = colorResource(R.color.white)
            )
            Text(
                subvalue,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                color = colorResource(R.color.whiteBlue)
            )

        }


    }
}

@Composable
fun WeatherAttrBanner(modifier: Modifier = Modifier,windAttrValue: String ,humditiyAttrValue: String ,pressureAttrValue:String,cloudAttrValue: String ) {
    Card(

        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.whiteBlue).copy(alpha = 0.05f)

        ),
        modifier = Modifier
            .height(151.dp)
            .fillMaxWidth().shadow(
                elevation = 50.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0xFF000000).copy(alpha = 0.25f),
                spotColor = Color(0xFF000000).copy(alpha = 0.25f)
            ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colorResource(R.color.whiteBlue).copy(0.1f)),
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp) ,
            horizontalArrangement = Arrangement.SpaceBetween) {
            ItemAttr(modifier,R.drawable.humditiy_icon,R.string.HUMIDITY,humditiyAttrValue+"%")
            ItemAttr(modifier,R.drawable.wind,R.string.WIND,windAttrValue)
            ItemAttr(modifier,R.drawable.pressure,R.string.PRESSURE,pressureAttrValue)
            ItemAttr(modifier,R.drawable.weatherpage,R.string.CLOUDCOVER,cloudAttrValue)
        }
    }

}

@Composable
fun ItemAttr(modifier: Modifier= Modifier, image: Int, weatherAttr: Int, weatherAttrValue: String){
    Column(
        Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = colorResource(R.color.blue).copy(0.2f)),
            contentAlignment = Alignment.Center

        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(image),
                contentDescription = "",
                tint = colorResource(R.color.blue)

            )

        }
        Text(
            stringResource(weatherAttr),
            style = MaterialTheme.typography.titleSmall,
            color = colorResource(R.color.greyBlueLight)
        )
        Text(
            weatherAttrValue,
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.whiteBlue)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF031734)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().background(color = Color(0xFF031734))
        ) {

        }
    }
}

