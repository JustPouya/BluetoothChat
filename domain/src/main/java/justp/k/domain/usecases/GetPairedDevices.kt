package justp.k.domain.usecases

import justp.k.domain.entities.Device
import justp.k.domain.repositories.BluetoothRepository
import javax.inject.Inject

class GetPairedDevices @Inject constructor(private val bluetoothRepository: BluetoothRepository) {
    fun execute():List<Device> = bluetoothRepository.getPairedDevices()
}