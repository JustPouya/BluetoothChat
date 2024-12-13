package just_pouya.data.services

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.util.Log
import justp.k.domain.common.Constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class BluetoothConnectionService(private val bluetoothAdapter: BluetoothAdapter?) {

    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val NAME = "BluetoothConnectionService"

    fun isBluetoothAvailable() = bluetoothAdapter != null
    fun isBluetoothEnabled(): Boolean = bluetoothAdapter?.isEnabled ?: false
    fun getPairedDevices(): List<BluetoothDevice> =
        bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()

    suspend fun listenForConnection(connectionSucceeded:suspend (BluetoothSocket) -> Unit) {
        val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID)
        }
        withContext(Dispatchers.IO) {
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e(TAG, "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }
                socket?.also {
                    connectionSucceeded.invoke(it)
                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }
        }
    }

    suspend fun connect(macAddress: String, connectionSucceeded:suspend (BluetoothSocket) -> Unit) {
        val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.getRemoteDevice(macAddress)
                ?.createRfcommSocketToServiceRecord(MY_UUID)
        }
        withContext(Dispatchers.IO){
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter?.cancelDiscovery()
            mmSocket?.let { socket ->
                /*Connect to the remote device through the socket. This call blocks
                until it succeeds or throws an exception.*/
                try {
                    socket.connect()
                    /*The connection attempt succeeded. Perform work associated with
                    the connection in a separate thread.*/
                    connectionSucceeded.invoke(socket)
                } catch (e: IOException) {
                    Log.e(TAG, "Socket's connect() method failed", e)
                }
            }
        }
    }



}