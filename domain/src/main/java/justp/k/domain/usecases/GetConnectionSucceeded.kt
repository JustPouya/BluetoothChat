package justp.k.domain.usecases

import justp.k.domain.repositories.BluetoothRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConnectionSucceeded @Inject constructor(private val bluetoothRepository: BluetoothRepository) {
    fun execute(): Flow<Pair<String,String>> = bluetoothRepository.getConnectionSucceeded()
}