package just_pouya.data.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import just_pouya.data.entities.MessageData
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM message WHERE macAddress = :macAddress")
    fun getAllMessages(macAddress: String): Flow<List<MessageData>>
    @Insert
    suspend fun insert(message: MessageData)
    @Delete
    suspend fun delete(message: MessageData)
}
