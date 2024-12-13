package just_pouya.data.entities

sealed class BluetoothStateData {

    sealed class ConnectionStateDataChanged() : BluetoothStateData() {
        data object DisConnected : ConnectionStateDataChanged()
        data object Connected : ConnectionStateDataChanged()
    }

    data class Write(val message: String) : BluetoothStateData()

    data class Read(val message: String) : BluetoothStateData()

    data class Error(val message: String) : BluetoothStateData()

}