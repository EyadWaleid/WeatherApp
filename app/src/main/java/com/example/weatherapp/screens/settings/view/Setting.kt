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
import androidx.compose.runtime.DisposableEffect
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
import com.example.weatherapp.screens.settings.view.components.LanguageDropdown
import com.example.weatherapp.screens.settings.view.components.LocationTrackerBtnToggle
import com.example.weatherapp.screens.settings.view.components.TemperatureUnitsBtnToggle
import com.example.weatherapp.screens.settings.view.components.WindUnitsBtnToggle
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
    DisposableEffect(Unit) {
        onDispose {
            settingViewModel.clearEvent()
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








