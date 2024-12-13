package just_pouya.data.mapper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import just_pouya.data.entities.BluetoothStateData
import just_pouya.data.entities.DeviceData
import just_pouya.data.entities.MessageData
import justp.k.domain.entities.BluetoothState
import justp.k.domain.entities.Device
import justp.k.domain.entities.Message

fun Device.toDeviceData() = DeviceData(macAddress, name)

fun Message.toMessageData() = MessageData( messageTxt=messageTxt, itsMe =  itsMe, macAddress =  macAddress)

@SuppressLint("MissingPermission")
fun BluetoothDevice.toDevice() = Device(name=name, macAddress = address)

fun BluetoothStateData.toBluetoothState():BluetoothState{
    return when (this) {
        is BluetoothStateData.ConnectionStateDataChanged.Connected ->
            BluetoothState.ConnectionStateChanged.Connected

        is BluetoothStateData.ConnectionStateDataChanged.DisConnected ->
            BluetoothState.ConnectionStateChanged.DisConnected

        is BluetoothStateData.Error -> BluetoothState.Error(this.message)
        is BluetoothStateData.Read -> BluetoothState.Read(this.message)
        is BluetoothStateData.Write -> BluetoothState.Write(this.message)
    }
}