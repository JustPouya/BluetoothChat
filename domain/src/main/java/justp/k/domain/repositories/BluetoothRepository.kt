package justp.k.domain.repositories

import justp.k.domain.entities.BluetoothState
import justp.k.domain.entities.Device
import kotlinx.coroutines.flow.Flow


interface BluetoothRepository {
    suspend fun connect(macAddress: String)
    suspend fun makeServerSocket()
    suspend fun send(bytes: ByteArray)
    fun getState(): Flow<BluetoothState>
    fun getConnectionSucceeded(): Flow<Pair<String,String>>
    fun getIsAvailable(): Boolean
    fun getPairedDevices(): List<Device>
    fun getNewDevices(): Flow<List<Device>>
    fun startDiscovery()
    fun stopDiscovery()
    fun getEnabled(): Boolean
    fun closeConnection()
}