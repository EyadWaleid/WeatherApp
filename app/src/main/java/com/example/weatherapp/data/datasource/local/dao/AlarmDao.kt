package com.example.weatherapp.data.datasource.local.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherapp.data.model.entity.UserAlerts
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAlarm(userUserAlerts: UserAlerts): Long
    @Query("SELECT * FROM user_alarm ")
     fun getUserAlarms(): Flow<List<UserAlerts>>
    @Update
    suspend fun updateAlarm(userAlerts: UserAlerts)
    @Delete
    suspend fun deleteCity(userAlerts: UserAlerts)
    @Query("SELECT * FROM user_alarm WHERE id = :id LIMIT 1")
    suspend fun getAlarmById(id: Long): UserAlerts?
}