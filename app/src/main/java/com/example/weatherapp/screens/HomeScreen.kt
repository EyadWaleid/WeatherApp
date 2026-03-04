package com.example.weatherapp.screens

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.WeatherAppTheme
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Log.d("Screen", "Creating new Home Screen XD Hiiiiiiii")
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
/*
                    brush = Brush.linearGradient(
                        // Right to left gradient
                        colorStops = arrayOf(
                            0.0f to Color(0xFF137FEC).copy(alpha = 0.4f), // subtle blue glow center
                            0.2f to Color(0xFF0D1B2A),        // very dark blue left

                            0.3f to Color(0xFF102B44),        // darker mid-left
                            1.0f to Color(0xFF0D1B2A)         // dark blue right
                        ),
                        start = Offset(x =0f, y = 0f), // right
                        end = Offset(1000f, 1000f) // left
                    )
*/
                color = colorResource(R.color.darkBlue)
            ).verticalScroll(rememberScrollState()).padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text("San Francisco, CA", color = colorResource(R.color.white), style = MaterialTheme.typography.titleLarge)
        Text("Your Location", color = colorResource(R.color.greyBlue),style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.size(30.dp))
        Icon(
            painter = painterResource(R.drawable.weathersettings),
            tint = colorResource(R.color.white),
            contentDescription = "",
            modifier =   Modifier.size(56.dp),
        )
        Text("72", style = MaterialTheme.typography.headlineLarge, color = colorResource(R.color.white))
        Spacer(Modifier.size(15.dp))
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            WeatherAttrItem(icon=R.drawable.humditiy_icon, weatherAttr = "HUMIDITY", value = "60")
            Spacer(Modifier.size(16.dp))
            WeatherAttrItem(icon=R.drawable.wind, weatherAttr = "WIND", value = "60")
        }
        Spacer(Modifier.size(20.dp))
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            WeatherAttrItem(icon=R.drawable.pressure, weatherAttr = "PRESSURE", value = "60")
            Spacer(Modifier.size(16.dp))
            WeatherAttrItem(icon=R.drawable.weatherpage, weatherAttr = "CLOUD COVER", value = "60")
        }
        Spacer(modifier = Modifier.size(16.dp))
        Text("Hourly ForeCast", style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.blue),modifier= Modifier.align (Alignment.Start ))
        LazyRow (modifier = Modifier.height(150.dp)) {
            items(count = 5){
                HourlyWeather(Modifier, icon = R.drawable.weatherpage, hour = "4:00 pm" ,weatherValue = "50")
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        DaysForecst()

    }
}
@Composable
fun WeatherAttrItem(modifier: Modifier= Modifier,icon:Int,weatherAttr:String , value: String){
    Card (
        modifier = Modifier.width(140.dp).height(90.dp),
        border=BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
        colors=CardDefaults.cardColors(
            Color(0xFFF1F5F9).copy(0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)

    ) {
       Column(  modifier = Modifier.padding(16.dp).fillMaxSize(),) {
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
fun HourlyWeather(modifier: Modifier= Modifier,icon: Int,hour:String,weatherValue: String){
  Card (
      modifier = Modifier.padding(16.dp).width(70.dp).height(129.dp),
      border=BorderStroke(1.dp ,Color(0xFF000000).copy(0.1f)),
      colors=CardDefaults.cardColors(
          Color(0xFFF1F5F9).copy(0.1f)
      ),
      shape = RoundedCornerShape(999.dp),
      elevation = CardDefaults.cardElevation(0.dp)) {
      Column (Modifier.padding(vertical=16.dp, horizontal = 20.dp).fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally) {
          Text("2 pm", style = MaterialTheme.typography.labelMedium, color = colorResource(R.color.greyBlue))
          Spacer(Modifier.size(16.dp))
          Icon(painter = painterResource(R.drawable.weatherpage),contentDescription = "", tint = colorResource(R.color.greyBlue))
          Spacer(Modifier.size(16.dp))
          Text("50", style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.white))
      }
  }
}
@Composable
fun DaysForecst(modifier: Modifier= Modifier){
    Card (
        modifier = Modifier.padding(16.dp).width(342.dp).height(294.dp),
        border=BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
        colors=CardDefaults.cardColors(
            Color(0xFFF1F5F9).copy(0.1f)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ){
        Column (Modifier.padding(16.dp).fillMaxWidth()) {
            Text("5-Day Forecast", color = colorResource(R.color.white),style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.size(16.dp))
            Row (horizontalArrangement = Arrangement.Absolute.SpaceBetween , modifier = Modifier.fillMaxWidth()){
                Text("Tomorrow", color = colorResource(R.color.white),style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.size(5.dp))
                Text("Clear sky", Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium))
                Spacer(Modifier.size(5.dp))
                Row {
                    Text("72", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.white) )
                    Spacer(Modifier.size(16.dp))
                    Text("72",style = MaterialTheme.typography.bodyMedium, color =colorResource(R.color.greyBlue) )
                }

            }
            Spacer(Modifier.size(24.dp))
            Row (horizontalArrangement = Arrangement.Absolute.SpaceBetween , modifier = Modifier.fillMaxWidth()){
                Text("Tomorrow", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.size(5.dp))
                Text("Clear sky", Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium))
                Spacer(Modifier.size(5.dp))
                Row {
                    Text("72", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.white) )
                    Spacer(Modifier.size(16.dp))
                    Text("72",style = MaterialTheme.typography.bodyMedium, color =colorResource(R.color.greyBlue) )
                }

            }
            Spacer(Modifier.size(24.dp))
            Row (horizontalArrangement = Arrangement.Absolute.SpaceBetween , modifier = Modifier.fillMaxWidth()){
                Text("Tomorrow", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.size(5.dp))
                Text("Clear sky", Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium))
                Spacer(Modifier.size(5.dp))
                Row {
                    Text("72", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.white) )
                    Spacer(Modifier.size(16.dp))
                    Text("72",style = MaterialTheme.typography.bodyMedium, color =colorResource(R.color.greyBlue) )
                }

            }
            Spacer(Modifier.size(24.dp))
            Row (horizontalArrangement = Arrangement.Absolute.SpaceBetween , modifier = Modifier.fillMaxWidth()){
                Text("Tomorrow", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.size(5.dp))
                Text("Clear sky", Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium))
                Spacer(Modifier.size(5.dp))
                Row {
                    Text("72", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.white) )
                    Spacer(Modifier.size(16.dp))
                    Text("72",style = MaterialTheme.typography.bodyMedium, color =colorResource(R.color.greyBlue) )
                }

            }
            Spacer(Modifier.size(24.dp))
            Row (horizontalArrangement = Arrangement.Absolute.SpaceBetween , modifier = Modifier.fillMaxWidth()){
                Text("Tomorrow", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.size(5.dp))
                Text("Clear sky", Modifier.align(Alignment.CenterVertically), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium))
                Spacer(Modifier.size(5.dp))
                Row {
                    Text("72", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.white) )
                    Spacer(Modifier.size(16.dp))
                    Text("72",style = MaterialTheme.typography.bodyMedium, color =colorResource(R.color.greyBlue) )
                }

            }
        }

    }
}
@Preview(showBackground = true , showSystemUi = true)
@Composable
fun HomePreview() {
    WeatherAppTheme {
        Box (Modifier.fillMaxSize().background(color = colorResource(R.color.darkBlue)), contentAlignment = Alignment.Center){
            DaysForecst()
        }
    }
}