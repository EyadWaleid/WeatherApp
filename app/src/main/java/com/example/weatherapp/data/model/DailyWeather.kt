package com.example.weatherapp.data.model

import java.util.concurrent.locks.Condition

data class  DailyWeather(val date:String,val maxTemp:Int,val minTemp:Int,val icon:String ,val condition:String)