package com.example.weatherapp.screens.home.view.components.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.entity.DailyWeather

@Composable
fun DaysForecst(modifier: Modifier = Modifier, dailyWeather: List<DailyWeather>,tempUnits: String) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        border = BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
        colors = CardDefaults.cardColors(Color(0xFFF1F5F9).copy(0.1f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.forecast_days),
                color = colorResource(R.color.white),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.size(25.dp))
            dailyWeather.forEachIndexed { index, day ->
                DayForecastItem(
                    dailyWeather = day,
                    dayColor = if (index == 0) colorResource(R.color.white) else colorResource(R.color.greyBlue),
                    tempUnit=tempUnits
                )
                Spacer(Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun DayForecastItem(
    modifier: Modifier = Modifier,
    dailyWeather: DailyWeather,
    tempUnit: String,
    dayColor: Color = colorResource(R.color.greyBlue),

    descriptionColor: Color = colorResource(R.color.greyBlue),
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            dailyWeather.dayName,
            color = dayColor,
            modifier= modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.size(width = 10.dp, height = 0.dp))

        Text(
            dailyWeather.tempDescription,
            color = descriptionColor,
            modifier= modifier.weight(1f),

            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.size(width = 10.dp, height = 0.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier= modifier.weight(1f),

            ) {
            Text("${dailyWeather.maxTemp.toInt()}$tempUnit", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.white))
            Spacer(Modifier.size(8.dp))
            Text("${dailyWeather.minTemp.toInt()}$tempUnit", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.greyBlue))
            Spacer(Modifier.size(4.dp)) }
    }
}