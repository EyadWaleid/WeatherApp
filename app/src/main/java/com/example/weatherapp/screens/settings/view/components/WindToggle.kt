package com.example.weatherapp.screens.settings.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.utils.constants.Units

@Composable
fun WindUnitsBtnToggle(modifier: Modifier = Modifier, onClick: (Int) -> Unit, windUnit: String) {
    var selectedIndex =  when (windUnit) {
        Units.METERS_PER_SECOND.displayName -> 0
        Units.MILES_PER_HOUR.displayName -> 1
        else -> 0}

    val options = listOf(R.string.wind_speed_in_seeconds, R.string.wind_speed_in_hours)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(
                BorderStroke(
                    1.dp,
                    colorResource(R.color.blueWithOpcity)
                ),
                shape = RoundedCornerShape(15.dp)
            )
            .background(
                color = colorResource(R.color.darkBlueWithOpacity),
                shape = RoundedCornerShape(15)

            )
            .padding(4.dp),

        ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEachIndexed { index, label ->
                val isSelected = selectedIndex == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(
                            if (isSelected)
                                Color(0xFF3A4A5F)
                            else
                                Color.Transparent
                        )

                        .clickable {
                            onClick(index)
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(label),
                            color = if (isSelected)
                                Color(0xFF2F80ED)
                            else
                                Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}