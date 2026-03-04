package com.example.weatherapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R

// Set of Material typography styles to start with
val spaceGrotesk = FontFamily(
    Font(R.font.spacegrotesk_bold, FontWeight.Bold),
    Font(R.font.spacegrotesk_semibold, FontWeight.SemiBold),
    Font(R.font.spacegrotesk_medium, FontWeight.Medium),
    Font(R.font.spacegrotesk_regular, FontWeight.Normal),
)

val Typography = Typography(
    //display large 57/64
    //display medium 45/52
    //display small 36/44

    // headline large 32/40
    // headline medium 28/36
    // headline small 24/32

    //tittle large 22/28
    //tittle medium 16/24
    //tittle small 14/20


    // body large 16-24
    // body medium 14/20
    // body small 12/16

    // label large 14/20
    // label medium 12/16
    // label small 11/16
    displayLarge = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),

    titleMedium = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Medium,
       fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = spaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    )

)
