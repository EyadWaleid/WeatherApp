package com.example.weatherapp.screens.settings.view

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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.screens.settings.viewmodel.SettingViewModel
import com.example.weatherapp.utils.constants.Language
import com.example.weatherapp.utils.constants.LocationSource
import com.example.weatherapp.utils.constants.TempUnits
import com.example.weatherapp.utils.constants.Units
import kotlinx.coroutines.launch

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    settingViewModel: SettingViewModel,
    snackbarHostState: SnackbarHostState,
    onMapClick: () -> Unit
) {
    val userSettingsState by settingViewModel.settingsState.collectAsState()
    val snackbarEvent by settingViewModel.snackbarEvent.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(snackbarEvent) {
        snackbarEvent?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short,
                )
                settingViewModel.clearEvent()
            }
        }
    }
    when (userSettingsState) {
        is SettingViewModel.SettingsState.Data -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.darkBlue))
                    .padding(8.dp),
            ) {
                AppBar()
                Spacer(modifier = Modifier.size(32.dp))
                Text(
                    stringResource(R.string.location_source),
                    color = colorResource(R.color.blue),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.size(12.dp))
                LocationTrackerBtnToggle(
                    onClick = {
                        onMapClick()
                    },
                    locationSource = (userSettingsState as SettingViewModel.SettingsState.Data).locationSource,
                    settingViewModel = settingViewModel
                )
                Spacer(modifier = Modifier.size(32.dp))
                Text(
                    stringResource(R.string.temperature_units),
                    color = colorResource(R.color.blue),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.size(12.dp))
                TemperatureUnitsBtnToggle(
                    tempUInt = (userSettingsState as SettingViewModel.SettingsState.Data).tempUnit,
                    onClick = {
                        when (it) {
                            0 -> settingViewModel.setTempUnit(TempUnits.CELSIUS.displayName)
                            1 -> settingViewModel.setTempUnit(TempUnits.FAHRENHEIT.displayName)
                            2 -> settingViewModel.setTempUnit(TempUnits.KELVIN.displayName)
                        }
                    })
                Spacer(modifier = Modifier.size(32.dp))
                Text(
                    stringResource(R.string.wind_speed_units),
                    color = colorResource(R.color.blue),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.size(12.dp))
                WindUnitsBtnToggle(
                    windUnit = (userSettingsState as SettingViewModel.SettingsState.Data).windUnit,
                    onClick = {
                        when (it) {
                            0 -> settingViewModel.setWindUnit(Units.METERS_PER_SECOND.displayName)
                            1 -> settingViewModel.setWindUnit(Units.MILES_PER_HOUR.displayName)
                        }
                    })
                Spacer(modifier = Modifier.size(32.dp))
                LanguageDropdown(
                    (userSettingsState as SettingViewModel.SettingsState.Data).language,
                    onClick = {
                        settingViewModel.setLanguage(it)
                    })
            }
        }

        is SettingViewModel.SettingsState.Error -> {
            Text("There is no data wait please ")
        }

        is SettingViewModel.SettingsState.Loading -> {
            Text("Setting data for u wait XD")
        }
    }
}

@Composable
fun AppBar(modifier: Modifier = Modifier) {
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
            text = stringResource(R.string.setting),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.white)
        )
    }
}

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

    val options = mutableListOf("GPS", "Map")
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
fun TemperatureUnitsBtnToggle(
    modifier: Modifier = Modifier,
    tempUInt: String,
    onClick: (Int) -> Unit
) {


    val selectedIndex = when (tempUInt) {
        TempUnits.CELSIUS.displayName -> 0
        TempUnits.FAHRENHEIT.displayName -> 1
        TempUnits.KELVIN.displayName -> 2
        else -> 0
    }

    val options = listOf(R.string.celsius, R.string.fahrenheit, R.string.kelvin)
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
                        .clip(RoundedCornerShape(15.dp))
                        .background(
                            if (isSelected)
                                Color(0xFF3A4A5F)
                            else
                                Color.Transparent
                        )
                        .clickable {
                            if (selectedIndex != index) {
                                onClick(index)
                            }
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
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WindUnitsBtnToggle(modifier: Modifier = Modifier, onClick: (Int) -> Unit, windUnit: String) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(windUnit) {
        selectedIndex = when (windUnit) {
            Units.METERS_PER_SECOND.displayName -> 0
            Units.MILES_PER_HOUR.displayName -> 1
            else -> 0
        }
    }

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

@Composable
fun LanguageDropdown(languageCode: String, onClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage = languageCode
    val languages = listOf(Language.ENGLISH, Language.ARABIC)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colorResource(R.color.darkBlueWithOpacity),
                shape = RoundedCornerShape(10.dp)
            )
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
                    Text(
                        stringResource(R.string.display_language),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        if (selectedLanguage == "en") stringResource(Language.ENGLISH.resId) else stringResource(
                            Language.ARABIC.resId
                        ), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White
                    )

                }
            }
            Icon(
                painter = if (expanded) painterResource(R.drawable.outline_arrow_upward_alt_24) else painterResource(
                    R.drawable.outline_arrow_downward_alt_24
                ),
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
                        if (selectedLanguage != language.code) {
                            selectedLanguage = language.code
                            onClick(selectedLanguage)
                        }
                        expanded = false
                    },
                    text = {
                        Text(text = stringResource(language.resId), color = Color.White)
                    },

                    )
            }
        }
    }
}
