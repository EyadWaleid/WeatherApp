package com.example.weatherapp.screens.discover.view.components.shimmer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.screens.home.view.components.shimmer.shimmerEffect

@Composable
fun LoadingDiscover(modifier: Modifier= Modifier){
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.darkBlue)),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        items(3) {
          CountryItem()

        }
    }
}
@Composable
fun CountryItem(modifier: Modifier = Modifier) {
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
            .height(93.dp).clip(RoundedCornerShape(15.dp)).shimmerEffect()

    ) {

        }


    }

