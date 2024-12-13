package justp.k.domain.usecases

import justp.k.domain.repositories.BluetoothRepository
import javax.inject.Inject

class ConnectToBluetoothDevice @Inject constructor(private val bluetoothRepository: BluetoothRepository) {
   suspend fun execute(macAddress:String) = bluetoothRepository.connect(macAddress)
}