package just_pouya.data.repositories

import just_pouya.data.entities.toDevice
import just_pouya.data.mapper.toBluetoothState
import just_pouya.data.mapper.toDevice
import just_pouya.data.services.MyBluetoothService
import just_pouya.data.util.BluetoothDeviceDiscoveryManager
import justp.k.domain.entities.BluetoothState
import justp.k.domain.entities.Device
import justp.k.domain.repositories.BluetoothRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BluetoothRepositoryImpl @Inject constructor(
    private val myBluetoothService: MyBluetoothService,
    private val bluetoothDiscoveryManager: BluetoothDeviceDiscoveryManager
) : BluetoothRepository {

    override suspend fun connect(macAddress: String) = myBluetoothService.connect(macAddress)

    override suspend fun makeServerSocket() = myBluetoothService.makeServerSocket()

    override suspend fun send(bytes: ByteArray) = myBluetoothService.write(bytes)

    override fun getState(): Flow<BluetoothState> = myBluetoothService.state.map {
        it.toBluetoothState()
    }

    override fun getConnectionSucceeded(): Flow<Pair<String, String>> =
        myBluetoothService.connectionSucceeded


    override fun getIsAvailable(): Boolean = myBluetoothService.isBluetoothAvailable()

    override fun getPairedDevices(): List<Device> = myBluetoothService.getPairedDevices().map {
        it.toDevice()
    }

    override fun getNewDevices(): Flow<List<Device>> =
        bluetoothDiscoveryManager.getDiscoveredDevices().map { list -> list.map { it.toDevice() } }


    override fun startDiscovery() {
        bluetoothDiscoveryManager.startDiscovery()
    }

    override fun stopDiscovery() {
        bluetoothDiscoveryManager.stopDiscovery()
    }

    override fun getEnabled(): Boolean =myBluetoothService.getBluetoothEnabled()

    override fun closeConnection() =myBluetoothService.close()

}