package just_pouya.data.repositories

import just_pouya.data.db.daos.MessageDao
import just_pouya.data.entities.toMessage
import just_pouya.data.mapper.toMessageData
import justp.k.domain.entities.Message
import justp.k.domain.repositories.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MessageRepositoryImpl @Inject constructor(private val messageDao: MessageDao) :
    MessageRepository {
    override fun getMessages(macAddress: String): Flow<List<Message>> =
        messageDao.getAllMessages(macAddress)
            .map { list -> list.map { it.toMessage() } }
            .flowOn(Dispatchers.IO)

    override suspend fun deleteMessages(macAddress: Message) =
        messageDao.delete(macAddress.toMessageData())

    override suspend fun addMessage(message: Message) = messageDao.insert(message.toMessageData())
}