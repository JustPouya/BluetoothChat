package justp.k.domain.usecases

import justp.k.domain.entities.Device
import justp.k.domain.repositories.DeviceRepository
import javax.inject.Inject

class AddDevice @Inject constructor(private val deviceRepository: DeviceRepository) {
    suspend fun execute(device: Device) {
        deviceRepository.addDevice(device)
    }
}