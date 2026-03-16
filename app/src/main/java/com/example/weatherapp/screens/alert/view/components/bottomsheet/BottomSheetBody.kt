package com.example.weatherapp.screens.alert.view.components.bottomsheet

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.screens.alert.view.components.body.TimeRangeRow
import com.example.weatherapp.screens.alert.viewmodel.AlertViewModel
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomSheetBody(modifier: Modifier = Modifier, onCreateAlert: (name: String, fromTime: LocalTime, toTime: LocalTime, alertType: String) -> Unit) {

    var alertName by remember { mutableStateOf("") }
    var fromTime by remember { mutableStateOf<LocalTime?>(null) }
    var toTime by remember { mutableStateOf<LocalTime?>(null) }
    var alertType by remember { mutableStateOf("Notification") }
    var isFormValid by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(R.color.darkBlueWithOpacity))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.config_new_alert),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = colorResource(R.color.greyBlue)
        )
        Spacer(Modifier.height(16.dp))

        AddressText(address = R.string.alert)
        Spacer(Modifier.height(8.dp))
        CustomEditText(
            hint = R.string.alert_naming,
            error = if (!isFormValid) "Enter name " else "",
            onValueChange = { alertName = it }
        )

        Spacer(Modifier.height(16.dp))

        AddressText(address = R.string.active_time)
        Spacer(Modifier.height(8.dp))
        TimeRangeRow(
            onTimeSelected = { from, to ->
                fromTime = from
                toTime = to
            }
        )

        if (!isFormValid) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Please select both from and to time",
                color = Color.Red.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start
            )
        }

        Spacer(Modifier.height(16.dp))

        AddressText(address = R.string.alert_type)
        Spacer(Modifier.height(8.dp))
        AlertType { selectedType ->
            alertType = selectedType
        }
        if (!isFormValid) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Please select an alert type",
                color = Color.Red.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start


            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (alertName.isNotBlank()
                    && fromTime != null
                    && toTime != null
                    && alertType.isNotBlank()) {
                    isFormValid=true
                    onCreateAlert(alertName, fromTime!!, toTime!!, alertType)
                }
                else{
                    isFormValid=false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (alertName.isNotBlank()
                    && fromTime != null
                    && toTime != null
                    && alertType.isNotBlank())
                    colorResource(R.color.blue)
                else
                    colorResource(R.color.greyBlue)
            )
        ) {
            Text(
                text = stringResource(R.string.alert),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}


