package just_pouya.bluetoothchat.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import just_pouya.bluetoothchat.entities.DeviceApp
import just_pouya.bluetoothchat.entities.toApp
import just_pouya.bluetoothchat.entities.toDevice
import justp.k.domain.usecases.AddDevice
import justp.k.domain.usecases.ConnectToBluetoothDevice
import justp.k.domain.usecases.GetDevices
import justp.k.domain.usecases.GetDiscoveredDevices
import justp.k.domain.usecases.GetIsBluetoothAvailable
import justp.k.domain.usecases.GetPairedDevices
import justp.k.domain.usecases.MakeBluetoothServerSocket
import justp.k.domain.usecases.GetBluetoothEnabled
import justp.k.domain.usecases.GetConnectionSucceeded
import justp.k.domain.usecases.StartDeviceDiscovery
import justp.k.domain.usecases.StopDeviceDiscovery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDevices: GetDevices,
    private val getPairedDevices: GetPairedDevices,
    private val getBluetoothEnabled: GetBluetoothEnabled,
    private val connectToBluetoothDevice: ConnectToBluetoothDevice,
    private val getConnectionSucceeded: GetConnectionSucceeded,
    private val addDevice: AddDevice,
    private val getIsBluetoothAvailable: GetIsBluetoothAvailable,
    private val getDiscoveredDevices: GetDiscoveredDevices,
    private val startDeviceDiscovery: StartDeviceDiscovery,
    private val stopDeviceDiscovery: StopDeviceDiscovery,
    private val makeBluetoothServerSocket: MakeBluetoothServerSocket
) : ViewModel() {
    private val _devices: MutableStateFlow<List<DeviceApp>> = MutableStateFlow(emptyList())
    val device: StateFlow<List<DeviceApp>> = _devices

    private val _discoveredDevices: MutableStateFlow<List<DeviceApp>> =
        MutableStateFlow(emptyList())
    val discoveredDevices: StateFlow<List<DeviceApp>> = _discoveredDevices

    val pairedDevices: MutableState<List<DeviceApp>> = mutableStateOf(listOf())

    val isBluetoothAvailable: MutableState<Boolean> = mutableStateOf(getIsBluetoothAvailable.execute())

    val isBluetoothEnabled: MutableState<Boolean> = mutableStateOf(getBluetoothEnabled.execute())
        get() {
            field.value= getBluetoothEnabled.execute()
            return field
        }

    private val _connectionSucceeded= MutableStateFlow(Pair("",""))
    val connectionSucceeded:StateFlow<Pair<String,String>> =_connectionSucceeded


    init {
        viewModelScope.launch {

            launch {
                getDevices.execute().collect {
                    _devices.value = it.map { data -> data.toApp() }

                }
            }
            launch {
                getDiscoveredDevices.execute().collect {
                    _discoveredDevices.value = it.map { data -> data.toApp() }
                }
            }
            launch {
                getConnectionSucceeded.execute().collect {
                    addDevice(it.second, it.first)
                    _connectionSucceeded.value = it
                }
            }
        }
    }

    fun makeServerSocket() {
        viewModelScope.launch {
            makeBluetoothServerSocket.execute()
        }
    }

    fun connect(macAddress: String) {
        viewModelScope.launch {
            connectToBluetoothDevice.execute(macAddress)
        }
    }

    fun startDiscovery() {
        startDeviceDiscovery.execute()
    }

    fun stopDiscovery() {
        stopDeviceDiscovery.execute()
    }

    private fun addDevice(macAddress: String, deviceName: String) {
        viewModelScope.launch {
            addDevice.execute(DeviceApp(macAddress, deviceName).toDevice())
        }
    }

    fun getPairedDevices() {
        pairedDevices.value = getPairedDevices.execute().map { data -> data.toApp() }
    }

    fun resetConnectionSucceeded() {
        _connectionSucceeded.value= Pair("","")
    }
}