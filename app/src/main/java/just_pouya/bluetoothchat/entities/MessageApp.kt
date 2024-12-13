package just_pouya.bluetoothchat.entities

import justp.k.domain.entities.Message

data class MessageApp(
    val id: Int?,
    val messageTxt: String,
    val itsMe: Boolean,
    val deviceMacAddress: String
)

fun Message.toApp() =
    MessageApp(id = id, messageTxt = messageTxt, itsMe = itsMe, deviceMacAddress = macAddress)

fun MessageApp.toMessage() =
    Message( messageTxt = messageTxt, itsMe = itsMe, macAddress = deviceMacAddress)