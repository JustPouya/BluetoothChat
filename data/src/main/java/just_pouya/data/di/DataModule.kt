package just_pouya.data.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import just_pouya.data.services.BluetoothConnectionService
import just_pouya.data.services.MyBluetoothService
import just_pouya.data.util.BluetoothDeviceDiscoveryManager
import just_pouya.data.util.DiscoveryReceiver
import justp.k.domain.repositories.BluetoothRepository
import justp.k.domain.repositories.DeviceRepository
import justp.k.domain.repositories.MessageRepository
import justp.k.domain.usecases.AddDevice
import justp.k.domain.usecases.AddMessage
import justp.k.domain.usecases.CloseBluetoothConnection
import justp.k.domain.usecases.ConnectToBluetoothDevice
import justp.k.domain.usecases.GetDevices
import justp.k.domain.usecases.GetDiscoveredDevices
import justp.k.domain.usecases.GetIsBluetoothAvailable
import justp.k.domain.usecases.GetMessages
import justp.k.domain.usecases.GetPairedDevices
import justp.k.domain.usecases.MakeBluetoothServerSocket
import justp.k.domain.usecases.SendMessage
import justp.k.domain.usecases.GetBluetoothEnabled
import justp.k.domain.usecases.GetBluetoothState
import justp.k.domain.usecases.GetConnectionSucceeded
import justp.k.domain.usecases.StartDeviceDiscovery
import justp.k.domain.usecases.StopDeviceDiscovery
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideBluetoothAdapter(@ApplicationContext context: Context): BluetoothAdapter? =
        context.getSystemService(BluetoothManager::class.java).adapter

    @Singleton
    @Provides
    fun provideDiscoveryReceiver(): DiscoveryReceiver =
        DiscoveryReceiver()

    @Singleton
    @Provides
    fun provideBluetoothDeviceDiscoveryManager(
        @ApplicationContext context: Context,
        discoveryReceiver: DiscoveryReceiver
    ): BluetoothDeviceDiscoveryManager =
        BluetoothDeviceDiscoveryManager(context, discoveryReceiver)

    @Singleton
    @Provides
    fun provideBluetoothConnectionService(
        bluetoothAdapter: BluetoothAdapter?
    ): BluetoothConnectionService {
        return BluetoothConnectionService(bluetoothAdapter)
    }

    @Singleton
    @Provides
    fun provideMyBluetoothService(
        bluetoothConnectionService: BluetoothConnectionService
    ): MyBluetoothService {
        return MyBluetoothService(bluetoothConnectionService)
    }

    @Provides
    fun provideAddDeviceUseCase(deviceRepository: DeviceRepository): AddDevice =
        AddDevice(deviceRepository)

    @Provides
    fun provideGetDevicesUseCase(deviceRepository: DeviceRepository): GetDevices =
        GetDevices(deviceRepository)

    @Provides
    fun provideAddMessageUseCase(messageRepository: MessageRepository): AddMessage =
        AddMessage(messageRepository)


    @Provides
    fun provideGetMessagesUseCase(messageRepository: MessageRepository): GetMessages =
        GetMessages(messageRepository)

    @Provides
    fun provideGetIsBluetoothAvailableUseCase(bluetoothRepository: BluetoothRepository):
            GetIsBluetoothAvailable = GetIsBluetoothAvailable(bluetoothRepository)

    @Provides
    fun provideGetBluetoothEnabledUseCase(bluetoothRepository: BluetoothRepository): GetBluetoothEnabled =
        GetBluetoothEnabled(bluetoothRepository)

    @Provides
    fun provideGetConnectionSucceededUseCase(bluetoothRepository: BluetoothRepository)
            : GetConnectionSucceeded = GetConnectionSucceeded(bluetoothRepository)

    @Provides
    fun provideGetBluetoothStateUseCase(bluetoothRepository: BluetoothRepository)
            : GetBluetoothState = GetBluetoothState(bluetoothRepository)

    @Provides
    fun provideGetPairedDevicesUseCase(bluetoothRepository: BluetoothRepository): GetPairedDevices =
        GetPairedDevices(bluetoothRepository)

    @Provides
    fun provideMakeBluetoothServerSocketUseCase(bluetoothRepository: BluetoothRepository):
            MakeBluetoothServerSocket = MakeBluetoothServerSocket(bluetoothRepository)

    @Provides
    fun provideConnectToBluetoothDeviceUseCase(bluetoothRepository: BluetoothRepository):
            ConnectToBluetoothDevice = ConnectToBluetoothDevice(bluetoothRepository)
    
    @Provides
    fun provideSendMessageUseCase(bluetoothRepository: BluetoothRepository): SendMessage =
        SendMessage(bluetoothRepository)

    @Provides
    fun provideGetDiscoveredDevicesUseCase(
        bluetoothDiscoveryRepository: BluetoothRepository
    ): GetDiscoveredDevices = GetDiscoveredDevices(bluetoothDiscoveryRepository)

    @Provides
    fun provideStartDeviceDiscoveryUseCase(
        bluetoothDiscoveryRepository: BluetoothRepository
    ): StartDeviceDiscovery = StartDeviceDiscovery(bluetoothDiscoveryRepository)

    @Provides
    fun provideStopDeviceDiscoveryUseCase(
        bluetoothDiscoveryRepository: BluetoothRepository
    ): StopDeviceDiscovery = StopDeviceDiscovery(bluetoothDiscoveryRepository)

    @Provides
    fun provideCloseBluetoothConnectionUseCase(
        bluetoothRepository: BluetoothRepository
    ): CloseBluetoothConnection = CloseBluetoothConnection(bluetoothRepository)
}

