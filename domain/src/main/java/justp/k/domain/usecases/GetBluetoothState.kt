package justp.k.domain.usecases

import justp.k.domain.entities.BluetoothState
import justp.k.domain.repositories.BluetoothRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBluetoothState @Inject constructor(private val bluetoothRepository: BluetoothRepository) {
    fun execute(): Flow<BluetoothState> = bluetoothRepository.getState()
}