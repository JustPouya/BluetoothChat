package just_pouya.data.util

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import just_pouya.data.entities.DeviceData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BluetoothDeviceDiscoveryManager @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val discoveryReceiver: DiscoveryReceiver
) {
    fun getDiscoveredDevices(): Flow<List<DeviceData>> =discoveryReceiver.discoveredDevices

    fun startDiscovery() {
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(discoveryReceiver, filter)
    }

    fun stopDiscovery() {
        context.unregisterReceiver(discoveryReceiver)
    }
}