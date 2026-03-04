package com.example.weatherapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun  SettingScreen(modifier: Modifier= Modifier){
    Column (modifier = modifier.fillMaxSize().background(colorResource(R.color.darkBlue)).padding(8.dp),
      ) {
        AppBar()
        Spacer(modifier = Modifier.size(32.dp))
        Text("Location Source", color = colorResource(R.color.blue),style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.size(12.dp))
        LocationTrackerBtnToggle()
        Spacer(modifier = Modifier.size(32.dp))
        Text("Temperature Units", color = colorResource(R.color.blue),style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.size(12.dp))
        TemperatureUnitsBtnToggle()
        Spacer(modifier = Modifier.size(32.dp))
        Text("Wind Speed Units", color = colorResource(R.color.blue),style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.size(12.dp))
        WindUnitsBtnToggle()
        Spacer(modifier = Modifier.size(32.dp))
        LanguageDropdown()


    }
}

@Composable
fun AppBar(modifier: Modifier= Modifier){
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
            text = "Settings",
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.white)
        )
    }
}
@Composable
fun LocationTrackerBtnToggle(modifier: Modifier= Modifier){
    var selectedIndex by remember { mutableStateOf(0) }

    val options = listOf("GPS", "Map")
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
            ).background(
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
                        .clickable { selectedIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painter = painterResource(icons[index]), contentDescription = "", tint = colorResource(if (isSelected) R.color.blue else R.color.greyBlue))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = label,
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
@Composable
fun TemperatureUnitsBtnToggle(modifier: Modifier= Modifier){
    var selectedIndex by remember { mutableStateOf(0) }
    val options = listOf("Celsius (°C)", "Fahrenheit (°F)","Kelvin (K)")
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
            ).background(
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
                        .clip(RoundedCornerShape(15.dp))
                        .background(
                            if (isSelected)
                                Color(0xFF3A4A5F)
                            else
                                Color.Transparent
                        )

                        .clickable { selectedIndex = index },
                    contentAlignment = Alignment.Center
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = label,
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
@Composable
fun WindUnitsBtnToggle(modifier: Modifier= Modifier){
    var selectedIndex by remember { mutableStateOf(0) }
    val options = listOf("m/s", "mph")
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
            ).background(
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

                        .clickable { selectedIndex = index },
                    contentAlignment = Alignment.Center
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = label,
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
@Composable
fun LanguageDropdown() {
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English (US)") }
    val languages = listOf("English (US)", "Arabic (EG)",)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.darkBlueWithOpacity), shape = RoundedCornerShape(10.dp))
            .clickable { expanded = !expanded }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_globe_24),
                    tint = Color(0xFF137FEC),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Display Language", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(selectedLanguage, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
            Icon(
                painter = if (expanded) painterResource(R.drawable.outline_arrow_upward_alt_24) else painterResource(R.drawable.outline_arrow_downward_alt_24),
                contentDescription = null,
                tint = Color.Gray
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = colorResource(R.color.darkBlue))
                .fillMaxWidth()
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    onClick = {
                        selectedLanguage = language
                        expanded = false
                    },
                    text = {
                        Text(text = language, color = Color.White)

                           },

                )
            }
        }
    }
}
@Preview(showBackground = true , showSystemUi = true)
@Composable
fun SettingPreview() {
    WeatherAppTheme {
        Box (Modifier.fillMaxSize().background(color = colorResource(R.color.darkBlue)), contentAlignment = Alignment.Center){
          AppBar()
        }
    }
}