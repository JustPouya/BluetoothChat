package just_pouya.data.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import just_pouya.data.entities.DeviceData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DiscoveryReceiver : BroadcastReceiver() {

    private val _discoveredDevicesChannel = Channel<List<DeviceData>>()
    val discoveredDevices: Flow<List<DeviceData>> = _discoveredDevicesChannel.receiveAsFlow()

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                val device: BluetoothDevice? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(
                            BluetoothDevice.EXTRA_DEVICE,
                            BluetoothDevice::class.java
                        )
                    } else {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }
                device?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        _discoveredDevicesChannel.send(
                            _discoveredDevicesChannel.receive()
                                    +
                                    DeviceData(it.name, it.address)
                        )
                    }
                }
            }
        }
    }
}
