package just_pouya.bluetoothchat.nav

import kotlinx.serialization.Serializable

sealed  class Screen{

    @Serializable
    data object MainScreen: Screen()
    @Serializable
    data class ChatScreen(val contactName:String,val macAddress:String): Screen()

}