package justp.k.domain.repositories

import justp.k.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessages(macAddress: String): Flow<List<Message>>
    suspend fun deleteMessages(macAddress: Message)
    suspend fun addMessage(message: Message)
}