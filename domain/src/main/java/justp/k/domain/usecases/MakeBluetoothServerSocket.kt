package justp.k.domain.usecases

import justp.k.domain.repositories.BluetoothRepository
import javax.inject.Inject

class MakeBluetoothServerSocket @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
) {
    suspend fun execute() = bluetoothRepository.makeServerSocket()
}