package justp.k.domain.usecases

import justp.k.domain.entities.Device
import justp.k.domain.repositories.BluetoothRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscoveredDevices @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
) {
    fun execute(): Flow<List<Device>> = bluetoothRepository.getNewDevices()
}