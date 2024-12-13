package justp.k.domain.repositories

import justp.k.domain.entities.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getDevices(): Flow<List<Device>>
    suspend fun addDevice(device: Device)
    suspend fun deleteDevice(device: Device)
}