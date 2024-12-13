package just_pouya.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import just_pouya.data.entities.DeviceData
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device")
    fun getAllDevices(): Flow<List<DeviceData>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrUpdate(device: DeviceData)
    @Delete
    suspend fun delete(device: DeviceData)
}
