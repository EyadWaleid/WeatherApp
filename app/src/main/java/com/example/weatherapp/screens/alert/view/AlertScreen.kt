package com.example.weatherapp.screens.alert.view

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.data.model.entity.UserAlerts
import com.example.weatherapp.screens.alert.view.components.body.AlertAppBar
import com.example.weatherapp.screens.alert.view.components.body.AlertCard
import com.example.weatherapp.screens.alert.view.components.bottomsheet.BottomSheet
import com.example.weatherapp.screens.alert.viewmodel.AlertViewModel
import com.example.weatherapp.utils.constants.ThereIsNoAlerts
import com.example.weatherapp.utils.constants.TimeUtils.calculateDuration
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen(modifier: Modifier = Modifier, alertViewModel: AlertViewModel,snackbarHostState: SnackbarHostState) {
    val alertState by alertViewModel.alertState.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val snackbarEvent by alertViewModel.snackbarEvent.collectAsState()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showBottomSheet = true
        }
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(snackbarEvent) {
        snackbarEvent?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short,
                )
                alertViewModel.clearEvent()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            alertViewModel.clearEvent()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.darkBlue))
    ) {


        when (alertState) {
            is AlertViewModel.AlertState.Data -> {
                val data = (alertState as AlertViewModel.AlertState.Data).userAlerts
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    AlertAppBar()
                    LazyColumn(Modifier.fillMaxSize()) {
                        item {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Notification & Alarm alerts",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = colorResource(R.color.greyBlue),

                                    )
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(
                                    "${(alertState as AlertViewModel.AlertState.Data).userAlerts.size}  Alerts",
                                    color = colorResource(R.color.blue),
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                                )
                            }
                            Spacer(Modifier.height(24.dp))
                        }
                        items(data.size) {
                            AlertCard(
                                title = data[it].name,
                                duration = calculateDuration(data[it].from,data[it].to),
                                alertType = data[it].type,
                                isActive = data[it].isOn,
                                onDelete = {
                                    alertViewModel.deleteAlarm(data[it])
                                },

                            )
                        }

                    }

                }
            }

            is AlertViewModel.AlertState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ThereIsNoAlerts()
                    Spacer(Modifier.size(10.dp))
                    Text(
                        text = stringResource(R.string.no_alerts),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        color = colorResource(R.color.greyBlue)
                    )
                }

            }

            is AlertViewModel.AlertState.Error -> {

            }

            is AlertViewModel.AlertState.Loading -> {

            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = colorResource(R.color.blue),
            onClick = { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                showBottomSheet = true
            } }
        ) {
            Icon(painter = painterResource(R.drawable.outline_add_24), contentDescription = "")
        }

        if (showBottomSheet) {
            BottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                onCreateAlert = { name, fromTime, toTime, alertType ->
                    showBottomSheet = false
                    alertViewModel.insertAlarm(
                        UserAlerts(
                            from = fromTime.toString(),
                            to = toTime.toString(),
                            isOn = true,
                            name = name,
                            type = alertType
                        )

                    )
                }
            )
        }
    }
}