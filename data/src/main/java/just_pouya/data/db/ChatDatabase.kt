package just_pouya.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import just_pouya.data.db.daos.DeviceDao
import just_pouya.data.db.daos.MessageDao
import just_pouya.data.entities.DeviceData
import just_pouya.data.entities.MessageData

@Database(entities = [DeviceData::class, MessageData::class], version = 1, exportSchema = false)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun messageDao(): MessageDao
}