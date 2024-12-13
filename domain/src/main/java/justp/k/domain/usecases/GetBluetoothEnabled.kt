package justp.k.domain.usecases

import justp.k.domain.repositories.BluetoothRepository
import javax.inject.Inject

class GetBluetoothEnabled @Inject constructor(private val bluetoothRepository: BluetoothRepository) {
    fun  execute():Boolean = bluetoothRepository.getEnabled()
}