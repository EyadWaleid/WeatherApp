package com.example.weatherapp.screens.alert.view.components.body
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.screens.alert.view.components.bottomsheet.HoursCard
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeRangeRow(
    onTimeSelected: (fromTime: LocalTime, toTime: LocalTime) -> Unit
) {
    var fromTime by remember { mutableStateOf(LocalTime.of(0, 0)) }
    var toTime by remember { mutableStateOf(LocalTime.of(0, 0)) }

    val fromDialogState = rememberMaterialDialogState()
    val toDialogState = rememberMaterialDialogState()

    MaterialDialog(
        dialogState = fromDialogState,
        backgroundColor = colorResource(R.color.lightDarkBlue),
        buttons = {
            positiveButton(
                text = "OK",
                textStyle = TextStyle(color = colorResource(R.color.blue))
            )
            negativeButton(
                text = "Cancel",
                textStyle = TextStyle(color = colorResource(R.color.greyBlue))
            )
        }
    ) {
        timepicker(
            initialTime = fromTime,
            title = "Select From Time",
            is24HourClock = false,
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = colorResource(R.color.blue),
                inactiveBackgroundColor = colorResource(R.color.darkBlue),
                activeTextColor = Color.White,
                inactiveTextColor = colorResource(R.color.greyBlue),
                selectorColor = colorResource(R.color.blue),
                selectorTextColor = Color.White,
                headerTextColor = Color.White,
                borderColor = Color.Transparent
            )
        ) { time ->
            fromTime = time
            if (time >= toTime) {
                toTime = time.plusMinutes(30).let {
                    if (it.hour == 0 && it.minute == 0) LocalTime.of(23, 59) else it
                }
            }
            onTimeSelected(fromTime, toTime)
        }
    }

    MaterialDialog(
        dialogState = toDialogState,
        backgroundColor = colorResource(R.color.lightDarkBlue),
        buttons = {
            positiveButton(
                text = "OK",
                textStyle = TextStyle(color = colorResource(R.color.blue))
            )
            negativeButton(
                text = "Cancel",
                textStyle = TextStyle(color = colorResource(R.color.greyBlue))
            )
        }
    ) {
        timepicker(
            initialTime = toTime,
            title = "Select To Time",
            is24HourClock = false,
            timeRange = fromTime.plusMinutes(1)..LocalTime.of(23, 59),
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = colorResource(R.color.blue),
                inactiveBackgroundColor = colorResource(R.color.darkBlue),
                activeTextColor = Color.White,
                inactiveTextColor = colorResource(R.color.greyBlue),
                selectorColor = colorResource(R.color.blue),
                selectorTextColor = Color.White,
                headerTextColor = Color.White,
                borderColor = Color.Transparent
            )
        ) { time ->
            toTime = time
            onTimeSelected(fromTime, toTime)
        }
    }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HoursCard(
            modifier = Modifier.weight(1f),
            address = R.string.from,
            value = fromTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        ) {
            fromDialogState.show()
        }

        HoursCard(
            modifier = Modifier.weight(1f),
            address = R.string.to,
            value = toTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        ) {
            toDialogState.show()
        }
    }
}