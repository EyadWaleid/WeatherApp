package com.example.weatherapp.screens.alert.view.components.body

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R

@Composable
fun AlertCard(
    title: String,
    duration: String,
    alertType: String,
    isActive: Boolean,
    onDelete:()-> Unit,
) {

    Card(
        modifier = Modifier.fillMaxWidth().padding(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.lightDarkBlue)
        ),
        border = BorderStroke(1.dp, colorResource(R.color.darkSlateBlue)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$duration min duration • $alertType",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.greyBlue)
                )
            }
            IconButton(
                onClick = {
                    onDelete()


                }, Modifier
                    .width(13.5.dp)
                    .height(15.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_delete_24),
                    tint = colorResource(R.color.greyBlue),
                    contentDescription = ""
                )
            }

        }
    }
}