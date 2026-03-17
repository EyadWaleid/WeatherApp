package com.example.weatherapp.screens.settings.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.utils.constants.Language

@Composable
fun LanguageDropdown(languageCode: String, onClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage = languageCode
    val languages = listOf(Language.ENGLISH, Language.ARABIC)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                colorResource(R.color.darkBlueWithOpacity),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { expanded = !expanded }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_globe_24),
                    tint = Color(0xFF137FEC),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        stringResource(R.string.display_language),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        if (selectedLanguage == "en") stringResource(Language.ENGLISH.resId) else stringResource(
                            Language.ARABIC.resId
                        ), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White
                    )

                }
            }
            Icon(
                painter = if (expanded) painterResource(R.drawable.outline_arrow_upward_alt_24) else painterResource(
                    R.drawable.outline_arrow_downward_alt_24
                ),
                contentDescription = null,
                tint = Color.Gray
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = colorResource(R.color.darkBlue))
                .fillMaxWidth()
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    onClick = {
                        if (selectedLanguage != language.code) {
                            selectedLanguage = language.code
                            onClick(selectedLanguage)
                        }
                        expanded = false
                    },
                    text = {
                        Text(text = stringResource(language.resId), color = Color.White)
                    },

                    )
            }
        }
    }
}