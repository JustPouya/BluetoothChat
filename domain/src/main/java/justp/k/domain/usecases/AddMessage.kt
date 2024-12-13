package justp.k.domain.usecases

import justp.k.domain.entities.Message
import justp.k.domain.repositories.MessageRepository
import javax.inject.Inject

class AddMessage @Inject constructor(private val messageRepository: MessageRepository) {
    suspend fun execute(deviceMacAddress: String,messageTxt: String ,itsMe: Boolean) {
        messageRepository.addMessage(Message(messageTxt = messageTxt, itsMe = itsMe, macAddress = deviceMacAddress))
    }
}