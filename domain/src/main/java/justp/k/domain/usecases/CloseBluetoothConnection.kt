package justp.k.domain.usecases

import justp.k.domain.repositories.BluetoothRepository
import javax.inject.Inject

class CloseBluetoothConnection @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
) {
    fun execute() = bluetoothRepository.closeConnection()
}