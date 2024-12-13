package just_pouya.data.services

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.util.Log
import just_pouya.data.entities.BluetoothStateData
import just_pouya.data.services.BluetoothConnectionService
import justp.k.domain.common.Constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("MissingPermission")
class MyBluetoothService(
    private val btConnectionService: BluetoothConnectionService
) : Closeable {

    private val mutex = Mutex()

    private val _connectionSucceeded = Channel<Pair<String,String>>(Channel.BUFFERED)
    val connectionSucceeded: Flow<Pair<String,String>> = _connectionSucceeded.receiveAsFlow()
        .flowOn(Dispatchers.IO)

    private val _state = MutableStateFlow<BluetoothStateData>(
        BluetoothStateData.ConnectionStateDataChanged.DisConnected
    )
    val state: StateFlow<BluetoothStateData> = _state.asStateFlow()

    private var connectedInputStream: InputStream? = null
    private var connectedOutputStream: OutputStream? = null

    fun getPairedDevices() = btConnectionService.getPairedDevices()

    fun isBluetoothAvailable(): Boolean = btConnectionService.isBluetoothAvailable()

    suspend fun makeServerSocket() {
        btConnectionService.listenForConnection { bluetoothSocket ->
            _connectionSucceeded.send(
                Pair(bluetoothSocket.remoteDevice.name, bluetoothSocket.remoteDevice.address)
            )
            handleConnection(bluetoothSocket)
        }
    }

    suspend fun connect(macAddress: String) {
        btConnectionService.connect(macAddress) { bluetoothSocket ->
            _connectionSucceeded.send(Pair(bluetoothSocket.remoteDevice.name,macAddress))
            handleConnection(bluetoothSocket)
        }
    }

    private fun changeState(bluetoothStateData: BluetoothStateData) {
        _state.value = bluetoothStateData
    }

    private suspend fun handleConnection(socket: BluetoothSocket) {

        changeState(BluetoothStateData.ConnectionStateDataChanged.Connected)

        mutex.withLock {
            connectedInputStream = socket.inputStream
            connectedOutputStream = socket.outputStream
        }
        withContext(Dispatchers.IO) {
            val buffer = ByteArray(1024)
            while (true) {
                try {
                    val bytesRead =
                        connectedInputStream?.read(buffer)
                            ?: throw IOException("InputStream closed")
                    changeState(BluetoothStateData.Read(String(buffer, 0, bytesRead)))
                } catch (e: IOException) {
                    Log.e(TAG, "Connection lost", e)
                    changeState(BluetoothStateData.ConnectionStateDataChanged.DisConnected)
                    break
                }
            }
            socket.close()
        }
    }

    suspend fun write(bytes: ByteArray) {
        withContext(Dispatchers.IO) {
            try {
                connectedOutputStream?.write(bytes)

                changeState(BluetoothStateData.Write(String(bytes)))
            } catch (e: IOException) {
                Log.e(TAG, "Error writing data", e)
                changeState(BluetoothStateData.Error("Couldn't send data to the other device"))
            }
        }
    }

    override fun close() {
        connectedInputStream?.close()
        connectedOutputStream?.close()
        connectedInputStream = null
        connectedOutputStream = null
    }

    fun getBluetoothEnabled(): Boolean = btConnectionService.isBluetoothEnabled()

}