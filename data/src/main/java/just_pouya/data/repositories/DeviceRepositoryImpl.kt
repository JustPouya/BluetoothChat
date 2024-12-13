package just_pouya.data.repositories

import just_pouya.data.db.daos.DeviceDao
import just_pouya.data.entities.toDevice
import just_pouya.data.mapper.toDeviceData
import justp.k.domain.entities.Device
import justp.k.domain.repositories.DeviceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DeviceRepositoryImpl @Inject constructor(private val deviceDao: DeviceDao) :
    DeviceRepository {

    override fun getDevices(): Flow<List<Device>> = deviceDao.getAllDevices()
        .map { list -> list.map { it.toDevice() } }
        .flowOn(Dispatchers.IO)

    override suspend fun addDevice(device: Device) = deviceDao.insertOrUpdate(device.toDeviceData())

    override suspend fun deleteDevice(device: Device) = deviceDao.delete(device.toDeviceData())
}