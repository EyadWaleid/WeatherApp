package com.example.weatherapp.screens.map.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import org.maplibre.spatialk.geojson.Position


@Composable
fun PlaceName(modifier: Modifier = Modifier, mypostion: Position, onClick: (Position) -> Unit, place: String){
    Box (modifier.fillMaxWidth().clip(shape = RoundedCornerShape(15.dp)).background(color = colorResource(
        R.color.darkBlueWithOpacity))) {
        Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Your Country choice is", style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.whiteBlue))

            Text(place)
            Button(onClick = {onClick(mypostion)}) {
                Text("Add", style = MaterialTheme.typography.bodySmall, color = colorResource(R.color.whiteBlue))
            }
        }

    }

}