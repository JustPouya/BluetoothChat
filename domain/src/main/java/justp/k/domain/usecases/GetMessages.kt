package justp.k.domain.usecases

import justp.k.domain.repositories.MessageRepository
import javax.inject.Inject

class GetMessages @Inject constructor(private val messageRepository: MessageRepository)  {
    fun execute(macAddress: String) = messageRepository.getMessages(macAddress)
}