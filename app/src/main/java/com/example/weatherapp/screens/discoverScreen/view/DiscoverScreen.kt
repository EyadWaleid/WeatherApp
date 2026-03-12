package com.example.weatherapp.screens.discoverScreen.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.screens.discoverScreen.view.shimmer.LoadingDiscover
import com.example.weatherapp.screens.discoverScreen.viewmodel.DiscoverViewModel
import com.example.weatherapp.utils.ThereIsNoData

@Composable
fun DiscoverScreen(modifier: Modifier = Modifier, discoverViewModel: DiscoverViewModel) {
    val discoverState by discoverViewModel.favState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.darkBlue))
    ) {
        DiscoverAppBar()
        when (discoverState) {
            is DiscoverViewModel.FavState.Loading -> {
                LoadingDiscover()
            }
            is DiscoverViewModel.FavState.Empty -> {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ThereIsNoData()
                    Spacer(Modifier.size(10.dp))
                    Text(
                        text = stringResource(R.string.noData),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        color = colorResource(R.color.greyBlue)
                    )
                }
            }

            is DiscoverViewModel.FavState.Data -> {
                val data = (discoverState as DiscoverViewModel.FavState.Data).cities
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.darkBlue)),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    item {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Saved Cities",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = colorResource(R.color.greyBlue),

                                )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                "${data.size} Locations",
                                color = colorResource(R.color.blue),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                    items(data.size) {
                        CountryItem(city = data[it], onClick = { favCity ->
                            discoverViewModel.deleteFavCity(favCity = favCity)
                        })


                    }
                }
            }

            is DiscoverViewModel.FavState.Error -> {

                Text("There is an error please try agian later")
            }


        }
    }

}
@Composable
fun CountryItem(modifier: Modifier = Modifier, city: FavCity, onClick: (FavCity) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            colorResource(R.color.cardColour)
        ),
        onClick = {},
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
                    color = colorResource(R.color.whiteBlue)
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    city.tempDescription,
                    style = MaterialTheme.typography.titleSmall,
                    color = colorResource(R.color.greyBlue)
                )
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text(
                    text = city.temp.toInt().toString(),
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
@Composable
fun DiscoverAppBar(modifier: Modifier = Modifier) {
    val lineColor = colorResource(R.color.blueWithOpcity)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = lineColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.dicover),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.white)
        )
    }
}

