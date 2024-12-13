package justp.k.domain.usecases

import justp.k.domain.repositories.BluetoothRepository
import javax.inject.Inject

class SendMessage @Inject constructor(private val bluetoothRepository: BluetoothRepository) {
    suspend fun execute(bytes: ByteArray) = bluetoothRepository.send(bytes)
}