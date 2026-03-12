package com.example.weatherapp.utils
import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale
import androidx.core.content.edit
object AppLocalization {

        fun changeLanguage(context: Activity, languageCode: String) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.getSystemService(LocaleManager::class.java).applicationLocales =
                    LocaleList.forLanguageTags(languageCode)
            } else {
                context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
                    .edit {
                        putString("language", languageCode)
                    }
                val locale =  Locale.forLanguageTag(languageCode)
                Locale.setDefault(locale)
                val config = Configuration(context.resources.configuration)
                config.setLocale(locale)
                context.createConfigurationContext(config)
               context.recreate()


            }
        }



}