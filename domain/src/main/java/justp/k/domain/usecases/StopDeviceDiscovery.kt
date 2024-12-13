package justp.k.domain.usecases


import justp.k.domain.repositories.BluetoothRepository
import javax.inject.Inject

class StopDeviceDiscovery @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
) {
    fun execute()= bluetoothRepository.stopDiscovery()
}