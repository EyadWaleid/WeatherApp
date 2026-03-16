package com.example.weatherapp.screens.alert.view.components.bottomsheet

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.weatherapp.R
import com.example.weatherapp.screens.alert.viewmodel.AlertViewModel
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCreateAlert: (name: String, fromTime: LocalTime, toTime: LocalTime, alertType: String)->Unit) {
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        containerColor = colorResource(R.color.darkBlue)
    ) {
        BottomSheetBody() { name, fromTime, toTime, alertType ->
            onCreateAlert(name, fromTime, toTime, alertType)
        }
    }
}