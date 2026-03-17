package com.example.weatherapp.screens.discover.view.components.dataShower

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.entity.FavCity

@Composable
fun CountryItem(modifier: Modifier = Modifier, city: FavCity, onClickItem:(FavCity)->Unit, onClick: (FavCity) -> Unit, unit: String) {
    Card(
        colors = CardDefaults.cardColors(
            colorResource(R.color.cardColour)
        ),
        onClick = {

            onClickItem(city)
        },
        border = BorderStroke(
            color = colorResource(R.color.storkeColour),
            width = 1.dp,
        ),
        modifier = Modifier
            .padding(16.dp)
            .width(358.dp)
            .height(93.dp)

    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(2f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    city.name + "," + city.countryCode,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.whiteBlue),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    city.tempDescription,
                    style = MaterialTheme.typography.titleSmall,
                    color = colorResource(R.color.greyBlue),


                )
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text(
                    text = city.temp.toInt().toString()+ when(unit) {
                        "metric" -> "°C"
                        "imperial" -> "°F"
                        else -> "K"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.whiteBlue)
                )
                Spacer(Modifier.size(8.dp))
                IconButton(
                    onClick = {
                        onClick(city)

                    }, Modifier
                        .width(13.5.dp)
                        .height(15.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_delete_24),
                        tint = colorResource(R.color.greyBlue),
                        contentDescription = ""
                    )
                }
            }
        }


    }

}