package justp.k.domain.usecases

import justp.k.domain.entities.Device
import justp.k.domain.repositories.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDevices @Inject constructor(private val deviceRepository: DeviceRepository) {
    fun execute(): Flow<List<Device>> = deviceRepository.getDevices()
}