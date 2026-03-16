package com.example.weatherapp.screens.alert.view.components.bottomsheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.weatherapp.R

@Composable
fun CustomEditText(
    modifier: Modifier = Modifier,
    hint: Int,
    onValueChange: (String) -> Unit,
    error: String = ""
) {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        modifier = modifier.fillMaxWidth(),
        isError = error.isNotEmpty(),
        placeholder = {
            Text(stringResource(hint), style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.greyBlue))
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.lightDarkBlue),
            unfocusedContainerColor = colorResource(R.color.lightDarkBlue),
            focusedBorderColor = colorResource(R.color.blueWithOpcity),
            unfocusedBorderColor = colorResource(R.color.darkSlateBlue),
            focusedTextColor = colorResource(R.color.greyBlueDark),
            unfocusedTextColor = colorResource(R.color.greyBlueDark),

        )
    )
}