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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import org.maplibre.spatialk.geojson.Position


@Composable
fun PlaceName(modifier: Modifier = Modifier, mypostion: Position, onClick: (Position) -> Unit, place: String){
    Box (modifier
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(15.dp))
        .background(
            color = colorResource(
                R.color.darkBlueWithOpacity
            )
        )) {
        Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.your_country_choice_is), style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.whiteBlue))
            Text(place)
            Button(onClick = {onClick(mypostion)}) {
                Text(stringResource(R.string.add), style = MaterialTheme.typography.bodySmall, color = colorResource(R.color.whiteBlue))
            }
        }

    }

}