package just_pouya.bluetoothchat.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import just_pouya.bluetoothchat.entities.MessageApp
import just_pouya.bluetoothchat.entities.toApp
import justp.k.domain.entities.BluetoothState
import justp.k.domain.usecases.AddMessage
import justp.k.domain.usecases.CloseBluetoothConnection
import justp.k.domain.usecases.ConnectToBluetoothDevice
import justp.k.domain.usecases.GetMessages
import justp.k.domain.usecases.SendMessage
import justp.k.domain.usecases.GetBluetoothState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val sendMessage: SendMessage,
    private val getMessages: GetMessages,
    private val addMessage: AddMessage,
    private val getBluetoothState: GetBluetoothState,
    private val closeBluetoothConnection: CloseBluetoothConnection,
    private val connectToBluetoothDevice: ConnectToBluetoothDevice,
) : ViewModel() {

    private val macAddress: String = savedStateHandle["macAddress"] ?: ""

    private val _messages = MutableStateFlow<List<MessageApp>>(emptyList())
    val messages: StateFlow<List<MessageApp>> = _messages

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _toast = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toast


    init {
        viewModelScope.launch {
            launch {
                getMessages.execute(macAddress).collect { messages ->
                    _messages.value = messages.map { it.toApp() }.reversed()
                }
            }
            launch {
                getBluetoothState.execute().collect { state ->
                    when (state) {
                        is BluetoothState.ConnectionStateChanged -> {
                            when (state) {
                                BluetoothState.ConnectionStateChanged.Connected ->
                                    _isConnected.value = true

                                BluetoothState.ConnectionStateChanged.DisConnected ->
                                    _isConnected.value = false
                            }
                        }

                        is BluetoothState.Error -> _toast.update { state.message }

                        is BluetoothState.Read -> {
                            addMessage(macAddress, state.message, false)
                        }

                        is BluetoothState.Write -> addMessage(macAddress, state.message, true)

                    }
                }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            sendMessage.execute(message.toByteArray())
        }
    }

    private fun addMessage(deviceMacAddress: String, messageTxt: String, itsMe: Boolean) {
        viewModelScope.launch {
            addMessage.execute(deviceMacAddress, messageTxt, itsMe)
        }
    }

    fun reConnect() {
        viewModelScope.launch {
            connectToBluetoothDevice.execute(macAddress)
        }
    }

    override fun onCleared() {
        super.onCleared()
        closeBluetoothConnection.execute()
    }

}