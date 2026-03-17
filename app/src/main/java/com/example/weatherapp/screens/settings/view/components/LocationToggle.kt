package com.example.weatherapp.screens.settings.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.screens.settings.viewmodel.SettingViewModel
import com.example.weatherapp.utils.constants.LocationSource

@Composable
fun LocationTrackerBtnToggle(
    modifier: Modifier = Modifier,
    locationSource: String,
    onClick: () -> Unit,
    settingViewModel: SettingViewModel
) {
    var selectedIndex = when (locationSource) {
        LocationSource.GPS.displayName -> 0
        LocationSource.MAP.displayName -> 1
        else -> {
            0
        }
    }

    val options = mutableListOf(R.string.gps, R.string.map)
    val icons = listOf(
        R.drawable.baseline_location_pin_24,
        R.drawable.outline_map_24
    )
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
                            if (settingViewModel.checkConnectivity()) {
                                if (selectedIndex == index && index == 0) {
                                    return@clickable
                                } else if (index == 0) {
                                    settingViewModel.setLocationSourceToMap()
                                    return@clickable
                                }
                                selectedIndex = index
                                onClick()
                            }

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(icons[index]),
                            contentDescription = "",
                            tint = colorResource(if (isSelected) R.color.blue else R.color.greyBlue)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(label),
                            color = if (isSelected)
                                Color(0xFF2F80ED)
                            else
                                Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}