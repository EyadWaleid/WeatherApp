package com.example.weatherapp.screens.alert.view.components.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.weatherapp.R

@Composable
fun AddressText(modifier: Modifier= Modifier,address: Int){
    Text(
        text = stringResource(address),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
        color = colorResource(R.color.greyBlue)
    )
}