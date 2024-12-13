package justp.k.domain.entities

sealed class BluetoothState {

    sealed class ConnectionStateChanged() : BluetoothState() {
        data object DisConnected : ConnectionStateChanged()
        data object Connected : ConnectionStateChanged()
    }

    data class Write(val message: String) : BluetoothState()

    data class Read(val message: String) : BluetoothState()

    data class Error(val message: String) : BluetoothState()

}