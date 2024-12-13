package just_pouya.bluetoothchat.entities

import justp.k.domain.entities.Device


data class DeviceApp(val macAddress:String, val name :String)

fun Device.toApp() = DeviceApp(
    macAddress=macAddress,
    name=name
)

fun DeviceApp.toDevice() = Device(
    macAddress=macAddress,
    name=name
)