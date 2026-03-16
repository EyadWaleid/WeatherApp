package com.example.weatherapp.screens.alert.view.components.bottomsheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R

@Composable
fun HoursCard(modifier: Modifier= Modifier, value: String, address: Int, onClick:()->Unit){
    Card(
       modifier =  Modifier.width(146.dp).height(47.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.lightDarkBlue)
        ),
        onClick = {onClick()},
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(R.color.darkSlateBlue)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(Modifier.padding(vertical = 12.dp, horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(address), textAlign = TextAlign.Center, color = colorResource(R.color.greyBlue) ,style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Text(value,color = colorResource(R.color.whiteBlue) ,style = MaterialTheme.typography.bodyLarge)
        }
    }
}